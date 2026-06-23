package com.kamu.project.tasktrigger.controller;

import com.kamu.project.tasktrigger.dto.TaskRequest;
import com.kamu.project.tasktrigger.dto.TaskResponse;
import com.kamu.project.tasktrigger.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request){
        TaskResponse response = taskService.createTask(request);
        return ResponseEntity.accepted().body(response); // 202 accepted hhtp kodu donuyor
    }

}
