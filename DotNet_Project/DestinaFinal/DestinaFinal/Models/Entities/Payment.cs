using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace DestinaFinal.Models.Entities
{
    public class Payment
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int PaymentId { get; set; }

        public decimal AmountPaid { get; set; }
        public string PaymentStatus { get; set; } // Status of the payment (e.g., Success, Failed, Pending)
        public DateTime PaymentDate { get; set; } // Date and time of the payment

        public int CustomerId { get; set; }
        [ForeignKey("CustomerId")]
        public User? Customer { get; set; }

        public int AgentId { get; set; }
        [ForeignKey("AgentId")]
        public User? Agent { get; set; }

        public int BookingId { get; set; }
        [ForeignKey("BookingId")]
        public Booking? Booking { get; set; }
    }
}
