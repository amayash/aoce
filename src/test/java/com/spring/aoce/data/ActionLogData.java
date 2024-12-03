package com.spring.aoce.data;

import com.spring.aoce.entity.ActionLog;

import java.time.LocalDateTime;

import static com.spring.aoce.data.UserData.createUser;

public class ActionLogData {
    public static ActionLog createActionLog() {
        ActionLog actionLog = new ActionLog();
        actionLog.setId(1L);
        actionLog.setTimestamp(LocalDateTime.now());
        actionLog.setUser(createUser());
        return actionLog;
    }
}
