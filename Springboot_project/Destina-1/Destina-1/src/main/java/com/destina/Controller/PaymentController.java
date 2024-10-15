package com.destina.Controller;



import com.destina.Dto.PaymentDto;
import com.destina.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/pay-for-booking/{bookingId}")
    public ResponseEntity<String> payForBooking(@PathVariable("bookingId") Long bookingId) {
        String result = paymentService.payForBooking(bookingId);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<PaymentDto>> getAllPayments() {
        List<PaymentDto> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<PaymentDto>> getPaymentsByCustomer(@PathVariable("customerId") Long customerId) {
        List<PaymentDto> payments = paymentService.getPaymentsByCustomer(customerId);
        if (payments.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(payments);
    }
    
    
    @GetMapping("/agent/{agentId}")
    public ResponseEntity<List<PaymentDto>> getPaymentsByAgent(@PathVariable("agentId") Long agentId) {
        List<PaymentDto> payments = paymentService.getPaymentsByAgent(agentId);
        if (payments.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(payments);
    }

    

}
