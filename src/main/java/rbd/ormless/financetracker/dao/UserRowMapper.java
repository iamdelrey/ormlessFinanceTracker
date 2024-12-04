package rbd.ormless.financetracker.dao;

import org.springframework.jdbc.core.RowMapper;
import rbd.ormless.financetracker.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<rbd.ormless.financetracker.model.User> {
    @Override
    public rbd.ormless.financetracker.model.User mapRow(ResultSet rs, int rowNum) throws SQLException {
        rbd.ormless.financetracker.model.User user = new rbd.ormless.financetracker.model.User();
        user.setId(rs.getInt("id_user"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        return user;
    }
}

