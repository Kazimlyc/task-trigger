package com.kamu.project.tasktrigger.worker;

import com.kamu.project.tasktrigger.config.RabbitMQConfig;
import com.kamu.project.tasktrigger.dto.TaskMessage;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class TaskWorker {

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitMQConfig.QUEUE_NAME, durable = "true"),
                    exchange = @Exchange(value = RabbitMQConfig.EXCHANGE_NAME, type = "direct"),
                    key = RabbitMQConfig.ROUTING_KEY
            ),
            containerFactory = "myWorkerFactory"
    )
    public void processTask(TaskMessage message){
        System.out.println("YENİ GÖREV YAKALANDI!");
        System.out.println("Görev ID   :" + message.id());
        System.out.println("Görev Tipi :" + message.taskType());
        System.out.println("Detay      :" + message.payload());
        System.out.println("-------------------------------------------------");
    }

}
