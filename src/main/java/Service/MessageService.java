package Service;

import DAO.MessageDAO;
import Model.Message;
import java.util.*;

public class MessageService {

    private static MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }  

    public static Message newMessage (Message message) {
        // Validate that the message content is not empty
        if (message == null || message.getMessage_text() == null 
		|| message.getMessage_text().isEmpty() 
		|| message.getMessage_text().length() > 255) { 

            throw new IllegalArgumentException("Message content cannot be empty");
        }
        Message savedMessage = messageDAO.saveNewMessage(message);
        return savedMessage;
    }

    public static List<Message> getAllMessages() {
        List<Message> allMessages = messageDAO.getAllMessages();
        return allMessages;
    }

    public static Message getMessageByMessageId(int message_id) {
        Message message = messageDAO.getMessageByMessageId(message_id);
        return message;
    }

    public static Message deleteByMessageId(int message_id) {
        Message message = messageDAO.getMessageByMessageId(message_id);
        messageDAO.deleteByMessageId(message_id);
        return message;
    }

    public static Message updateByMessageId(Message updatedMessage, int message_id) {
        Message existingMessage = messageDAO.getMessageByMessageId(message_id);
        if (updatedMessage.getMessage_text() == null 
		    || updatedMessage.getMessage_text().isEmpty() 
		    || updatedMessage.getMessage_text().length() > 255) 
	    { 
            throw new IllegalArgumentException("Message not found with ID: " + message_id);
        } else {
            messageDAO.updateByMessageId(updatedMessage, message_id);
            return existingMessage;
        }
    }
    
    public static List<Message> getAllMessagesByAccountId (int account_id) {
        List<Message> allMessages = messageDAO.getAllMessagesByAccountId(account_id);
        return allMessages;
    }
}