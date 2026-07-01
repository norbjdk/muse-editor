package com.muse.editor.event.project;

import com.muse.editor.core.model.message.InvitationMessage;
import com.muse.editor.event.model.AppEvent;

public class CollaboratorInvitedEvent extends AppEvent {
    private final InvitationMessage invitationMessage;

    public CollaboratorInvitedEvent(InvitationMessage invitationMessage) {
        this.invitationMessage = invitationMessage;
    }

    public InvitationMessage getInvitationMessage() {
        return invitationMessage;
    }
}
