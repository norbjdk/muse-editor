package com.muse.editor.core.model.message;

import com.fasterxml.jackson.annotation.JsonProperty;

public record InvitationMessage(
        @JsonProperty("type") String type,
        @JsonProperty("from") String username,
        @JsonProperty("content") String content
) {}
