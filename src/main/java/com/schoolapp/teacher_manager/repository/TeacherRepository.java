package com.schoolapp.teacher_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schoolapp.teacher_manager.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    
}
