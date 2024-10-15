using DestinaFinal.Data;
using DestinaFinal.Models.Entities;

namespace DestinaFinal
{
    public class NotificationService
    {
        private readonly ApplicationDbContext _context;

        public NotificationService(ApplicationDbContext context)
        {
            _context = context;
        }

        public async Task SaveNotificationAsync(string subject, int customerId, int fromId = 1)
        {
            var notification = new Notification
            {
                Subject = subject,
                FromId = fromId,          
                CustomerId = customerId   
            };

            _context.Notifications.Add(notification);
            await _context.SaveChangesAsync();
        }
    }
}
