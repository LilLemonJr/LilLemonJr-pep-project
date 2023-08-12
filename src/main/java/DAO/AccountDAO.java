package DAO;

import Model.Account;
import java.sql.*;
import Util.ConnectionUtil;

public class AccountDAO {

    public Account registerAccount(Account account) {  
        Connection connection = ConnectionUtil.getConnection();
        Account registeredAccount = null;
        try {
            String SQL ="INSERT INTO account(username, password) VALUES(?,?);";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                registeredAccount = account; 
            }  
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return registeredAccount;
    }

    public Account getAccountByUsername(String username) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String SQL = "SELECT * FROM account WHERE username = ?;";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Account account = new Account(
                    rs.getInt("account_id"), 
                    rs.getString("username"), 
                    rs.getString("password")
                );
                return account;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
        return null;
    }
}