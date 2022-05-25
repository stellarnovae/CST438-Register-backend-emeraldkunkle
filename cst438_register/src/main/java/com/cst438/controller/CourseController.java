package com.cst438.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cst438.domain.Course;
import com.cst438.domain.CourseDTOG;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;
import com.cst438.service.GradebookService;

@RestController
public class CourseController {
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	
	@Autowired
	CourseRepository courseRepository;
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	GradebookService gradebookService;
	/*
	 * endpoint used by gradebook service to transfer final course grades
	 */
	@PostMapping("/course/{course_id}")
	@Transactional
	public void updateCourseGrades( @RequestBody CourseDTOG courseDTO, @PathVariable("course_id") int course_id) {
		
		//TODO  complete this method in homework 4
		System.out.print("before for loop");
		for(int i = 0; i < courseDTO.grades.size(); i++) {
			String tempEmail = courseDTO.grades.get(i).student_email;
			String tempName = courseDTO.grades.get(i).student_name;
			String tempGrade = courseDTO.grades.get(i).grade;
			
			Student student = studentRepository.findByEmail(tempEmail);
			Course course  = courseRepository.findById(courseDTO.course_id).orElse(null);
			
			System.out.print(course_id);
			
			Enrollment enroll = new Enrollment();
			
			enroll.setStudent(student);
			enroll.setCourse(course);
			enroll.setCourseGrade(tempGrade);
			
			enrollmentRepository.save(enroll);
		}
		
	}

}
