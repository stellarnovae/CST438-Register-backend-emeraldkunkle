package com.cst438.service;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.cst438.domain.Course;
import com.cst438.domain.CourseDTOG;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;


public class GradebookServiceMQ extends GradebookService {
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	
	@Autowired
	StudentRepository studentRepository; 
	
	@Autowired
	CourseRepository courseRepository;
	
	@Autowired
	Queue gradebookQueue;
	
	
	public GradebookServiceMQ() {
		System.out.println("MQ grade book service");
	}
	
	// send message to grade book service about new student enrollment in course
	@Override
	public void enrollStudent(String student_email, String student_name, int course_id) {
		
		EnrollmentDTO enroll = new EnrollmentDTO(student_name, student_email, course_id);
		
		
		
		System.out.println("Sending rabbitmq message: " + enroll);
        rabbitTemplate.convertAndSend(gradebookQueue.getName(),  enroll);
        System.out.println("Message sent...");
		//TODO  complete this method in homework 4
		
	}
	
	@RabbitListener(queues = "registration-queue")
	@Transactional
	public void receive(CourseDTOG courseDTOG) {
		
		//TODO  complete this method in homework 4
		System.out.print("before for loop");
		for(int i = 0; i < courseDTOG.grades.size(); i++) {
			String tempEmail = courseDTOG.grades.get(i).student_email;
			String tempName = courseDTOG.grades.get(i).student_name;
			String tempGrade = courseDTOG.grades.get(i).grade;
			
			Student student = studentRepository.findByEmail(tempEmail);
			Course course  = courseRepository.findById(courseDTOG.course_id).orElse(null);
			
			
			Enrollment enroll = new Enrollment();
			
			enroll.setStudent(student);
			enroll.setCourse(course);
			enroll.setCourseGrade(tempGrade);
			
			enrollmentRepository.save(enroll);
		}
	}
	
	

}
