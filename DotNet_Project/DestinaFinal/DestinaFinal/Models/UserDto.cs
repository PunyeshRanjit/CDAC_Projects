using DestinaFinal.Models.Entities;

namespace DestinaFinal.Models
{
    public class UserDto
    {
        public int Id { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string Email { get; set; }
        public string Password { get; set; }
        public string MobileNumber { get; set; }
        public string Role { get; set; }
        public string Address { get; set; }
        public DateTime CreatedOn { get; set; }
        public AccountStatus AccountStatus { get; set; }

        public string? ResetToken { get; set; }
    }
}
