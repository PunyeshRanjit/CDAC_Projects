namespace DestinaFinal.Models
{
    public class PaymentDto
    {
        public int PaymentId { get; set; }
        public decimal AmountPaid { get; set; }
        public string PaymentStatus { get; set; }
        public DateTime PaymentDate { get; set; }
        public int CustomerId { get; set; }
        public int AgentId { get; set; }
        public int BookingId { get; set; }
    }

}
