using DestinaFinal.Data;
using DestinaFinal.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace DestinaFinal.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class NotificationController : ControllerBase
    {

        public NotificationController(ApplicationDbContext applicationDbContext)
        {
            ApplicationDbContext = applicationDbContext;
        }
        private ApplicationDbContext ApplicationDbContext { get; }

        // GET: api/notification
        [HttpGet]
        public async Task<ActionResult<IEnumerable<NotificationDto>>> GetNotifications()
        {
            var notifications = await ApplicationDbContext.Notifications
                .Include(n => n.From)
                .Include(n => n.Customer)
                .Select(n => new NotificationDto
                {
                    NotificationId = n.NotificationId,
                    Subject = n.Subject,
                    FromName = $"{n.From.FirstName} {n.From.LastName}",
                    CustomerName = $"{n.Customer.FirstName} {n.Customer.LastName}"
                })
                .ToListAsync();

            return Ok(notifications);
        }
    }

}
