using DestinaFinal.Data;
using DestinaFinal.Models.Entities;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Runtime.InteropServices;

namespace DestinaFinal.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ReviewController : ControllerBase
    {
        public ReviewController(ApplicationDbContext applicationDbContext, JwtService jwtService, EmailService emailService, NotificationService notificationService)
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

        // GET: api/Review
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Review>>> GetReviews()
        {
            var reviews = await ApplicationDbContext.Reviews
                .Include(r => r.Customer)
                .Include(r => r.Package)
                .ToListAsync();

            return Ok(reviews.Select(r => new
            {
                r.ReviewId,
                r.PostTime,
                r.Content,
                CustomerId = r.Customer.Id,
                CustomerName = r.Customer.FirstName + " " + r.Customer.LastName,
                PackageId = r.Package.PackageId,
                PackageTitle = r.Package.Title
            }));
        }

        // GET: api/Review/5
        [HttpGet("{id}")]
        public async Task<ActionResult<Review>> GetReview(int id)
        {
            var review = await ApplicationDbContext.Reviews
                .Include(r => r.Customer)
                .Include(r => r.Package)
                .FirstOrDefaultAsync(r => r.ReviewId == id);

            if (review == null)
            {
                return NotFound();
            }

            return Ok(new
            {
                review.ReviewId,
                review.PostTime,
                review.Content,
                CustomerId = review.Customer.Id,
                CustomerName = review.Customer.FirstName + " " + review.Customer.LastName,
                PackageId = review.Package.PackageId,
                PackageTitle = review.Package.Title
            });
        }

        // GET: api/Review/ByPackage/5
        [HttpGet("ByPackage/{packageId}")]
        public async Task<ActionResult<IEnumerable<object>>> GetReviewsByPackage(int packageId)
        {
            var reviews = await ApplicationDbContext.Reviews
                .Include(r => r.Customer)
                .Include(r => r.Package)
                .Where(r => r.Package.PackageId == packageId)
                .Select(r => new
                {
                    r.ReviewId,
                    r.PostTime,
                    r.Content,
                    CustomerId = r.Customer.Id,
                    CustomerName = r.Customer.FirstName + " " + r.Customer.LastName,
                    PackageId = r.Package.PackageId,
                    PackageTitle = r.Package.Title
                })
                .ToListAsync();

            if (reviews == null || !reviews.Any())
            {
                return NotFound("No reviews found for this package.");
            }

            return Ok(reviews);
        }

        // POST: api/Review
        [HttpPost]
        public async Task<ActionResult<Review>> CreateReview(Review review)
        {
            // Set the PostTime to the current date and time
            review.PostTime = DateTime.UtcNow;

            ApplicationDbContext.Reviews.Add(review);
            await ApplicationDbContext.SaveChangesAsync();

            var user = await ApplicationDbContext.Users.FindAsync(review.CustomerId);
            var package = await ApplicationDbContext.Packages.FindAsync(review.PackageId);
            const string subject = "Thank you for your valuable review";
            var body = $"""
                        <html>
                            <body>
                                <h1>Hello, {user.FirstName} {user.LastName}</h1>
                                <h2>
                                    We appreciate your feedback on {package.Title}.
                                </h2>
                                <p>
                                    Your review helps us improve our services and provides valuable insights to future customers.
                                </p>
                                <p>
                                    If you have any further questions or need assistance, feel free to contact us.
                                </p>
                                <h3>Thank you!</h3>
                                <h3>Best regards,</h3>
                                <h3>Your Destina Team</h3>
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

            await NotificationService.SaveNotificationAsync(subject, user.Id, package.AgentId);

            return Ok("Review Created Successfully");
        }

        // PUT: api/Review/5
        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateReview(int id, Review review)
        {
            if (id != review.ReviewId)
            {
                return BadRequest();
            }

            // Update the PostTime to the current date and time
            review.PostTime = DateTime.UtcNow;

            ApplicationDbContext.Entry(review).State = EntityState.Modified;

            try
            {
                await ApplicationDbContext.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!ReviewExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }
            var package = await ApplicationDbContext.Packages.FindAsync(review.PackageId);

            await NotificationService.SaveNotificationAsync("Review updated", review.CustomerId, package.AgentId);

            return Ok("Review Updated Successfully");
        }

        // DELETE: api/Review/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteReview(int id)
        {
            var review = await ApplicationDbContext.Reviews.FindAsync(id);
            if (review == null)
            {
                return NotFound();
            }

            ApplicationDbContext.Reviews.Remove(review);
            await ApplicationDbContext.SaveChangesAsync();

            var package = await ApplicationDbContext.Packages.FindAsync(review.PackageId);
            await NotificationService.SaveNotificationAsync("Review Deleted", review.CustomerId, package.AgentId);

            return Ok("Review Deleted Successfully");
        }

        // GET: api/Review/agent/{agentId}
        [HttpGet("agent/{agentId}")]
        public async Task<ActionResult<IEnumerable<Review>>> GetReviewsByAgentId(int agentId)
        {
            var reviews = from review in ApplicationDbContext.Reviews
                          join user in ApplicationDbContext.Users on review.CustomerId equals user.Id
                          join package in ApplicationDbContext.Packages on review.PackageId equals package.PackageId
                          where package.AgentId == agentId
                          select new
                          {
                              ReviewId = review.ReviewId,
                              PostTime = review.PostTime,
                              Content = review.Content,
                              CustomerName = user.FirstName + " " + user.LastName,
                              PackageTitle = package.Title,
                              PackageLocation = package.Location,
                              PackageStartDate = package.StartDate,
                              PackageEndDate = package.EndDate
                          };

            if (reviews.Any())
                return Ok(await reviews.ToListAsync());
            else
                return Ok("No Reviews found for this agent.");
        }


        private bool ReviewExists(int id)
        {
            return ApplicationDbContext.Reviews.Any(e => e.ReviewId == id);
        }
    }
}
