package com.kamu.project.tasktrigger.service;

import com.kamu.project.tasktrigger.config.RabbitMQConfig;
import com.kamu.project.tasktrigger.dto.TaskMessage;
import com.kamu.project.tasktrigger.dto.TaskRequest;
import com.kamu.project.tasktrigger.dto.TaskResponse;
import com.kamu.project.tasktrigger.model.Task;
import com.kamu.project.tasktrigger.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final RabbitTemplate rabbitTemplate;

    public TaskResponse createTask(TaskRequest request) {
        // dtodan gelen verilerle yeni bir entitiy olustur
        Task newTask = Task.builder()
                .taskType(request.taskType())
                .payload(request.payload())
                //status ve createdAt @PrePersist sayesinde otomatik ayarlanacak
                .build();

        // veritabani kayit
        Task savedTask = taskRepository.save(newTask);

        TaskMessage messageToQueue = new TaskMessage(
                savedTask.getId(),
                savedTask.getTaskType(),
                savedTask.getPayload()
        );

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

}
