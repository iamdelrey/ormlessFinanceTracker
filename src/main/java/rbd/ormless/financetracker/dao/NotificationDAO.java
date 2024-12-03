package rbd.ormless.financetracker.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import rbd.ormless.financetracker.model.Notification;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class NotificationDAO {
    private final JdbcTemplate jdbcTemplate;

    public NotificationDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Notification> findByUserId(int userId) {
        String sql = "SELECT * FROM \"Notification\" WHERE id_user = ? ORDER BY notification_date_time DESC";
        return jdbcTemplate.query(sql, new Object[]{userId}, this::mapRowToNotification);
    }

    public void delete(int notificationId) {
        String sql = "DELETE FROM \"Notification\" WHERE id_notification = ?";
        jdbcTemplate.update(sql, notificationId);
    }

    public void markAllAsReadForUser(int userId) {
        String sql = "UPDATE \"Notification\" SET status = 'Прочитано' WHERE id_user = ? AND status = 'Непрочитано'";
        jdbcTemplate.update(sql, userId);
    }

    public void save(Notification notification) {
        String sql = "INSERT INTO \"Notification\" (notification_text, notification_date_time, status, id_user) " +
                "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                notification.getNotificationText(),
                notification.getNotificationDateTime(),
                notification.getStatus(),
                notification.getIdUser()
        );
    }

    public void markAllAsRead(int userId) {
        String sql = "UPDATE \"Notification\" SET status = 'Прочитано' WHERE id_user = ? AND status = 'Непрочитано'";
        jdbcTemplate.update(sql, userId);
    }

    private Notification mapRowToNotification(ResultSet rs, int rowNum) throws SQLException {
        Notification notification = new Notification();
        notification.setIdNotification(rs.getInt("id_notification"));
        notification.setNotificationText(rs.getString("notification_text"));
        notification.setNotificationDateTime(rs.getTimestamp("notification_date_time").toLocalDateTime());
        notification.setStatus(rs.getString("status"));
        notification.setIdUser(rs.getInt("id_user"));
        return notification;
    }
}
