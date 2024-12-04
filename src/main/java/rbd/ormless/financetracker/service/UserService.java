package rbd.ormless.financetracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rbd.ormless.financetracker.dao.UserDAO;
import rbd.ormless.financetracker.dao.UserRowMapper;
import rbd.ormless.financetracker.model.User;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Service
public class UserService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private rbd.ormless.financetracker.dao.UserDAO userDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void register(rbd.ormless.financetracker.model.User user) {
        if (userDAO.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email уже используется");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDAO.save(user);
    }

    public rbd.ormless.financetracker.model.User findByEmail(String email) {
        return userDAO.findByEmail(email).orElse(null);
    }

    public List<rbd.ormless.financetracker.model.User> getAllUsers() {
        String sql = "SELECT * FROM \"User\"";
        return jdbcTemplate.query(sql, new rbd.ormless.financetracker.dao.UserRowMapper());
    }

    public void deleteUser(int userId) {
        String sql = "DELETE FROM \"User\" WHERE id_user = ?";
        jdbcTemplate.update(sql, userId);
    }
}

