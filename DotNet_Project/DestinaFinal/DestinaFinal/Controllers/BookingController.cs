using DestinaFinal.Data;
using DestinaFinal.Models.Entities;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace DestinaFinal.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class BookingController : ControllerBase
    {
        public BookingController(ApplicationDbContext applicationDbContext, JwtService jwtService, EmailService emailService, NotificationService notificationService)
        {
            ApplicationDbContext = applicationDbContext;
            JwtService = jwtService;
            EmailService = emailService;
            NotificationService = notificationService;
        }

        public ApplicationDbContext ApplicationDbContext { get; }
        public JwtService JwtService { get; }
        public EmailService EmailService { get; }
        public NotificationService NotificationService { get; }

        //POST: api/booking
        [HttpPost]
        [HttpPost]
        public async Task<ActionResult<Booking>> CreateBooking(Booking booking)
        {
            var package = await ApplicationDbContext.Packages.FindAsync(booking.PackageId);
            if (package == null)
            {
                return BadRequest("Package not found.");
            }

            // Calculate the total amount based on the price per person and number of travelers
            booking.TotalAmount = package.PricePerPerson * booking.NumberOfTravelers;

            ApplicationDbContext.Bookings.Add(booking);
            await ApplicationDbContext.SaveChangesAsync();

            var user = await ApplicationDbContext.Users.FindAsync(booking.CustomerId);
            var agent = await ApplicationDbContext.Users.FindAsync(booking.AgentId);

            // Decrease the number of available seats in the package
            package.NumberOfSeatsAvailable -= booking.NumberOfTravelers;
            if (package.NumberOfSeatsAvailable < 0)
            {
                return BadRequest("Not enough seats available.");
            }

            ApplicationDbContext.Packages.Update(package);
            await ApplicationDbContext.SaveChangesAsync();

            const string subject = "Booking Successful";
            var body = $"""
                        <html>
                            <body>
                                <h1>Hello, {user.FirstName} {user.LastName}</h1>
                                <h2>
                                    Your booking for <strong>{package.Title}</strong> has been successfully confirmed!
                                </h2>
                                <p>
                                    <strong>Start Date:</strong> {package.StartDate:MMMM dd, yyyy}<br>
                                    <strong>End Date:</strong> {package.EndDate:MMMM dd, yyyy}<br>
                                    <strong>Total Amount:</strong> ${booking.TotalAmount}
                                </p>
                                <h3>Agent Information:</h3>
                                <p>
                                    <strong>Name:</strong> {agent.FirstName} {agent.LastName}<br>
                                    <strong>Email:</strong> {agent.Email}<br>
                                    <strong>Mobile:</strong> {agent.MobileNumber}
                                </p>
                                <p>
                                    If you have any questions or need further assistance, feel free to contact us.
                                </p>
                                <h3>Thank you for choosing Destina!</h3>
                            </body>
                        </html>
                        """;


            if (string.IsNullOrEmpty(user.Email))
            {
                throw new InvalidOperationException("User email is null or empty.");
            }

            try
            {
                EmailService.SendEmail(user.Email, subject, body);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error sending email: {ex.Message}");
                throw;
            }

            await NotificationService.SaveNotificationAsync(subject, user.Id, agent.Id);

            return Ok("Booking Created Successfully");
        }


        //DELETE: api/booking/3
        //delete booking with the booking id
        [HttpDelete("{id}")]
        public async Task<IActionResult> CancelBooking(int id)
        {
            var booking = await ApplicationDbContext.Bookings.FindAsync(id);
            if (booking == null)
            {
                return Ok("Booking Not Found");
            }

            var user = await ApplicationDbContext.Users.FindAsync(booking.CustomerId);
            var agent = await ApplicationDbContext.Users.FindAsync(booking.AgentId);
            var package = await ApplicationDbContext.Packages.FindAsync(booking.PackageId);

            // Increase the number of available seats in the package
            package.NumberOfSeatsAvailable += booking.NumberOfTravelers;


            booking.BookingStatus = "Cancelled";

            var Payment = await ApplicationDbContext.Payments.FirstOrDefaultAsync(p => p.BookingId == id);
            // Update the payment status to "Refund"
            if (Payment != null)
            {
                Payment.PaymentStatus = "Refund";
                ApplicationDbContext.Payments.Update(Payment);
            }

            const string subject = "Booking Cancelled";
            var body = $"""
                        <html>
                            <body>
                                <h1>Hello, {user.FirstName} {user.LastName}</h1>
                                <h2>
                                    Your booking for <strong>{package.Title}</strong> has been successfully cancelled.
                                    Your refund process is initiated.
                                </h2>
                                <p>
                                    <strong>Start Date:</strong> {package.StartDate:MMMM dd, yyyy}<br>
                                    <strong>End Date:</strong> {package.EndDate:MMMM dd, yyyy}
                                </p>
                                <h3>Agent Information:</h3>
                                <p>
                                    <strong>Name:</strong> {agent.FirstName} {agent.LastName}<br>
                                    <strong>Email:</strong> {agent.Email}<br>
                                    <strong>Mobile:</strong> {agent.MobileNumber}
                                </p>
                                <p>
                                    Thank you for considering us. We hope to see you again in the future.
                                </p>
                                <h3>Best Regards, Destina</h3>
                            </body>
                        </html>
                        """;

            if (string.IsNullOrEmpty(user.Email))
            {
                throw new InvalidOperationException("User email is null or empty.");
            }

            try
            {
                EmailService.SendEmail(user.Email, subject, body);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error sending email: {ex.Message}");
                throw;
            }

            await ApplicationDbContext.SaveChangesAsync();

            await NotificationService.SaveNotificationAsync(subject, user.Id, agent.Id);

            return Ok("Your Booking Cancelled!");
        }

        //GET: api/booking/customer/1
        //get booking based on customer id
        [HttpGet("customer/{customerId}")]
        public async Task<ActionResult<IEnumerable<object>>> GetBookingsByCustomerId(int customerId)
        {
            var result = from booking in ApplicationDbContext.Bookings
                         join user in ApplicationDbContext.Users on booking.AgentId equals user.Id
                         join package in ApplicationDbContext.Packages on booking.PackageId equals package.PackageId
                         where booking.CustomerId == customerId
                         select new
                         {
                             BookingId = booking.BookingId,
                             AgentId = user.Id,
                             AgentName = user.FirstName + " " + user.LastName,
                             AgentMobileNumber = user.MobileNumber,
                             AgentEmail = user.Email,
                             BookingDate = booking.BookingDateTime,
                             NumberOfPeople = booking.NumberOfTravelers,
                             TotalAmount = booking.TotalAmount,
                             PackageLocation = package.Location,
                             PackageTitle = package.Title,
                             StartDate = package.StartDate,
                             EndDate = package.EndDate,
                             BookingStatus = booking.BookingStatus
                         };

            if (result.Any())
                return Ok(await result.ToListAsync());
            else
                return Ok("No Bookings");
        }


        //GET: api/booking/agent/1
        //get booking based on agent id
        [HttpGet("agent/{agentId}")]
        public async Task<ActionResult<IEnumerable<object>>> GetBookingsByAgentId(int agentId)
        {
            var result = from booking in ApplicationDbContext.Bookings
                         join customer in ApplicationDbContext.Users on booking.CustomerId equals customer.Id
                         join package in ApplicationDbContext.Packages on booking.PackageId equals package.PackageId
                         where booking.AgentId == agentId
                         select new
                         {
                             BookingId = booking.BookingId,
                             CustomerId = customer.Id,
                             CustomerName = customer.FirstName + " " + customer.LastName,
                             CustomerMobileNumber = customer.MobileNumber,
                             CustomerEmail = customer.Email,
                             BookingDate = booking.BookingDateTime,
                             NumberOfPeople = booking.NumberOfTravelers,
                             TotalAmount = booking.TotalAmount,
                             PackageTitle = package.Title,
                             StartDate = package.StartDate,
                             EndDate = package.EndDate,
                             BookingStatus = booking.BookingStatus
                         };

            if (result.Any())
                return Ok(await result.ToListAsync());
            else
                return Ok("No Bookings");
        }

    }
}
