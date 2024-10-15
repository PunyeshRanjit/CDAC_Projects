namespace DestinaFinal.Models
{
    public class PackageDto
    {
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
        public string AgentName { get; set; }
    }
}
