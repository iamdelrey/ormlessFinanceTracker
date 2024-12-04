package rbd.ormless.financetracker.service;

import org.springframework.stereotype.Service;
import rbd.ormless.financetracker.dao.TransactionDAO;
import rbd.ormless.financetracker.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionDAO transactionDAO;

    public TransactionService(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

//    public List<Transaction> getTransactionsByType(String transactionType) {
//        return transactionDAO.findByTransactionType(transactionType);
//    }

    public List<Transaction> getTransactionsByTypeAndAmount(String transactionType, BigDecimal minAmount, BigDecimal maxAmount) {
        return transactionDAO.findByTypeAndAmountRange(transactionType, minAmount, maxAmount);
    }

    public List<Transaction> getTransactionsByCategoryAndAmount(String category, BigDecimal minAmount, BigDecimal maxAmount) {
        return transactionDAO.findByCategoryAndAmountRange(category, minAmount, maxAmount);
    }

    public List<Transaction> getTransactionsByCategoryId(String category) {
        return transactionDAO.findByCategoryId(category);
    }

    public void addTransaction(Transaction transaction) {
        transactionDAO.addTransaction(transaction);
    }

    public List<Transaction> getTransactionsByBudgetId(int idBudget) {
        return transactionDAO.findByBudgetId(idBudget); // Вызываем метод DAO
    }

    public void updateTransaction(Transaction transaction) {
        transactionDAO.updateTransaction(transaction);
    }

    public void deleteTransaction(int idTransaction, int idUser) {
        transactionDAO.deleteTransaction(idTransaction, idUser);
    }
}
