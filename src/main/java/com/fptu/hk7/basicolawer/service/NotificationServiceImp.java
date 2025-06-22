package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.pojo.Notification;
import com.fptu.hk7.basicolawer.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NotificationServiceImp implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImp(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    @Override
    public Optional<Notification> findById(UUID id) {
        return notificationRepository.findById(id);
    }

    @Override
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public Notification update(UUID id, Notification notification) {
        if (!notificationRepository.existsById(id)) {
            throw new RuntimeException("Notification not found");
        }
        notification.setId(id);
        return notificationRepository.save(notification);
    }

    @Override
    public void deleteById(UUID id) {
        notificationRepository.deleteById(id);
    }
}
