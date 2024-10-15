package com.destina.Service;

import com.destina.Dto.PaymentDto;
import com.destina.model.Booking;
import com.destina.model.Payment;
import com.destina.Repository.BookingRepository;
import com.destina.Repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public String payForBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!"Pending".equals(booking.getBookingStatus())) {
            return "Booking status is cancelled, Payment cannot be made.";
        }

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setCustomer(booking.getCustomer());
        payment.setAgent(booking.getAgent());
        payment.setAmountPaid(booking.getTotalAmount());
        payment.setPaymentStatus("Success");
        payment.setPaymentDate(java.time.LocalDateTime.now());

        paymentRepository.save(payment);

        booking.setBookingStatus("Paid");
        booking.setPayment(payment);
        bookingRepository.save(booking);

        return "Payment successful and booking status updated to Paid.";
    }

    public List<PaymentDto> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PaymentDto> getPaymentsByCustomer(Long customerId) {
        return paymentRepository.findByCustomerId(customerId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PaymentDto> getPaymentsByAgent(Long agentId) {
        return paymentRepository.findByAgentId(agentId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private PaymentDto convertToDto(Payment payment) {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setPaymentId(payment.getPaymentId());
        paymentDto.setAmountPaid(payment.getAmountPaid());
        paymentDto.setPaymentStatus(payment.getPaymentStatus());
        paymentDto.setPaymentDate(payment.getPaymentDate());
        paymentDto.setCustomerId(payment.getCustomer().getId());
        paymentDto.setAgentId(payment.getAgent().getId());
        paymentDto.setBookingId(payment.getBooking().getBookingId());
        return paymentDto;
    }

    public Optional<Payment> findPaymentByBookingId(Long bookingId) {
        return paymentRepository.findByBooking_BookingId(bookingId);
    }
}
