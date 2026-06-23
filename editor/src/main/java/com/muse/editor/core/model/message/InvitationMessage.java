package com.muse.editor.core.model.message;

import com.fasterxml.jackson.annotation.JsonProperty;

public record InvitationMessage(String type, String from, String content) {}