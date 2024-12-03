package rbd.ormless.financetracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rbd.ormless.financetracker.dao.UserDAO;
import rbd.ormless.financetracker.model.User;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(User user) {
        if (userDAO.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email уже используется");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDAO.save(user);
    }

    public rbd.ormless.financetracker.model.User findByEmail(String email) {
        return userDAO.findByEmail(email).orElse(null);
    }
}
