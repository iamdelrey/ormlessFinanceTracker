package rbd.ormless.financetracker.dao;

import org.springframework.jdbc.core.RowMapper;
import rbd.ormless.financetracker.model.Transaction;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionRowMapper implements RowMapper<Transaction> {
    @Override
    public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setIdTransaction(rs.getInt("id_transaction"));
        transaction.setIdAccount(rs.getInt("id_account"));
        transaction.setIdUser(rs.getInt("id_user"));
        transaction.setAmount(BigDecimal.valueOf(rs.getInt("amount")));
        transaction.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
        transaction.setTransactionType(rs.getString("transaction_type"));
        transaction.setCategory(rs.getString("category"));
        transaction.setDescription(rs.getString("description"));
        transaction.setIdBudget(rs.getInt("id_budget"));
        transaction.setIdGoal(rs.getInt("id_goal"));
        return transaction;
    }
}
