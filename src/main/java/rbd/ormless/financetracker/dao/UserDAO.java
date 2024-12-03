package rbd.ormless.financetracker.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import rbd.ormless.financetracker.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class UserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Метод для сохранения пользователя в базе данных
    public void save(User user) {
        String sql = "INSERT INTO \"User\" (name, surname, email, password) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getName(), user.getSurname(), user.getEmail(), user.getPassword());
    }

    // Метод для проверки, существует ли пользователь с указанным email
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM \"User\" WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }


    public Optional<rbd.ormless.financetracker.model.User> findByEmail(String email) {
        String sql = "SELECT * FROM \"User\" WHERE email = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new Object[]{email}, this::mapRowToUser));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private rbd.ormless.financetracker.model.User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        rbd.ormless.financetracker.model.User user = new rbd.ormless.financetracker.model.User();
        user.setId(rs.getInt("id_user"));
        user.setName(rs.getString("name"));
        user.setSurname(rs.getString("surname"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        return user;
    }
}
