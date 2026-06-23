package com.kamu.project.tasktrigger.dto;

import com.kamu.project.tasktrigger.model.TaskType;

//kullanici bize sadece hangi gorevi istedigini ve json icerigini gondericek
public record TaskRequest(
        TaskType taskType,
        String payload
) {}
