package com.destina.Service;



import com.destina.model.Booking;
import com.destina.model.Package;
import com.destina.model.User;
import com.destina.Dto.BookingDto;
import com.destina.Repository.BookingRepository;
import com.destina.Repository.PackageRepository;
import com.destina.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private PackageService packageService; 

    public Booking createBooking(BookingDto bookingDto) throws Exception {
        Optional<Package> optionalPackage = packageRepository.findById(bookingDto.getPackageId());
        if (!optionalPackage.isPresent()) {
            throw new IllegalArgumentException("Package not found.");
        }
        Package pkg = optionalPackage.get();

        // Calculate total amount
        bookingDto.setTotalAmount(pkg.getPricePerPerson().multiply(BigDecimal.valueOf(bookingDto.getNumberOfTravelers())));

        // Decrease available seats
        pkg.setNumberOfSeatsAvailable(pkg.getNumberOfSeatsAvailable() - bookingDto.getNumberOfTravelers());
        if (pkg.getNumberOfSeatsAvailable() < 0) {
            throw new IllegalArgumentException("Not enough seats available.");
        }
        packageRepository.save(pkg);

        
        
        // Send email
        Optional<User> userOpt = userRepository.findById(bookingDto.getCustomerId());
        Optional<User> agentOpt = userRepository.findById(bookingDto.getAgentId());

        if (userOpt.isPresent() && agentOpt.isPresent()) {
            User user = userOpt.get();
            User agent = agentOpt.get();

            String subject = "Booking Successful";
            String body = String.format(
                    "<html><body>" +
                    "<h1>Hello, %s %s</h1>" +
                    "<h2>Your booking for <strong>%s</strong> has been successfully confirmed!</h2>" +
                    "<p><strong>Start Date:</strong> %s<br><strong>End Date:</strong> %s<br>" +
                    "<strong>Total Amount:</strong> $%s</p>" +
                    "<h3>Agent Information:</h3>" +
                    "<p><strong>Name:</strong> %s %s<br><strong>Email:</strong> %s<br><strong>Mobile:</strong> %s</p>" +
                    "<p>If you have any questions or need further assistance, feel free to contact us.</p>" +
                    "<h3>Thank you for choosing Destina!</h3></body></html>",
                    user.getFirstName(), user.getLastName(), pkg.getTitle(),
                    pkg.getStartDate(), pkg.getEndDate(), bookingDto.getTotalAmount(),
                    agent.getFirstName(), agent.getLastName(), agent.getEmail(), agent.getMobileNumber()
                    
                    
            );
            
           

            emailService.sendEmail(user.getEmail(), subject, body);
            notificationService.saveNotification("Booking Successful", user.getId(), agent.getId());
        }
        
        Booking booking = new Booking();
        booking.setBookingDateTime(bookingDto.getBookingDateTime());
        booking.setNumberOfTravelers(bookingDto.getNumberOfTravelers());
        booking.setTotalAmount(pkg.getPricePerPerson().multiply(BigDecimal.valueOf(bookingDto.getNumberOfTravelers())));
        booking.setaPackage(pkg);
        booking.setCustomer(userOpt.get());
        booking.setAgent(agentOpt.get());
        
     // Set booking status from DTO
        booking.setBookingStatus(bookingDto.getBookingStatus() != null ? bookingDto.getBookingStatus() : "Pending");
        
        // Save booking
        //BookingDto savedBooking = bookingRepository.save(bookingDto);
        Booking savedBooking = bookingRepository.save(booking);

        return savedBooking;
    }

    public void cancelBooking(Long id) throws Exception {
        Optional<Booking> optionalBooking = bookingRepository.findById(id);
        if (!optionalBooking.isPresent()) {
            throw new IllegalArgumentException("Booking not found.");
        }

        Booking booking = optionalBooking.get();
        Optional<User> userOpt = userRepository.findById(booking.getCustomer().getId());
        Optional<User> agentOpt = userRepository.findById(booking.getAgent().getId());
        Optional<Package> pkgOpt = packageRepository.findById(booking.getaPackage().getPackageId());

        if (userOpt.isPresent() && agentOpt.isPresent() && pkgOpt.isPresent()) {
            User user = userOpt.get();
            User agent = agentOpt.get();
            Package pkg = pkgOpt.get();

            // Increase available seats
            pkg.setNumberOfSeatsAvailable(pkg.getNumberOfSeatsAvailable() + booking.getNumberOfTravelers());
            packageRepository.save(pkg);

            String subject = "Booking Cancelled";
            String body = String.format(
                    "<html><body>" +
                    "<h1>Hello, %s %s</h1>" +
                    "<h2>Your booking for <strong>%s</strong> has been successfully cancelled.</h2>" +
                    "<p><strong>Start Date:</strong> %s<br><strong>End Date:</strong> %s</p>" +
                    "<h3>Agent Information:</h3>" +
                    "<p><strong>Name:</strong> %s %s<br><strong>Email:</strong> %s<br><strong>Mobile:</strong> %s</p>" +
                    "<p>Thank you for considering us. We hope to see you again in the future.</p>" +
                    "<h3>Best Regards, Destina</h3></body></html>",
                    user.getFirstName(), user.getLastName(), pkg.getTitle(),
                    pkg.getStartDate(), pkg.getEndDate(),
                    agent.getFirstName(), agent.getLastName(), agent.getEmail(), agent.getMobileNumber()
            );

            emailService.sendEmail(user.getEmail(), subject, body);
            notificationService.saveNotification("Booking Cancelled", user.getId(), agent.getId());
         
            // Change booking status to "Cancelled" instead of deleting
            booking.setBookingStatus("Cancelled");
            
            bookingRepository.save(booking);
            //bookingRepository.delete(booking);
        }
    }

    public List<BookingDto> getBookingsByCustomerId(Long customerId) {
        return bookingRepository.findAll().stream()
            .filter(booking -> booking.getCustomer().getId().equals(customerId))
            .map(booking -> {
                Package pkg = packageRepository.findById(booking.getaPackage().getPackageId()).orElse(null);
                User agent = userRepository.findById(booking.getAgent().getId()).orElse(null);
                return new BookingDto(
                    booking.getBookingId(),
                    booking.getBookingDateTime(),
                    booking.getNumberOfTravelers(),
                    booking.getTotalAmount(),
                    booking.getaPackage().getPackageId(),
                    booking.getCustomer().getId(),
                    booking.getAgent().getId(),
                    pkg != null ? pkg.getTitle() : null,
                    pkg != null ? pkg.getStartDate() : null,
                    pkg != null ? pkg.getEndDate() : null,
                    pkg != null ? pkg.getLocation() : null,
                    agent != null ? agent.getFirstName() + " " + agent.getLastName() : null,
                    agent != null ? agent.getEmail() : null,
                    agent.getRole(),
                    booking.getBookingStatus()
                );
            })
            .collect(Collectors.toList());
    }

    public List<BookingDto> getBookingsByAgentId(Long agentId) {
        return bookingRepository.findAll().stream()
            .filter(booking -> booking.getAgent().getId().equals(agentId))
            .map(booking -> {
                Package pkg = packageRepository.findById(booking.getaPackage().getPackageId()).orElse(null);
                User customer = userRepository.findById(booking.getCustomer().getId()).orElse(null);
                return new BookingDto(
                    booking.getBookingId(),
                    booking.getBookingDateTime(),
                    booking.getNumberOfTravelers(),
                    booking.getTotalAmount(),
                    booking.getaPackage().getPackageId(),
                    booking.getCustomer().getId(),
                    booking.getAgent().getId(),
                    pkg != null ? pkg.getTitle() : null,
                    pkg != null ? pkg.getStartDate() : null,
                    pkg != null ? pkg.getEndDate() : null,
                    pkg != null ? pkg.getLocation() : null,
                    customer != null ? customer.getFirstName() + " " + customer.getLastName() : null,
                    customer != null ? customer.getEmail() : null,
                    customer.getRole(),
                    booking.getBookingStatus()
                );
            })
            .collect(Collectors.toList());
    }
}
