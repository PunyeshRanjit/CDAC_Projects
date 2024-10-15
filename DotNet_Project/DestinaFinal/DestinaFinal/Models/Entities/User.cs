using Microsoft.Identity.Client;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text.Json.Serialization;

namespace DestinaFinal.Models.Entities
{
    public class User
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }

        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string Email { get; set; }
        public string Password { get; set; }
        public string MobileNumber { get; set; }
        public string Role { get; set; }
        public string Address { get; set; }
        public DateTime CreatedOn { get; set; }
        public AccountStatus AccountStatus { get; set; } = AccountStatus.UNAPROOVED;

        public string? ResetToken { get; set; } 

        // Navigation properties
        [JsonIgnore]
        public ICollection<Package>? Packages { get; set; }
        [JsonIgnore]
        public ICollection<Review>? ReviewsAsCustomer { get; set; }
        [JsonIgnore]
        public ICollection<Booking>? BookingsAsCustomer { get; set; }
        [JsonIgnore]
        public ICollection<Booking>? BookingsAsAgent { get; set; }
        [JsonIgnore]
        public ICollection<Payment>? PaymentsAsCustomer { get; set; }
        [JsonIgnore]
        public ICollection<Payment>? PaymentsAsAgent { get; set; }
        [JsonIgnore]
        public ICollection<Notification>? SentNotifications { get; set; }
        [JsonIgnore]
        public ICollection<Notification>? ReceivedNotifications { get; set; }
    }



    public enum AccountStatus
    {
        UNAPROOVED, ACTIVE, BLOCKED
    }

}
