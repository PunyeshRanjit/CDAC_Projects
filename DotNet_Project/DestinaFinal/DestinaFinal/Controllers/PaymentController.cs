using DestinaFinal.Data;
using DestinaFinal.Models;
using DestinaFinal.Models.Entities;
using DestinaFinal.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace DestinaFinal.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PaymentController : ControllerBase
    {
        public PaymentController(ApplicationDbContext applicationDbContext)
        {
            ApplicationDbContext = applicationDbContext;
        }
        public ApplicationDbContext ApplicationDbContext { get; }

        // POST: api/payment/pay
        [HttpPost("pay-for-booking/{bookingId}")]
        public async Task<IActionResult> PayForBooking(int bookingId)
        {
            var booking = await ApplicationDbContext.Bookings.Include(b => b.Payment)
                                                             .FirstOrDefaultAsync(b => b.BookingId == bookingId);

            if (booking == null)
            {
                return NotFound("Booking not found.");
            }

            // Payment processing and updating payment status
            var payment = new Payment
            {
                BookingId = booking.BookingId,
                CustomerId = booking.CustomerId,
                AgentId = booking.AgentId,
                AmountPaid = booking.TotalAmount,
                PaymentStatus = "Success", // Assume the payment was successful
                PaymentDate = DateTime.Now
            };

            ApplicationDbContext.Payments.Add(payment);

            // Update booking status to "Paid"
            booking.BookingStatus = "Paid";
            booking.Payment = payment;

            await ApplicationDbContext.SaveChangesAsync();

            return Ok("Payment successful and booking status updated to Paid.");
        }


        // GET: api/payment
        [HttpGet]
        public async Task<ActionResult<IEnumerable<PaymentDto>>> GetPayments()
        {
            var payments = await ApplicationDbContext.Payments
                .Select(p => new PaymentDto
                {
                    PaymentId = p.PaymentId,
                    AmountPaid = p.AmountPaid,
                    PaymentStatus = p.PaymentStatus,
                    PaymentDate = p.PaymentDate,
                    CustomerId = p.CustomerId,
                    AgentId = p.AgentId,
                    BookingId = p.BookingId
                })
                .ToListAsync();

            return Ok(payments);
        }


        // GET: api/payment/customer/{customerId}
        [HttpGet("customer/{customerId}")]
        public async Task<ActionResult<IEnumerable<PaymentDto>>> GetPaymentsByCustomer(int customerId)
        {
            var payments = await ApplicationDbContext.Payments
                .Where(p => p.CustomerId == customerId)
                .Select(p => new PaymentDto
                {
                    PaymentId = p.PaymentId,
                    AmountPaid = p.AmountPaid,
                    PaymentStatus = p.PaymentStatus,
                    PaymentDate = p.PaymentDate,
                    CustomerId = p.CustomerId,
                    AgentId = p.AgentId,
                    BookingId = p.BookingId
                })
                .ToListAsync();

            if (payments == null || payments.Count == 0)
            {
                return NotFound("No payments found for this customer.");
            }

            return Ok(payments);
        }


        // GET: api/payment/agent/{agentId}
        [HttpGet("agent/{agentId}")]
        public async Task<ActionResult<IEnumerable<PaymentDto>>> GetPaymentsByAgent(int agentId)
        {
            var payments = await ApplicationDbContext.Payments
                .Where(p => p.AgentId == agentId)
                .Select(p => new PaymentDto
                {
                    PaymentId = p.PaymentId,
                    AmountPaid = p.AmountPaid,
                    PaymentStatus = p.PaymentStatus,
                    PaymentDate = p.PaymentDate,
                    CustomerId = p.CustomerId,
                    AgentId = p.AgentId,
                    BookingId = p.BookingId
                })
                .ToListAsync();

            if (payments == null || payments.Count == 0)
            {
                return NotFound("No payments found for this agent.");
            }

            return Ok(payments);
        }

    }

}
