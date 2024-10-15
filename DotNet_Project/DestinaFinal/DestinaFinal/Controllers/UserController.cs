using DestinaFinal.Data;
using DestinaFinal.Models;
using DestinaFinal.Models.Entities;
using DestinaFinal.Models.LoginModel;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.OpenApi.Extensions;

namespace DestinaFinal.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UserController : ControllerBase
    {
        public UserController(ApplicationDbContext applicationDbContext, EmailService emailService, JwtService jwtService, NotificationService notificationService)
        {
            ApplicationDbContext = applicationDbContext;
            EmailService = emailService;
            JwtService = jwtService;
            NotificationService = notificationService;
        }

        public ApplicationDbContext ApplicationDbContext { get; }
        public EmailService EmailService { get; }

        public JwtService JwtService { get; }

        public NotificationService NotificationService { get; }

        [HttpPost("Register")]
        public async Task<IActionResult> Register(User user)
        {
            if (ApplicationDbContext.Users.Any(u => u.Email == user.Email))
            {
                return BadRequest("User with this email already exists.");
            }

            user.CreatedOn = DateTime.Now;
            user.Role = user.Role.ToUpper();
            user.Password = BCrypt.Net.BCrypt.HashPassword(user.Password);

            // Set account status based on role
            if (user.Role == "CUSTOMER")
            {
                user.AccountStatus = AccountStatus.ACTIVE;
            }
            else
            {
                user.AccountStatus = AccountStatus.UNAPROOVED;
            }

            ApplicationDbContext.Add(user);
            await ApplicationDbContext.SaveChangesAsync();

            // Define email subject and body
            const string subject = "Account Created";
            var message = user.Role == "CUSTOMER"
                ? "Your account has been activated. You can now log in with your email and password."
                : "We have sent an approval request to the admin. Once your request is approved, you will receive a confirmation email, and you will be able to log in to your account.";

            var body = $"""
                        <html>
                            <body>
                                <h1>Hello, {user.FirstName} {user.LastName}</h1>
                                <h2>Your account has been successfully created.</h2>
                                <p>{message}</p>
                                <h3>Thank you for your patience.</h3>
                                <p>If you have any questions or need assistance, feel free to contact our support team.</p>
                                <h4>Best Regards, Destina</h4>
                            </body>
                        </html>
                        """;

            if (string.IsNullOrEmpty(user.Email))
            {
                throw new InvalidOperationException("User email is null or empty.");
            }

            try
            {
                EmailService.SendEmail(user.Email, subject, body);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error sending email: {ex.Message}");
                throw;
            }

            // Save notification
            await NotificationService.SaveNotificationAsync(subject, user.Id, 1);



            return Ok("Thank you for registering. Your account has been sent for approval. Once it is approved, you will receive an email.");
        }


        [HttpPost("login")]
        public IActionResult Login([FromBody] LoginDto loginDto)
        {
            if (ApplicationDbContext.Users.Any(u => u.Email.Equals(loginDto.Email) && u.Role.Equals(loginDto.Role.ToUpper())))
            {
                var user = ApplicationDbContext.Users.SingleOrDefault(u => u.Email == loginDto.Email && u.Role == loginDto.Role.ToUpper());

                if (user == null)
                {
                    return Unauthorized("Invalid email, role, or password.");
                }
                else 
                {
                    if (user.AccountStatus == AccountStatus.UNAPROOVED)
                    {
                        return Ok("Account is unapproved");
                    }
                    else if (user.AccountStatus == AccountStatus.BLOCKED)
                    {
                        return Ok("Account is Blocked");
                    }
                    else
                    {
                        if (user.Role != "ADMIN")
                        {
                            bool isPasswordValid = BCrypt.Net.BCrypt.Verify(loginDto.Password, user.Password);

                            if (!isPasswordValid)
                            {
                                return Unauthorized("Invalid email or password.");
                            }

                        }

                        return Ok(new
                        {
                            Message = "Login successful",
                            UserId = user.Id,
                            Token = JwtService.GenerateToken(user)
                        });
                    }
                    
                }
            }
            return Ok("not found");
            
        }

        // GET: api/User
        [HttpGet]
        public async Task<IActionResult> GetUsers()
        {
            var users = await ApplicationDbContext.Users
                .Where(user => (user.Role == "AGENT" || user.Role == "CUSTOMER") && (user.AccountStatus == AccountStatus.ACTIVE || user.AccountStatus == AccountStatus.BLOCKED))
                .Select(user => new UserDto
                {
                    Id = user.Id,
                    FirstName = user.FirstName,
                    LastName = user.LastName,
                    Email = user.Email,
                    Password = user.Password,
                    MobileNumber = user.MobileNumber,
                    Address = user.Address,
                    Role = user.Role,
                    AccountStatus = user.AccountStatus,
                    CreatedOn = user.CreatedOn
                })
                .ToListAsync();

            return Ok(users);
        }


        // GET: api/User/Unapproved
        [HttpGet("Unapproved")]
        public async Task<IActionResult> GetUnapprovedUsers()
        {
            var unapprovedUsers = await ApplicationDbContext.Users
                .Where(user => user.AccountStatus == AccountStatus.UNAPROOVED)
                .Select(user => new UserDto
                {
                    Id = user.Id,
                    FirstName = user.FirstName,
                    LastName = user.LastName,
                    Email = user.Email,
                    Password = user.Password,
                    MobileNumber = user.MobileNumber,
                    Address = user.Address,
                    Role = user.Role,
                    AccountStatus = user.AccountStatus,
                    CreatedOn = user.CreatedOn
                })
                .ToListAsync();

            if (!unapprovedUsers.Any())
            {
                return NotFound("No unapproved users found.");
            }

            return Ok(unapprovedUsers);
        }


        // GET: api/User/5
        [HttpGet("{id}")]
        public async Task<IActionResult> GetUser(int id)
        {
            var user = await ApplicationDbContext.Users.FindAsync(id);
            if (user == null)
            {
                return NotFound();
            }
            var userDto = new UserDto
            {
                Id = user.Id,
                FirstName = user.FirstName,
                LastName = user.LastName,
                Email = user.Email,
                Password = user.Password,
                MobileNumber = user.MobileNumber,
                Address = user.Address,
                Role = user.Role,
                AccountStatus = user.AccountStatus,
                CreatedOn = user.CreatedOn
            };

            return Ok(user);
        }

        // PUT: api/User/5
        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateUser(int id, User user)
        {
            if (id != user.Id)
            {
                return BadRequest();
            }

            var existingUser = await ApplicationDbContext.Users.FindAsync(id);
            if (existingUser == null)
            {
                return NotFound();
            }

            existingUser.FirstName = user.FirstName;
            existingUser.LastName = user.LastName;
            existingUser.Email = user.Email;
            existingUser.MobileNumber = user.MobileNumber;
            existingUser.Role = user.Role;

            ApplicationDbContext.Entry(existingUser).State = EntityState.Modified;

            try
            {
                await ApplicationDbContext.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!ApplicationDbContext.Users.Any(e => e.Id == id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return NoContent();
        }

        // DELETE: api/User/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteUser(int id)
        {
            var user = await ApplicationDbContext.Users.FindAsync(id);
            if (user == null)
            {
                return NotFound();
            }

            ApplicationDbContext.Users.Remove(user);
            await ApplicationDbContext.SaveChangesAsync();

            await NotificationService.SaveNotificationAsync("Account Deleted", id, 1);
            return NoContent();
        }

        //PUT: api/User/approve/4
        //ACTIVE account
        [HttpPut("approve/{id}")]
        public async Task<IActionResult> ApproveUser(int id)
        {
            var user = await ApplicationDbContext.Users.FindAsync(id);
            if (user == null)
            {
                return NotFound();
            }

            user.AccountStatus = AccountStatus.ACTIVE;
            await ApplicationDbContext.SaveChangesAsync();

            const string subject = "Account Activated";
            var body = $"""
                <html>
                    <body>
                        <h1>Hello, {user.FirstName} {user.LastName}</h1>
                        <h2>
                            Your account has been activated now you can 
                            login in to your account with your email and password.
                        </h2>
                        <h3>Thanks</h3>
                    </body>
                </html>
            """;
            if (string.IsNullOrEmpty(user.Email))
            {
                throw new InvalidOperationException("User email is null or empty.");
            }

            try
            {
                EmailService.SendEmail(user.Email, subject, body);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error sending email: {ex.Message}");
                throw;
            }
            await NotificationService.SaveNotificationAsync(subject, id, 1);
            return Ok("Account is Active Now");
        }

        //PUT: api/User/approve/4
        //BLOCK account
        [HttpPut("block/{id}")]
        public async Task<IActionResult> BlockUser(int id)
        {
            var user = await ApplicationDbContext.Users.FindAsync(id);
            if (user == null)
            {
                return NotFound();
            }

            user.AccountStatus = AccountStatus.BLOCKED;
            await ApplicationDbContext.SaveChangesAsync();

            const string subject = "Account Blocked!";
            var body = $"""
                <html>
                    <body>
                        <h1>Hello, {user.FirstName} {user.LastName}</h1>
                        <h2>
                            Your account has been blocked you won't be
                            able to login in to your account.
                        </h2>
                        <h3>Thanks</h3>
                    </body>
                </html>
            """;
            if (string.IsNullOrEmpty(user.Email))
            {
                throw new InvalidOperationException("User email is null or empty.");
            }

            try
            {
                EmailService.SendEmail(user.Email, subject, body);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error sending email: {ex.Message}");
                throw;
            }

            await NotificationService.SaveNotificationAsync(subject, id, 1);

            return Ok("Account is Blocked!");
        }


        [HttpPost("forget-password")]
        public async Task<IActionResult> ForgetPassword([FromBody] EmailDto emailDto)
        {
            var user = await ApplicationDbContext.Users.FirstOrDefaultAsync(u => u.Email == emailDto.Email);
            if (user == null)
            {
                return NotFound("User with this email does not exist.");
            }


            var resetToken = Guid.NewGuid().ToString();

            // Save the reset token to the user entity
            user.ResetToken = resetToken;

            await ApplicationDbContext.SaveChangesAsync();

            // Send an email with the reset link
            //var resetLink = Url.Action("ResetPassword", "User", new { token = resetToken }, Request.Scheme);
            const string subject = "Password Reset Request";
            var body = $"""
                <html>
                    <body>
                        <h1>Hello, {user.FirstName} {user.LastName}</h1>
                        <p>You requested to reset your password. Click the link below to reset it:</p>
                        <p>this is your token to rest the password: {resetToken}<p>
                        <p>This link will expire in 1 hour.</p>
                        <h4>Best Regards, Destina</h4>
                    </body>
                </html>
            """;

            try
            {
                EmailService.SendEmail(user.Email, subject, body);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error sending email: {ex.Message}");
                throw;
            }

            return Ok("Password reset token has been sent to your email.");
        }



        [HttpPost("reset-password")]
        public async Task<IActionResult> ResetPassword([FromBody] ResetPasswordDto resetPasswordDto)
        {
            var user = await ApplicationDbContext.Users.FirstOrDefaultAsync(u => u.ResetToken == resetPasswordDto.Token);

            if (user == null)
            {
                return BadRequest("Invalid password reset token.");
            }

            user.Password = BCrypt.Net.BCrypt.HashPassword(resetPasswordDto.NewPassword);
            user.ResetToken = null; // Clear the reset token

            await ApplicationDbContext.SaveChangesAsync();

            return Ok("Password has been successfully reset.");
        }


    }

}

