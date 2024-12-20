package rbd.ormless.financetracker.model;

import java.math.BigDecimal;
import rbd.ormless.financetracker.model.BudgetPlan;
import java.time.LocalDateTime;

public class Notification {
    private int idNotification;
    private String notificationText;
    private LocalDateTime notificationDateTime;
    private String notificationDateTimeFormatted; // Новое поле
    private String status;
    private int idUser;

    public Notification() {}

    // Конструктор для использования в вашем коде
    public Notification(String notificationText, LocalDateTime notificationDateTime, String status, int idUser) {
        this.notificationText = notificationText;
        this.notificationDateTime = notificationDateTime;
        this.status = status;
        this.idUser = idUser;
    }

    // Геттер и сеттер для нового поля
    public String getNotificationDateTimeFormatted() {
        return notificationDateTimeFormatted;
    }

    public void setNotificationDateTimeFormatted(String notificationDateTimeFormatted) {
        this.notificationDateTimeFormatted = notificationDateTimeFormatted;
    }

    // Остальные геттеры и сеттеры
    public int getIdNotification() {
        return idNotification;
    }

    public void setIdNotification(int idNotification) {
        this.idNotification = idNotification;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public LocalDateTime getNotificationDateTime() {
        return notificationDateTime;
    }

    public void setNotificationDateTime(LocalDateTime notificationDateTime) {
        this.notificationDateTime = notificationDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}
