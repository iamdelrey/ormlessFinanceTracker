package rbd.ormless.financetracker.service;

import org.springframework.stereotype.Service;
import rbd.ormless.financetracker.dao.NotificationDAO;

@Service
public class NotificationService {

    private final NotificationDAO notificationDAO;

    public NotificationService(NotificationDAO notificationDAO) {
        this.notificationDAO = notificationDAO;
    }

    public void deleteNotification(int notificationId) {
        notificationDAO.delete(notificationId);
    }

    public void markAllAsReadForUser(int userId) {
        notificationDAO.markAllAsReadForUser(userId);
    }
}
