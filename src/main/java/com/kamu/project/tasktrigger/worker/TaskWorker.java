package com.kamu.project.tasktrigger.worker;

import com.kamu.project.tasktrigger.config.RabbitMQConfig;
import com.kamu.project.tasktrigger.dto.TaskMessage;
import com.kamu.project.tasktrigger.model.Task;
import com.kamu.project.tasktrigger.model.TaskStatus;
import com.kamu.project.tasktrigger.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class TaskWorker {

    private final TaskRepository taskRepository;
    private final JavaMailSender mailSender;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitMQConfig.QUEUE_NAME, durable = "true"),
                    exchange = @Exchange(value = RabbitMQConfig.EXCHANGE_NAME, type = "direct"),
                    key = RabbitMQConfig.ROUTING_KEY
            ),
            containerFactory = "myWorkerFactory"
    )
    public void processTask(TaskMessage message){
        System.out.println("YENİ GÖREV YAKALANDI: " + message.id());

        Task task = taskRepository.findById(message.id()).orElse(null);
        if ( task == null){
            System.out.println("HATA: Görev veritabanında bulunamadı!");
            return;
        }

        try{
            task.setStatus(TaskStatus.PROCESSING);
            taskRepository.save(task);

            // gorev tipine gore icraat
            if(task.getTaskType().name().equals("BULK_EMAIL")){
                System.out.println("Mail gönderimi başlatılıyor...");

                //gercek mail olusturma ve gonderme
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setFrom("sistem@tasktrigger.com");
                mailMessage.setTo("kullanici@test.com");
                mailMessage.setSubject("TaskTrigger Otomasyon Raporu - Görev ID: "+ message.id());
                mailMessage.setText("Merhaba, \n\nSisteme gönderdiğiniz komut başarıyla işlenmiştir. \n\nGörev Detayı: " + message.payload());

                mailSender.send(mailMessage);

                System.out.println("Mail başarıyla gönderildi!");

            }

            task.setStatus(TaskStatus.COMPLETED);
            taskRepository.save(task);
            System.out.println("Görev statüsü COMPLETED olarak güncellendi. İşlem bitti!\n");

        } catch ( Exception e ) {
            task.setStatus(TaskStatus.FAILED);
            taskRepository.save(task);
            System.out.println("Görev işlenirken hata oluştu! Statü FAILED yapıldı. Hata: " + e.getMessage() + "\n");
        }

 
    }

}
