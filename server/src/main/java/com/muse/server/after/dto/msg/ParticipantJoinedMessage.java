package com.muse.server.after.dto.msg;

public record ParticipantJoinedMessage(Long sessionId, Long userId, String username) {}

