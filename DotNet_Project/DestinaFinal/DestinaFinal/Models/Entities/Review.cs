using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace DestinaFinal.Models.Entities
{
    public class Review
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int ReviewId { get; set; }

        public DateTime PostTime { get; set; }
        public string? Content { get; set; }

        public int CustomerId { get; set; }
        [ForeignKey("CustomerId")]
        public User? Customer { get; set; }

        public int PackageId { get; set; }
        [ForeignKey("PackageId")]
        public Package? Package { get; set; }
    }

}
