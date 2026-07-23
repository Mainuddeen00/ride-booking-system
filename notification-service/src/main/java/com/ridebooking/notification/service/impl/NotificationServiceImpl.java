package com.ridebooking.notification.service.impl;

import com.ridebooking.notification.dto.NotificationRequestDTO;
import com.ridebooking.notification.dto.NotificationResponseDTO;
import com.ridebooking.notification.entity.Notification;
import com.ridebooking.notification.enums.NotificationStatus;
import com.ridebooking.notification.exception.NotificationAlreadySentException;
import com.ridebooking.notification.exception.ResourceNotFoundException;
import com.ridebooking.notification.mapper.NotificationMapper;
import com.ridebooking.notification.repository.NotificationRepository;
import com.ridebooking.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public NotificationResponseDTO createNotification(NotificationRequestDTO requestDTO) {

        Notification notification = NotificationMapper.toEntity(requestDTO);

        notification = notificationRepository.save(notification);

        return NotificationMapper.toResponseDTO(notification);
    }

    @Override
    public NotificationResponseDTO getNotificationById(Long notificationId) {

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Notification not found with id : " + notificationId));

        return NotificationMapper.toResponseDTO(notification);
    }

    @Override
    public List<NotificationResponseDTO> getNotificationsByUserId(Long userId) {

        return notificationRepository.findByUserId(userId)
                .stream()
                .map(NotificationMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<NotificationResponseDTO> getAllNotifications() {

        return notificationRepository.findAll()
                .stream()
                .map(NotificationMapper::toResponseDTO)
                .toList();
    }

    @Override
    public NotificationResponseDTO sendNotification(Long notificationId) {

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Notification not found with id : " + notificationId));

        if (notification.getStatus() == NotificationStatus.SENT) {
            throw new NotificationAlreadySentException(
                    "Notification has already been sent.");
        }

        notification.setStatus(NotificationStatus.SENT);
        notification.setSentAt(LocalDateTime.now());

        notification = notificationRepository.save(notification);

        return NotificationMapper.toResponseDTO(notification);
    }
}