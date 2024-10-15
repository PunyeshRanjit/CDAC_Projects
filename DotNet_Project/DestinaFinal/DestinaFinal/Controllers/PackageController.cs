using DestinaFinal.Data;
using DestinaFinal.Models;
using DestinaFinal.Models.Entities;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Linq;

namespace DestinaFinal.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PackageController : ControllerBase
    {
        public PackageController(ApplicationDbContext applicationDbContext, JwtService jwtService, EmailService emailService)
        {
            ApplicationDbContext = applicationDbContext;
            JwtService = jwtService;
            EmailService = emailService;
        }

        public ApplicationDbContext ApplicationDbContext { get; }
        public JwtService JwtService { get; }
        public EmailService EmailService { get; }

        //GET: api/package
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Package>>> GetAllPackages()
        {
            var result = await (from package in ApplicationDbContext.Packages
                                join user in ApplicationDbContext.Users on package.AgentId equals user.Id
                                where package.NumberOfSeatsAvailable > 0
                                select new PackageDto
                                {
                                    PackageId = package.PackageId,
                                    Title = package.Title,
                                    Image = package.Image,
                                    Description = package.Description,
                                    Location = package.Location,
                                    StartDate = package.StartDate,
                                    EndDate = package.EndDate,
                                    PricePerPerson = package.PricePerPerson,
                                    NumberOfSeatsAvailable = package.NumberOfSeatsAvailable,
                                    AgentId = package.AgentId,
                                    AgentName = user.FirstName + " " + user.LastName
                                }).ToListAsync();

            if (result.Count > 0)
                return Ok(result);
            else
                return Ok("No Packages");
        }

        //GET: api/package/1
        [HttpGet("{id}")]
        public async Task<ActionResult<Package>> GetPackageById(int id)
        {
            var package = await (from p in ApplicationDbContext.Packages
                                 join u in ApplicationDbContext.Users on p.AgentId equals u.Id
                                 where p.PackageId == id
                                 select new PackageDto
                                 {
                                     PackageId = p.PackageId,
                                     Title = p.Title,
                                     Image = p.Image, 
                                     Description = p.Description,
                                     Location = p.Location,
                                     StartDate = p.StartDate,
                                     EndDate = p.EndDate,
                                     PricePerPerson = p.PricePerPerson,
                                     NumberOfSeatsAvailable = p.NumberOfSeatsAvailable,
                                     AgentId = p.AgentId,
                                     AgentName = u.FirstName + " " + u.LastName
                                 }).FirstOrDefaultAsync();

            if (package == null)
            {
                return NotFound();
            }

            return Ok(package);
        }

        //GET: api/package/string
        [HttpGet("search")]
        public async Task<ActionResult<IEnumerable<Package>>> SearchPackages([FromQuery] string location)
        {
            var result = await (from p in ApplicationDbContext.Packages
                                join u in ApplicationDbContext.Users on p.AgentId equals u.Id
                                where p.Location.Contains(location)
                                select new PackageDto
                                {
                                    PackageId = p.PackageId,
                                    Title = p.Title,
                                    Image = p.Image, // Handle image encoding if necessary
                                    Description = p.Description,
                                    Location = p.Location,
                                    StartDate = p.StartDate,
                                    EndDate = p.EndDate,
                                    PricePerPerson = p.PricePerPerson,
                                    NumberOfSeatsAvailable = p.NumberOfSeatsAvailable,
                                    AgentId = p.AgentId,
                                    AgentName = u.FirstName + " " + u.LastName
                                }).ToListAsync();

            if (result.Count > 0)
                return Ok(result);
            else
                return Ok("No Packages Found");
        }

        //POST: api/package
        [HttpPost]
        [HttpPost]
        public async Task<ActionResult<Package>> CreatePackage([FromForm] string title, [FromForm] string description, [FromForm] string location, [FromForm] DateTime startDate, [FromForm] DateTime endDate, [FromForm] decimal pricePerPerson, [FromForm] int numberOfSeatsAvailable, [FromForm] int agentId, IFormFile imageFile)
        {
            if (imageFile == null || imageFile.Length == 0)
            {
                return BadRequest("No image file uploaded.");
            }

            // Convert the image file to a byte array
            byte[] imageData;
            using (var memoryStream = new MemoryStream())
            {
                await imageFile.CopyToAsync(memoryStream);
                imageData = memoryStream.ToArray();
            }

            // Create the Package object
            var package = new Package
            {
                Title = title,
                Image = imageData,
                Description = description,
                Location = location,
                StartDate = startDate,
                EndDate = endDate,
                PricePerPerson = pricePerPerson,
                NumberOfSeatsAvailable = numberOfSeatsAvailable,
                AgentId = agentId
            };

            // Add the package to the database
            ApplicationDbContext.Packages.Add(package);
            await ApplicationDbContext.SaveChangesAsync();

            return Ok("Package created successfully");
        }

        //PUT: api/package/1
        [HttpPut("{id}")]
        public async Task<IActionResult> UpdatePackage(int id, [FromForm] string title, [FromForm] string description, [FromForm] string location, [FromForm] DateTime startDate, [FromForm] DateTime endDate, [FromForm] decimal pricePerPerson, [FromForm] int numberOfSeatsAvailable, [FromForm] int agentId, [FromForm] IFormFile imageFile)
        {
            if (id <= 0)
            {
                return BadRequest("Invalid package ID.");
            }

            var existingPackage = await ApplicationDbContext.Packages.FindAsync(id);
            if (existingPackage == null)
            {
                return NotFound("Package not found.");
            }

            // Update package details
            existingPackage.Title = title;
            existingPackage.Description = description;
            existingPackage.Location = location;
            existingPackage.StartDate = startDate;
            existingPackage.EndDate = endDate;
            existingPackage.PricePerPerson = pricePerPerson;
            existingPackage.NumberOfSeatsAvailable = numberOfSeatsAvailable;
            existingPackage.AgentId = agentId;

            // Handle image update if a new image file is provided
            if (imageFile != null && imageFile.Length > 0)
            {
                using (var memoryStream = new MemoryStream())
                {
                    await imageFile.CopyToAsync(memoryStream);
                    existingPackage.Image = memoryStream.ToArray();
                }
            }

            ApplicationDbContext.Entry(existingPackage).State = EntityState.Modified;

            try
            {
                await ApplicationDbContext.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                var packageExists = ApplicationDbContext.Packages.Any(e => e.PackageId == id);
                if (!packageExists)
                {
                    return NotFound("Package not found.");
                }
                throw;
            }

            return Ok("Package Updated Successfully!");
        }

        //DELETE: api/package/1
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeletePackage(int id)
        {
            var package = await ApplicationDbContext.Packages.FindAsync(id);
            if (package == null)
            {
                return NotFound();
            }

            ApplicationDbContext.Packages.Remove(package);
            await ApplicationDbContext.SaveChangesAsync();

            return Ok("Package Deleted!");
        }

        //GET: api/agent/1
        //Get agent id specific packages
        [HttpGet("agent/{agentId}")]
        public async Task<ActionResult<IEnumerable<Package>>> GetPackagesByAgent(int agentId)
        {
            var result = await (from p in ApplicationDbContext.Packages
                                join u in ApplicationDbContext.Users on p.AgentId equals u.Id
                                where p.AgentId == agentId
                                select new PackageDto
                                {
                                    PackageId = p.PackageId,
                                    Title = p.Title,
                                    Image = p.Image, // Handle image encoding if necessary
                                    Description = p.Description,
                                    Location = p.Location,
                                    StartDate = p.StartDate,
                                    EndDate = p.EndDate,
                                    PricePerPerson = p.PricePerPerson,
                                    NumberOfSeatsAvailable = p.NumberOfSeatsAvailable,
                                    AgentId = p.AgentId,
                                    AgentName = u.FirstName + " " + u.LastName
                                }).ToListAsync();

            if (result.Count > 0)
                return Ok(result);
            else
                return Ok("No Packages Found");
        }

    }
}
