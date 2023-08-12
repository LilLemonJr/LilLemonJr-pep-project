package Controller;

import Service.AccountService;
import Service.MessageService;
import Model.Account;
import Model.Message;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/Account", this::registerAccountHandler);
        app.post("/Account/{account_id}", this::userLoginHandler);
        app.post("/Message", this::newMsgHandler);
        app.get("/Message", this::getAllMsgHandler);
        app.get("/Message/{message_id}", this::getMsgByMsgIdHandler);
        app.delete("/Message", this::deleteByMsgIdHandler);
        app.patch("/Message/{message_id}", this::updateByMsgIdHandler);
        app.get("/Message/{posted_by}", this:: getAllMsgByAcctIdHandler);

        return app;
    }

    private void registerAccountHandler(Context context) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Account account = mapper.readValue(context.body(), Account.class);
            Account registerAccount = AccountService.registerAccount(account);

            if (registerAccount == null) {
                context.status(400);
            } else {
                context.status(200); // rather 201
                context.json(registerAccount);
            }
        } catch (IOException e) {
            e.printStackTrace();
            context.status(400);
        } 
    }

    private void userLoginHandler(Context context) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Account account = mapper.readValue(context.body(), Account.class);

            String username = account.getUsername();
            String password = account.getPassword();

            Account retrievedAccount = AccountService.userLogin(username, password);
    
            if (retrievedAccount == null) {
                context.status(401); // Unauthorized
                context.result("Invalid credentials");
            } else {
                context.status(200);
                context.json(retrievedAccount);
            }
        } catch (IOException e) {
            e.printStackTrace();
            context.status(400);
            context.result("Bad request");
        }
    }

    private void newMsgHandler(Context context) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Message message = mapper.readValue(context.body(), Message.class);
            Message newMessage = MessageService.newMessage(message);
            if (newMessage == null){
                context.status(400);
            } else {
                context.status(200); // try 201
                context.json(newMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
            context.status(400);
        }
    }

    private void getAllMsgHandler(Context context) { //not using context?
        List<Message> messages = MessageService.getAllMessages();
        context.json(messages);
    }

    private void getMsgByMsgIdHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message messages = MessageService.getMessageByMessageId(messageId);
        if (messages == null) {
            context.status(200); // try 204 (no content)
        } else {
            context.json(messages);
            context.status(200);

        }
    }

    private void deleteByMsgIdHandler(Context context) {
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message isDeleted = MessageService.deleteByMessageId(message_id);
        if (isDeleted == null) {
            context.status(200); // try 204
        } else {
            context.status(200);
            context.json(isDeleted);
        }
    }

    private void updateByMsgIdHandler(Context context) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Message message = mapper.readValue(context.body(), Message.class);
            int message_id = Integer.parseInt(context.pathParam("message_id"));
            Message updatedMessage = MessageService.updateByMessageId(message, message_id);
            if(updatedMessage == null){
                context.status(400); // try 404
            }else{
                context.status(200);
                context.json(updatedMessage);
            }
        } catch (IOException e) {
            context.status(400);
            e.printStackTrace();
        }
    }

    private void getAllMsgByAcctIdHandler(Context context) {
        int accountId = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = MessageService.getAllMessagesByAccountId(accountId);
        if (messages.isEmpty()) {
            context.status(200); // try 204
        } else {
            context.json(messages);
        }
    }
}