package com.kamu.project.tasktrigger.service;

import com.kamu.project.tasktrigger.config.RabbitMQConfig;
import com.kamu.project.tasktrigger.dto.TaskMessage;
import com.kamu.project.tasktrigger.dto.TaskRequest;
import com.kamu.project.tasktrigger.dto.TaskResponse;
import com.kamu.project.tasktrigger.model.Task;
import com.kamu.project.tasktrigger.model.TaskStatus;
import com.kamu.project.tasktrigger.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final RabbitTemplate rabbitTemplate;
    private final RedisTemplate<String, String> redisTemplate;

    public TaskResponse createTask(TaskRequest request) {
        // dtodan gelen verilerle yeni bir entitiy olustur
        Task newTask = Task.builder()
                .taskType(request.taskType())
                .payload(request.payload())
                .status(TaskStatus.PENDING)
                //status ve createdAt @PrePersist sayesinde otomatik ayarlanacak
                .build();
        // veritabani kayit
        Task savedTask = taskRepository.save(newTask);
        TaskMessage messageToQueue = new TaskMessage(
                savedTask.getId(),
                savedTask.getTaskType(),
                savedTask.getPayload()
        );

        redisTemplate.opsForValue().set(savedTask.getId().toString(),"PENDING");
        //TODO: rabbitmqya atilacak
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, messageToQueue);

        //kullaniciya jobId don
        return new TaskResponse(
                savedTask.getId(),
                "ACCEPTED",
                "Görev başarıyla alındı ve sıraya eklendi."
        );
    }

    public TaskResponse getTaskStatus(UUID taskId){

        String status = redisTemplate.opsForValue().get(taskId.toString());

        if (status != null){
            return new TaskResponse(taskId, status, "Redis'ten hızlı cevap");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Görev bulunamadı: " + taskId));

        return new TaskResponse(
                task.getId(),
                task.getStatus().name(),
                "Görev durumu: " + task.getStatus()
        );
    }

}
