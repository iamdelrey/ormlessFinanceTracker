package rbd.ormless.financetracker.service;

import org.springframework.stereotype.Service;
import rbd.ormless.financetracker.dao.NotificationDAO;
import rbd.ormless.financetracker.model.Notification;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    private final NotificationDAO notificationDAO;

    public NotificationService(NotificationDAO notificationDAO) {
        this.notificationDAO = notificationDAO;
    }

    // Убедитесь, что это работает корректно в NotificationService
    public void createNotification(String text, int userId) {
        Notification notification = new Notification();
        notification.setNotificationText(text);
        notification.setNotificationDateTime(LocalDateTime.now());
        notification.setStatus("Непрочитано");
        notification.setIdUser(userId);

        notificationDAO.save(notification);
    }

    public void deleteNotification(int notificationId) {
        notificationDAO.delete(notificationId);
    }

    public void markAllAsRead(int userId) {
        notificationDAO.markAllAsRead(userId);
    }
}

