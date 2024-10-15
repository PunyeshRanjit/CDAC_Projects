using System.Net.Mail;
using System.Net;

namespace DestinaFinal
{
    public class EmailService
    {
        private readonly IConfiguration _configuration;

        public EmailService(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        public void SendEmail(string toEmail, string subject, string body)
        {
            // Fetching email configuration settings
            var fromEmail = _configuration.GetSection("Constants:FromEmail").Value ?? throw new InvalidOperationException("FromEmail is not configured.");
            var fromEmailPassword = _configuration.GetSection("Constants:EmailAccountPassword").Value ?? throw new InvalidOperationException("EmailAccountPassword is not configured.");
           
            if (fromEmail == null)
                throw new InvalidOperationException("FromEmail is null.");
            if (string.IsNullOrEmpty(toEmail))
                throw new ArgumentException("Recipient email cannot be null or empty", nameof(toEmail));

            if (string.IsNullOrEmpty(subject))
                throw new ArgumentException("Email subject cannot be null or empty", nameof(subject));

            if (string.IsNullOrEmpty(body))
                throw new ArgumentException("Email body cannot be null or empty", nameof(body));

            var message = new MailMessage()
            {
                From = new MailAddress(fromEmail),
                Subject = subject,
                Body = body,
                IsBodyHtml = true // Consider whether the body is HTML or plain text
            };

            message.To.Add(toEmail);

            using (var smtpClient = new SmtpClient("smtp.gmail.com"))
            {
                smtpClient.Port = 587;
                smtpClient.Credentials = new NetworkCredential(fromEmail, fromEmailPassword);
                smtpClient.EnableSsl = true;

                try
                {
                    smtpClient.Send(message);
                }
                catch (SmtpException smtpEx)
                {
                    // Log or handle the SMTP exception
                    throw new InvalidOperationException("Failed to send email due to SMTP error.", smtpEx);
                }
                catch (Exception ex)
                {
                    // Handle other potential exceptions
                    throw new InvalidOperationException("An error occurred while sending the email.", ex);
                }
            }
        }
    }
}
