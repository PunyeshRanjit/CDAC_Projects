using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text.Json.Serialization;

namespace DestinaFinal.Models.Entities
{
    public class Package
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int PackageId { get; set; }

        public string Title { get; set; }
        public byte[] Image { get; set; }
        public string Description { get; set; }
        public string Location { get; set; }
        public DateTime StartDate { get; set; }
        public DateTime EndDate { get; set; }
        public decimal PricePerPerson { get; set; }
        public int NumberOfSeatsAvailable { get; set; }

        public int AgentId { get; set; }
        
        [ForeignKey("AgentId")]
        public User? Agent { get; set; }
        
        [JsonIgnore]
        public ICollection<Review>? Reviews { get; set; }

        [JsonIgnore]
        public ICollection<Booking>? Bookings { get; set; }
    }
}
