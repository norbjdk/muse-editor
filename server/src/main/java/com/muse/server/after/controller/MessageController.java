package com.muse.server.after.controller;

import com.muse.server.after.dto.msg.InvitationResponse;
import com.muse.server.after.dto.msg.InviteCollaboratorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public MessageController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/invite/{targetUsername}")
    public void handleInvitationRequest(@DestinationVariable String targetUsername, @Payload InviteCollaboratorMessage invite) {
        System.out.println("Received invitation: " + invite);
        System.out.println("Target user: " + targetUsername);
        System.out.println("Sending to: /user/" + targetUsername + "/queue/notifications");

        System.out.println("Type: " + invite.type());
        System.out.println("From: " + invite.from());
        System.out.println("Content: " + invite.content());

        messagingTemplate.convertAndSendToUser(targetUsername, "/queue/notifications", invite);
    }

    @MessageMapping("/invite/response/{fromUsername}")
    public void handleInvitationResponse(@DestinationVariable String fromUsername, @Payload InvitationResponse response) {
        messagingTemplate.convertAndSendToUser(fromUsername, "/queue/invitation-responses", response);
    }
}
