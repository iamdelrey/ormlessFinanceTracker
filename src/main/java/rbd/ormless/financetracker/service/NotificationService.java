package rbd.ormless.financetracker.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    private final List<String> notifications = new ArrayList<>();

    public void addNotification(String message) {
        notifications.add(message);
    }

    public List<String> getNotifications() {
        return new ArrayList<>(notifications);
    }

    public void clearNotifications() {
        notifications.clear();
    }
}
