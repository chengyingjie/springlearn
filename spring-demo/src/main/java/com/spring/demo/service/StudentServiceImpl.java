package com.spring.demo.service;

import com.spring.core.annotation.Autowired;
import com.spring.core.annotation.Service;
import com.spring.demo.dal.StudentDao;
import com.spring.demo.dto.StudentDTO;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentDao studentDao;


    public StudentDTO getStudentInfo() {
        return studentDao.getStudentInfo();
    }
}
