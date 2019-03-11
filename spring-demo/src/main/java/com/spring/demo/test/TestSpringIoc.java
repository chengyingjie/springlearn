package com.spring.demo.test;

import com.alibaba.fastjson.JSON;
import com.spring.core.context.AnnotationApplicationContext;
import com.spring.core.context.ApplicationContext;
import com.spring.core.exception.SpringLearnException;
import com.spring.demo.controller.StudentController;

public class TestSpringIoc {

    public static void main(String[] args) throws SpringLearnException {
        ApplicationContext applicationContext = new AnnotationApplicationContext("com.spring.demo");

        StudentController studentController = applicationContext.getBean(StudentController.class);
        System.out.println(JSON.toJSONString(studentController.getStudentInfo()));
    }
}
