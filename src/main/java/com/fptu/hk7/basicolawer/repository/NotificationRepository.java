package com.fptu.hk7.basicolawer.repository;

import com.fptu.hk7.basicolawer.pojo.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
}
