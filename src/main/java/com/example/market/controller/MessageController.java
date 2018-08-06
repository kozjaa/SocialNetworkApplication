package com.example.market.controller;

import com.example.market.model.Message;
import com.example.market.model.User;
import com.example.market.service.MessageService;
import com.example.market.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

@Controller
public class MessageController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/user/{id}/newmessage")
    public String sendMessageToUser(@PathVariable Integer id, Model model) {
        User currentUser = userService.getCurrentLoggedUser();
        String newMessageUsername = userService.getUserById(id).getUsername();
        String currentUsername = currentUser.getUsername();
        List<Message> conversationWithUserByUsername = userService.getAllMessages(newMessageUsername);
        List<String> myConversations = userService.getConversations();
        Integer myNumberOfFriendsInvitations = currentUser.getInvitations();
        Integer myNumberOfNewMessages = currentUser.getNewmessage();
        Integer myNumberOfNotifications = currentUser.getNotification();
        List<Message> myMessages = currentUser.getMymessages();
        boolean isNull = true;

        if(newMessageUsername.equals(currentUsername)) {
            return "redirect:/myprofile";}
        else {
            model.addAttribute("empty", isNull);
            model.addAttribute("conversation", conversationWithUserByUsername);
            model.addAttribute("user", newMessageUsername);
            model.addAttribute("current", currentUsername);
            model.addAttribute("message", new Message());
            model.addAttribute("messages", myConversations);
            model.addAttribute("username", currentUsername);
            model.addAttribute("invitations", myNumberOfFriendsInvitations);
            model.addAttribute("newmessage", myNumberOfNewMessages);
            model.addAttribute("notifications", myNumberOfNotifications);
            return "newmessage";}
    }

    @RequestMapping(value = "/newmessage", method = RequestMethod.POST)
    public String saveMessageToUse(@Valid Message message, BindingResult bindingResult) {
        String receiverName = message.getReceiver();
        Integer receiverId = userService.getUserByName(receiverName).getId();

        if(bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error ->{
                System.out.println(error.getObjectName() + " " + error.getDefaultMessage());
            });
            return "redirect:/user/" + receiverId + "/newmessage";}
        else {
            messageService.saveMessage(message);
            return "redirect:/user/" + receiverId + "/newmessage";}
    }

    @RequestMapping(value = "/mymessages")
    public String getMyMessages(Model model) {
        User currentUser = userService.getCurrentLoggedUser();
        String currentUsername = currentUser.getUsername();
        currentUser.setNewmessage(0);
        userService.updateUser(currentUser);
        List<Message> myMessages = currentUser.getMymessages();
        Integer myNumberOfFriendsInvitations = currentUser.getInvitations();
        Integer myNumberOfNotifications = currentUser.getNotification();
        boolean isNull = myMessages.size()>0;

        if (myMessages.size()>0){
            Message lastMessage = myMessages.get(myMessages.size()-1);
            String receiverNameFromLastMessage = lastMessage.getReceiver();

            if (receiverNameFromLastMessage.equals(currentUsername)) {
                Integer lastAuthorId = userService.getUserByName(lastMessage.getAuthor()).getId();
                return "redirect:/user/" + lastAuthorId + "/newmessage"; }
                else {
                Integer lastReceiverId = userService.getUserByName(receiverNameFromLastMessage).getId();
                return "redirect:/user/" + lastReceiverId + "/newmessage";}}

        else {
            model.addAttribute("empty", isNull);
            model.addAttribute("invitations", myNumberOfFriendsInvitations);
            model.addAttribute("username", currentUsername);
            model.addAttribute("notifications", myNumberOfNotifications);
            return "newmessage";}
    }

    @RequestMapping(value = "/conversation/{user}")
    public String getConversation(@PathVariable String user) {
        Integer conversationUserId = userService.getUserByName(user).getId();
        return "redirect:/user/" + conversationUserId + "/newmessage";
    }
}
