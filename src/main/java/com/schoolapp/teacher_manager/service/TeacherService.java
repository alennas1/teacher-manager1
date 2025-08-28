package com.schoolapp.teacher_manager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.schoolapp.teacher_manager.model.Teacher;
import com.schoolapp.teacher_manager.repository.TeacherRepository;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }
    

    public void saveTeacher(Teacher teacher) {
        teacherRepository.save(teacher);
    }

    public Teacher getTeacherById(Long id) {
        Optional<Teacher> optional = teacherRepository.findById(id);
        return optional.orElseThrow(() -> new RuntimeException("Teacher not found"));
    }

    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }
 public void deleteAllById(List<Long> ids) {
        teacherRepository.deleteAllById(ids);
    }
    //find by id
    public Teacher findById(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));
    }

   
}
