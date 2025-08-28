package com.schoolapp.teacher_manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    @GetMapping({"/", "/dashboard"})
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/teachers")
    public String teachers() {
        return "teachers";
    }
    @GetMapping("/print")
    public String print() {
        return "print";
    }

    @GetMapping("/teachers/view/{id}")
    public String viewTeacher(@PathVariable Long id) {
        return "teachers-view";
    }

    @GetMapping("/teachers/edit/{id}")
    public String editTeacher(@PathVariable Long id) {
        return "teachers-edit";
    }

    @GetMapping("/teachers/add")
    public String addTeacher() {
        return "teachers-add";
    }
}
