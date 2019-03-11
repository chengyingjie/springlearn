package com.spring.demo.dal;

import com.spring.core.annotation.Repository;
import com.spring.demo.dto.StudentDTO;

@Repository
public class StudentDaoImpl implements StudentDao {

    public StudentDTO getStudentInfo() {

        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setName("studentAAA");
        studentDTO.setWeight(65);
        studentDTO.setHeight(180);

        return studentDTO;
    }
}
