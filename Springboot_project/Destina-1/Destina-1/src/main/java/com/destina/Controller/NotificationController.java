package com.destina.Controller;



import com.destina.Dto.NotificationDto;
import com.destina.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getNotifications() {
        List<NotificationDto> notifications = notificationService.getNotifications();
        return ResponseEntity.ok(notifications);
    }
    
    @PostMapping("/create")
    public String createNotification(@RequestParam String subject, @RequestParam Long customerId) {
        notificationService.saveNotification(subject, customerId, 1L); // Assuming 1L as default fromId
        return "Notification Created Successfully";
}
    }

