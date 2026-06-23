package com.kamu.project.tasktrigger.repository;

import com.kamu.project.tasktrigger.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
// JpaRepository sayesinde save(), findById(), findAll() gibi metotlar otomatik geliyor
}
