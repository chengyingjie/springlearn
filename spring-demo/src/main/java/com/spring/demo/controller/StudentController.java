package com.spring.demo.controller;

import com.spring.core.annotation.Autowired;
import com.spring.core.annotation.Controller;
import com.spring.demo.dto.StudentDTO;
import com.spring.demo.service.StudentService;

@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;


    public StudentDTO getStudentInfo() {
        return studentService.getStudentInfo();
    }
}
