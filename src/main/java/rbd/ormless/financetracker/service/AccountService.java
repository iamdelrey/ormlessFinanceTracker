package rbd.ormless.financetracker.service;

import org.springframework.stereotype.Service;
import rbd.ormless.financetracker.dao.AccountDAO;
import rbd.ormless.financetracker.dao.UserDAO;
import rbd.ormless.financetracker.model.Account;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountDAO accountDAO;
    private final UserDAO userDAO; // Добавлено

    public AccountService(AccountDAO accountDAO, UserDAO userDAO) {
        this.accountDAO = accountDAO;
        this.userDAO = userDAO; // Инициализация
    }

    public List<Account> getAllAccounts(int userId) {
        return accountDAO.findAll(userId);
    }

    public void addAccount(Account account) {
        accountDAO.save(account);
    }

    public void updateAccount(Account account) {
        accountDAO.update(account);
    }

    public void deleteAccount(int accountId, int userId) {
        accountDAO.delete(accountId, userId);
    }

    public rbd.ormless.financetracker.model.User getUserByEmail(String email) {
        return userDAO.findByEmail(email).orElse(null);
    }

    public BigDecimal getAccountBalance(int userId) {
        return accountDAO.getBalance(userId);
    }
}
