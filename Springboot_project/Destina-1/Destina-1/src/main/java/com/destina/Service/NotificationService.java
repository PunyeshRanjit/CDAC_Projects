package com.destina.Service;



import com.destina.Repository.*;
import com.destina.model.Notification;
import com.destina.model.User;

import jakarta.transaction.Transactional;

import com.destina.Dto.NotificationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;

    public List<NotificationDto> getNotifications() {
        return notificationRepository.findAll()
                .stream()
                .map(n -> new NotificationDto(
                    n.getNotificationId(),
                    n.getSubject(),
                    n.getFrom().getFirstName() + " " + n.getFrom().getLastName(),
                    n.getCustomer().getFirstName() + " " + n.getCustomer().getLastName()
                ))
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void saveNotification(String subject, Long customerId, Long fromId) {
        // Fetching the 'from' and 'customer' User entities
        User fromUser = userRepository.findById(fromId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid fromId: " + fromId));
        User customerUser = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customerId: " + customerId));

        // Creating and saving the Notification entity
        Notification notification = new Notification();
        notification.setSubject(subject);
        notification.setFrom(fromUser);
        notification.setCustomer(customerUser);

        notificationRepository.save(notification);
    }
}


