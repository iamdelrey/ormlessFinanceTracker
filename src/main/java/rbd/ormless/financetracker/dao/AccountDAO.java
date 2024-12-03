package rbd.ormless.financetracker.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import rbd.ormless.financetracker.model.Account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AccountDAO {

    private final JdbcTemplate jdbcTemplate;

    public AccountDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Account> findAll(int userId) {
        String sql = "SELECT * FROM \"Account\" WHERE id_user = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, this::mapRowToAccount);
    }

    public void save(Account account) {
        String sql = "INSERT INTO \"Account\" (id_user, account_name, account_type, balance) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, account.getIdUser(), account.getAccountName(), account.getAccountType(), account.getBalance());
    }

    public void update(Account account) {
        String sql = "UPDATE \"Account\" SET account_name = ?, account_type = ?, balance = ? WHERE id_account = ? AND id_user = ?";
        jdbcTemplate.update(sql, account.getAccountName(), account.getAccountType(), account.getBalance(), account.getIdAccount(), account.getIdUser());
    }

    public void delete(int accountId, int userId) {
        String sql = "DELETE FROM \"Account\" WHERE id_account = ? AND id_user = ?";
        jdbcTemplate.update(sql, accountId, userId);
    }

    private Account mapRowToAccount(ResultSet rs, int rowNum) throws SQLException {
        Account account = new Account();
        account.setIdAccount(rs.getInt("id_account"));
        account.setIdUser(rs.getInt("id_user"));
        account.setAccountName(rs.getString("account_name"));
        account.setAccountType(rs.getString("account_type"));
        account.setBalance(rs.getInt("balance"));
        return account;
    }
}
