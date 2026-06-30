package com.kamu.project.tasktrigger.dto;


import com.kamu.project.tasktrigger.model.TaskType;

import java.util.UUID;

// Worker'ın işi yapabilmesi için sadece ID, tip ve payload bilgisi yeterlidir.
public record TaskMessage(
        UUID id,
        TaskType taskType,
        String payload
) {}