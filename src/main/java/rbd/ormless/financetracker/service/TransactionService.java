package rbd.ormless.financetracker.service;

import org.springframework.stereotype.Service;
import rbd.ormless.financetracker.dao.TransactionDAO;
import rbd.ormless.financetracker.model.Transaction;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionDAO transactionDAO;

    public TransactionService(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
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
