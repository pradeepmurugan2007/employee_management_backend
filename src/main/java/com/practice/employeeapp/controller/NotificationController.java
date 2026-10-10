package com.practice.employeeapp.controller;

import com.practice.employeeapp.models.Notification;
import com.practice.employeeapp.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(originPatterns = "*")
public class NotificationController {

    @Autowired
    private NotificationRepository repository;

    @GetMapping
    public List<Notification> getNotifications() {
        List<Notification> notifs = repository.findAllByOrderByCreatedAtDesc();
        if (notifs.isEmpty()) {
            Notification n1 = new Notification();
            n1.setTitle("System Update");
            n1.setMessage("Welcome to EmpLy! Ensure your settings are configured.");
            n1.setType("INFO");
            n1.setRead(false);
            n1.setCreatedAt(LocalDateTime.now().minusHours(1));
            repository.save(n1);
            
            Notification n2 = new Notification();
            n2.setTitle("Success");
            n2.setMessage("Backend connected successfully.");
            n2.setType("SUCCESS");
            n2.setRead(false);
            n2.setCreatedAt(LocalDateTime.now().minusMinutes(10));
            repository.save(n2);
            
            return repository.findAllByOrderByCreatedAtDesc();
        }
        return notifs;
    }

    @PutMapping("/{id}/read")
    public Notification markAsRead(@PathVariable Long id) {
        Notification notif = repository.findById(id).orElseThrow();
        notif.setRead(true);
        return repository.save(notif);
    }
    
    @PutMapping("/read-all")
    public void markAllAsRead() {
        List<Notification> notifs = repository.findAll();
        notifs.forEach(n -> n.setRead(true));
        repository.saveAll(notifs);
    }
}
