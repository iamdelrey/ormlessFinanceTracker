package rbd.ormless.financetracker.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import rbd.ormless.financetracker.model.Transaction;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TransactionDAO {

    private final JdbcTemplate jdbcTemplate;

    public TransactionDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Transaction> findByCategoryId(String category) {
        String sql = "SELECT * FROM \"Transaction\" WHERE category = ?";
        List<Transaction> transactions = jdbcTemplate.query(sql, new Object[]{category}, this::mapRowToTransaction);
        System.out.println("Транзакции для категории: " + category + ", результат: " + transactions);
        return transactions;
    }


    public void addTransaction(Transaction transaction) {
        String sql = "INSERT INTO \"Transaction\" (id_account, id_user, amount, date_time, transaction_type, category, description, id_budget, id_goal) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, transaction.getIdAccount(), transaction.getIdUser(), transaction.getAmount(),
                transaction.getDateTime(), transaction.getTransactionType(), transaction.getCategory(),
                transaction.getDescription(), transaction.getIdBudget(), transaction.getIdGoal());
    }

    public void updateTransaction(Transaction transaction) {
        String sql = "UPDATE \"Transaction\" SET amount = ?, date_time = ?, transaction_type = ?, category = ?, description = ? " +
                "WHERE id_transaction = ? AND id_user = ?";
        jdbcTemplate.update(sql, transaction.getAmount(), transaction.getDateTime(),
                transaction.getTransactionType(), transaction.getCategory(),
                transaction.getDescription(), transaction.getIdTransaction(), transaction.getIdUser());
    }

    public List<Transaction> findByBudgetId(int idBudget) {
        String sql = "SELECT * FROM \"Transaction\" WHERE id_budget = ?";
        return jdbcTemplate.query(sql, new Object[]{idBudget}, this::mapRowToTransaction);
    }

    public void deleteTransaction(int idTransaction, int idUser) {
        String sql = "DELETE FROM \"Transaction\" WHERE id_transaction = ? AND id_user = ?";
        jdbcTemplate.update(sql, idTransaction, idUser);
    }

    public List<Transaction> findByBudgetPlanId(Long budgetPlanId) {
        String sql = "SELECT * FROM \"Transaction\" WHERE id_budget = ?";
        return jdbcTemplate.query(sql, new Object[]{budgetPlanId}, this::mapRowToTransaction);
    }

    public BigDecimal findTotalTransactionAmountByUserId(int userId) {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM \"Transaction\" WHERE id_user = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userId}, BigDecimal.class);
    }

    public void markAllAsReadForUser(int userId) {
        String sql = "UPDATE \"Notification\" SET status = 'Прочитано' WHERE status = 'Непрочитано' AND id_user = ?";
        jdbcTemplate.update(sql, userId);
    }

    private Transaction mapRowToTransaction(ResultSet rs, int rowNum) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setIdTransaction(rs.getInt("id_transaction"));
        transaction.setIdAccount(rs.getInt("id_account"));
        transaction.setIdUser(rs.getInt("id_user"));
        transaction.setAmount(rs.getBigDecimal("amount"));
        transaction.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
        transaction.setTransactionType(rs.getString("transaction_type"));
        transaction.setCategory(rs.getString("category"));
        transaction.setDescription(rs.getString("description"));
        transaction.setIdBudget(rs.getInt("id_budget"));
        transaction.setIdGoal(rs.getInt("id_goal"));
        return transaction;
    }

}
