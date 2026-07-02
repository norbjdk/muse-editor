package com.muse.editor.core.model.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InvitationMessage {
    private String type;
    private String username;
    private String content;


    public InvitationMessage() {}

    public InvitationMessage(String type, String username, String content) {
        this.type = type;
        this.username = username;
        this.content = content;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    @JsonProperty("from")
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    @Override
    public String toString() {
        return "InvitationMessage{type='" + type + "', username='" + username + "', content='" + content + "'}";
    }
}