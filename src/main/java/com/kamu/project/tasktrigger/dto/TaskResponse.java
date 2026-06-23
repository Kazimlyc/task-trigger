package com.kamu.project.tasktrigger.dto;

import java.util.UUID;

//kullaniciya gorevin basariyla siraya alindigini ve takip etmesi gerekn jobId'yi donecek
public record TaskResponse(
        UUID jobId,
        String status,
        String message
) {}
