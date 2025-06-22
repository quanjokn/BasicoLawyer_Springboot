package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.pojo.Notification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationService {
    List<Notification> findAll();
    Optional<Notification> findById(UUID id);
    Notification save(Notification notification);
    Notification update(UUID id, Notification notification);
    void deleteById(UUID id);
}
