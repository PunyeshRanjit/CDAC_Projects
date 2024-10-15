using DestinaFinal.Models.Entities;
using Microsoft.EntityFrameworkCore;

namespace DestinaFinal.Data
{
    public class ApplicationDbContext : DbContext
    {

        public DbSet<User> Users { get; set; }
        public DbSet<Package> Packages { get; set; }
        public DbSet<Review> Reviews { get; set; }
        public DbSet<Booking> Bookings { get; set; }
        public DbSet<Payment> Payments { get; set; }
        public DbSet<Notification> Notifications { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            // User - Package (One-to-Many)
            // An Agent (User) can create multiple Packages, but each Package is associated with a single Agent.
            modelBuilder.Entity<Package>()
                .HasOne(p => p.Agent)
                .WithMany(u => u.Packages)
                .HasForeignKey(p => p.AgentId)
                .OnDelete(DeleteBehavior.Restrict);

            // User - Review (One-to-Many) (Customer)
            // A Customer (User) can write multiple Reviews, but each Review is associated with a single Customer.
            modelBuilder.Entity<Review>()
                .HasOne(r => r.Customer)
                .WithMany(u => u.ReviewsAsCustomer)
                .HasForeignKey(r => r.CustomerId)
                .OnDelete(DeleteBehavior.Restrict);

            // Ensure that no extra UserId is added by removing unnecessary navigations.
            modelBuilder.Entity<Review>()
                .Ignore(r => r.Customer)
                .HasOne(r => r.Customer)
                .WithMany(u => u.ReviewsAsCustomer)
                .HasForeignKey(r => r.CustomerId)
                .OnDelete(DeleteBehavior.Restrict);

            // Package - Review (One-to-Many)
            // A Package can have multiple Reviews, but each Review is associated with a single Package.
            modelBuilder.Entity<Review>()
                .HasOne(r => r.Package)
                .WithMany(p => p.Reviews)
                .HasForeignKey(r => r.PackageId)
                .OnDelete(DeleteBehavior.Cascade);

            // User - Booking (One-to-Many) (Customer)
            // A Customer (User) can make multiple Bookings, but each Booking is associated with a single Customer.
            modelBuilder.Entity<Booking>()
                .HasOne(b => b.Customer)
                .WithMany(u => u.BookingsAsCustomer)
                .HasForeignKey(b => b.CustomerId)
                .OnDelete(DeleteBehavior.Restrict);

            // User - Booking (One-to-Many) (Agent)
            // An Agent (User) can handle multiple Bookings, but each Booking is associated with a single Agent.
            modelBuilder.Entity<Booking>()
                .HasOne(b => b.Agent)
                .WithMany(u => u.BookingsAsAgent)
                .HasForeignKey(b => b.AgentId)
                .OnDelete(DeleteBehavior.Restrict);

            // Package - Booking (One-to-Many)
            // A Package can have multiple Bookings, but each Booking is associated with a single Package.
            modelBuilder.Entity<Booking>()
                .HasOne(b => b.Package)
                .WithMany(p => p.Bookings)
                .HasForeignKey(b => b.PackageId)
                .OnDelete(DeleteBehavior.Cascade);

            // Booking - Payment (One-to-One)
            // A Booking can have one Payment, and each Payment is associated with a single Booking.
            modelBuilder.Entity<Payment>()
                .HasOne(p => p.Booking)
                .WithOne(b => b.Payment)
                .HasForeignKey<Payment>(p => p.BookingId)
                .OnDelete(DeleteBehavior.Cascade);

            // User - Payment (One-to-Many) (Customer)
            // A Customer (User) can make multiple Payments, but each Payment is associated with a single Customer.
            modelBuilder.Entity<Payment>()
                .HasOne(p => p.Customer)
                .WithMany(u => u.PaymentsAsCustomer)
                .HasForeignKey(p => p.CustomerId)
                .OnDelete(DeleteBehavior.Restrict);

            // User - Payment (One-to-Many) (Agent)
            // An Agent (User) can receive multiple Payments, but each Payment is associated with a single Agent.
            modelBuilder.Entity<Payment>()
                .HasOne(p => p.Agent)
                .WithMany(u => u.PaymentsAsAgent)
                .HasForeignKey(p => p.AgentId)
                .OnDelete(DeleteBehavior.Restrict);

            // Notification - User (One-to-Many) (From)
            // A User can send multiple Notifications, but each Notification is associated with a single User (From).
            modelBuilder.Entity<Notification>()
                .HasOne(n => n.From)
                .WithMany(u => u.SentNotifications)
                .HasForeignKey(n => n.FromId)
                .OnDelete(DeleteBehavior.Restrict);

            // Notification - User (One-to-Many) (Customer)
            // A User can receive multiple Notifications, but each Notification is associated with a single User (Customer).
            modelBuilder.Entity<Notification>()
                .HasOne(n => n.Customer)
                .WithMany(u => u.ReceivedNotifications)
                .HasForeignKey(n => n.CustomerId)
                .OnDelete(DeleteBehavior.Restrict);

            // Seed initial data for the User table
            modelBuilder.Entity<User>().HasData(new User()
            {
                Id = 1,
                FirstName = "Sahil",
                LastName = "Bagde",
                Email = "sahil@gmail.com",
                MobileNumber = "1234567890",
                Password = "admin123",
                AccountStatus = AccountStatus.ACTIVE,
                Role = "ADMIN",
                Address = "Nagpur Maharashtra",
                CreatedOn = new DateTime(2024, 08, 06, 22, 32, 23)
            });
        }





        // Constructor
        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options) : base(options)
        {
        }

        protected override void ConfigureConventions(ModelConfigurationBuilder configurationBuilder)
        {
            configurationBuilder.Properties<AccountStatus>().HaveConversion<string>();
        }

        
    }
    }
