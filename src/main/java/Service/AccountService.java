package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {

    private static AccountDAO accountDAO;
    
    public AccountService(AccountDAO accountDAO) {
        AccountService.accountDAO = accountDAO;
    }

    public static Account registerAccount(Account account) {
        if (account.getUsername().isEmpty()
                    || account.getPassword().length() < 4
                    || accountDAO.getAccountByUsername(account.getUsername()) != null) {
                return null;
        } else {
            Account newAccount = accountDAO.registerAccount(account);
            return newAccount;
        }
    }

    public static Account userLogin(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username and password are required");
        }
        Account account = accountDAO.getAccountByUsername(username);
        if (account == null || !account.getPassword().equals(password)) {
            return null;
        } else {
            return account;
        }
    }
}