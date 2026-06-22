package com.kamu.project.tasktrigger.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Enumlari veritabanina string olarak kaydediyorum 0,1,2 yerine direkt PENDING VS
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskType taskType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    //Gorev icerigini JSON string olarak burda tutucaz
    @Column(columnDefinition = "TEXT", nullable = false)
    private String payload;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt= LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if ( this.status == null){
            this.status = TaskStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }


}
