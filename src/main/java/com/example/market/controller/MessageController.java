package com.example.market.controller;

import com.example.market.model.Message;
import com.example.market.model.User;
import com.example.market.service.MessageService;
import com.example.market.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MessageController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/user/{id}/newmessage")
    public String sendMessageToUser(@PathVariable Integer id, Model model) {
        String user = userService.getUserById(id).getUsername();
        String currentUsername = userService.getCurrentLoggedUser().getUsername();
        if(user.equals(currentUsername))
        {return "redirect:/mydata";}
        else {
            model.addAttribute("conversation", userService.getAllMessages(user));
            model.addAttribute("user", user);
            model.addAttribute("message", new Message());
            model.addAttribute("messages", userService.getConversations());
            model.addAttribute("username", userService.getCurrentLoggedUser().getUsername());
            model.addAttribute("invitations", userService.getCurrentLoggedUser().getInvitations());
            model.addAttribute("newmessage", userService.getCurrentLoggedUser().getNewmessage());
            return "newmessage";}
    }


    @RequestMapping(value = "/newmessage", method = RequestMethod.POST)
    public String saveMessageToUse(Message message) {
        Integer id = userService.getUserByName(message.getReceiver()).getId();
        messageService.saveMessage(message);
        return "redirect:/user/" + id + "/newmessage";
    }

    @RequestMapping(value = "/mymessages")
    public String getMyMessages(Model model) {
        String currentUsername = userService.getCurrentLoggedUser().getUsername();
        User user = userService.getUserByName(currentUsername);
        user.setNewmessage(0);
        userService.updateUser(user);
        model.addAttribute("messages", userService.getConversations());
        model.addAttribute("username", userService.getCurrentLoggedUser().getUsername());
        model.addAttribute("invitations", userService.getCurrentLoggedUser().getInvitations());
        model.addAttribute("newmessage", userService.getCurrentLoggedUser().getNewmessage());
        return "mymessages";
    }

    @RequestMapping(value = "/conversation/{user}")
    public String getConversation(@PathVariable String user) {
        Integer id = userService.getUserByName(user).getId();
        return "redirect:/user/" + id + "/newmessage";
    }
}
