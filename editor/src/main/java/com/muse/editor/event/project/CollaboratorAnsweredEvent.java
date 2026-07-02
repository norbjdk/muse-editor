package com.muse.editor.event.project;

import com.muse.editor.core.model.message.InvitationResponse;
import com.muse.editor.event.model.AppEvent;

public class CollaboratorAnsweredEvent extends AppEvent {
    private final InvitationResponse invitationResponse;

    public CollaboratorAnsweredEvent(InvitationResponse invitationResponse) {
        this.invitationResponse = invitationResponse;
    }

    public InvitationResponse getInvitationResponse() {
        return invitationResponse;
    }
}
