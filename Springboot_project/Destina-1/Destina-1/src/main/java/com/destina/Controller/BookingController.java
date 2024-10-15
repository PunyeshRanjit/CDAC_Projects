package com.destina.Controller;

import com.destina.model.Booking;
import com.destina.Service.BookingService;
import com.destina.Service.EmailService;
import com.destina.Service.NotificationService;
import com.destina.Dto.BookingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationService notificationService;

    // POST: api/booking
    @PostMapping
    public ResponseEntity<String> createBooking(@RequestBody BookingDto bookingDto) {
        try {
            bookingService.createBooking(bookingDto);
            return ResponseEntity.ok("Booking Created Successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating booking: " + e.getMessage());
        }
    }

    // DELETE: api/booking/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelBooking(@PathVariable("id") Long id) {
        try {
            bookingService.cancelBooking(id);
            return ResponseEntity.ok("Your Booking Cancelled!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error cancelling booking: " + e.getMessage());
        }
    }

    // GET: api/booking/customer/{customerId}
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<BookingDto>> getBookingsByCustomerId(@PathVariable("customerId") Long customerId) {
        List<BookingDto> bookings = bookingService.getBookingsByCustomerId(customerId);
        if (bookings.isEmpty()) {
            return ResponseEntity.ok().body(bookings);
        }
        return ResponseEntity.ok(bookings);
    }

    // GET: api/booking/agent/{agentId}
    @GetMapping("/agent/{agentId}")
    public ResponseEntity<List<BookingDto>> getBookingsByAgentId(@PathVariable("agentId") Long agentId) {
        List<BookingDto> bookings = bookingService.getBookingsByAgentId(agentId);
        if (bookings.isEmpty()) {
            return ResponseEntity.ok().body(bookings);
        }
        return ResponseEntity.ok(bookings);
    }
}
