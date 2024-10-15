using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace DestinaFinal.Models.Entities
{
    public class Booking
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int BookingId { get; set; }

        public DateTime BookingDateTime { get; set; }
        public int NumberOfTravelers { get; set; }
        public decimal TotalAmount { get; set; }

        public int PackageId { get; set; }
        [ForeignKey("PackageId")]
        public Package? Package { get; set; }

        public int CustomerId { get; set; }
        [ForeignKey("CustomerId")]
        public User? Customer { get; set; }

        public int AgentId { get; set; }
        [ForeignKey("AgentId")]
        public User? Agent { get; set; }

        public Payment? Payment { get; set; }

        // New field to maintain booking status
        public string BookingStatus { get; set; } = "Pending"; // Default status
    }
}
