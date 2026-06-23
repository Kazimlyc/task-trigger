package com.kamu.project.tasktrigger.service;

import com.kamu.project.tasktrigger.dto.TaskRequest;
import com.kamu.project.tasktrigger.dto.TaskResponse;
import com.kamu.project.tasktrigger.model.Task;
import com.kamu.project.tasktrigger.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskResponse createTask(TaskRequest request) {
        // dtodan gelen verilerle yeni bir entitiy olustur
        Task newTask = Task.builder()
                .taskType(request.taskType())
                .payload(request.payload())
                //status ve createdAt @PrePersist sayesinde otomatik ayarlanacak
                .build();

        // veritabani kayit

        Task savedTask = taskRepository.save(newTask);

        //TODO: rabbitmqya atilacak

        //kullaniciya jobId don
        return new TaskResponse(
                savedTask.getId(),
                "ACCEPTED",
                "Görev başarıyla alındı ve sıraya eklendi."
        );



    }

}
