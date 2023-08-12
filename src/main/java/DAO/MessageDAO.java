package DAO;

import Model.Message;
import java.util.*;
import java.sql.*;
import Util.ConnectionUtil;

public class MessageDAO { 

    public Message saveNewMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?);" ;
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
 
            ps.setInt(1, message.getPosted_by()); 
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                int generated_message_id = (int) rs.getInt(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message";

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message message = new Message(
                    rs.getInt("message_id"), 
                    rs.getInt("posted_by"), 
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                messages.add(message);
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages; 
    }


    public Message getMessageByMessageId(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT message_id, posted_by, message_text, time_posted_epoch FROM message WHERE message_id = ?";
            
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, message_id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {   
                return new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message deleteByMessageId(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "DELETE FROM message WHERE message_id = ?"; 

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, message_id);

            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " row(s) deleted");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Message updateByMessageId(Message message, int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, message.getMessage_text());
            ps.setInt(2, message_id);

            ps.executeUpdate();
            return getMessageByMessageId(message_id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }



    public List<Message> getAllMessagesByAccountId(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT message_id, posted_by, message_text, time_posted_epoch FROM Message WHERE posted_by = ?"; 
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, account_id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message newMessage = new Message(
                    rs.getInt("message_id"), 
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                messages.add(newMessage);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }
}