package com.psd.student.validation;


import com.psd.student.exception.PSDException;
import com.psd.student.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static com.psd.student.constants.PSDConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class StudentValidationTests {

    @Autowired
    StudentValidation validation;

    @Test
    public void testEmptyStudent(){
        List<Student> studentList = new ArrayList<>();
        Student student = new Student();
        studentList.add(student);
        try{
        List<Student> validateStudentList = validation.validateStudentdetails(studentList);
        String result = validation.validateStudent(student);
        assertEquals("  First Name,  Last Name,  Year,  DOB,  DOJ", result);
        }
        catch (PSDException ex){
            System.out.println(ex);
            assertEquals(100, ex.getError().getCode());
            assertEquals("Invalid Student Record. Following attributes have issues -" +
                    "  First Name,  Last Name,  Year,  DOB,  DOJ", ex.getError().getDescription());

        }
    }

    @Test
    public void testEmptyLastNameStudent(){
        List<Student> studentList = new ArrayList<>();
        Student student = new Student();
        student.setFirstName("Meena");
        student.setYear(10);
        student.setDob("25/06/2000");
        student.setDoj("17/12/2020");
        studentList.add(student);
        try{
            List<Student> validateStudentList = validation.validateStudentdetails(studentList);
            String result = validation.validateStudent(student);
            assertEquals("Last Name,", result);
        }
        catch (PSDException ex){
            assertEquals(100, ex.getError().getCode());
            assertEquals("Invalid Student Record. Following attributes have issues -" +
                    "  Last Name,", ex.getError().getDescription());

        }
    }

    @Test
    public void testInvalidLastNameStudent() {
        List<Student> studentList = new ArrayList<>();
        Student student = new Student();
        student.setFirstName("Meena");
        student.setLastName("siva");
        student.setYear(10);
        student.setDob("25/06/2000");
        student.setDoj("17/12/2020");
        studentList.add(student);
        try {
            List<Student> validateStudentList = validation.validateStudentdetails(studentList);
            String result = validation.validateStudent(student);
            assertEquals("Last Name,", result);
        } catch (PSDException ex) {
            assertEquals(100, ex.getError().getCode());
            assertEquals("Invalid Student Record. Following attributes have issues -" +
                    "  Last Name,", ex.getError().getDescription());
        }
    }

    @Test
    public void testInvalidDOBStudent() {
        List<Student> studentList = new ArrayList<>();
        Student student = new Student();
        student.setFirstName("Meena");
        student.setLastName("Siva");
        student.setYear(10);
        student.setDob("25/06/2XXX");
        student.setDoj("17/12/2020");
        studentList.add(student);
        try {
            List<Student> validateStudentList = validation.validateStudentdetails(studentList);
            String result = validation.validateStudent(student);
            assertEquals("DOB,", result);
        } catch (PSDException ex) {
            assertEquals(100, ex.getError().getCode());
            assertEquals("Invalid Student Record. Following attributes have issues -" +
                    "  DOB,", ex.getError().getDescription());
        }
    }

    @Test
    public void testInvalidDOJStudent() {
        List<Student> studentList = new ArrayList<>();
        Student student = new Student();
        student.setFirstName("Meena");
        student.setLastName("Siva");
        student.setYear(10);
        student.setDob("25/06/2000");
        student.setDoj("17/12/202Y");
        studentList.add(student);
        try {
            List<Student> validateStudentList = validation.validateStudentdetails(studentList);
            String result = validation.validateStudent(student);
            assertEquals("DOJ,", result);
        } catch (PSDException ex) {
            assertEquals(100, ex.getError().getCode());
            assertEquals("Invalid Student Record. Following attributes have issues -" +
                    "  DOJ", ex.getError().getDescription());
        }
    }

    @Test
    public void testInvalidStudentList() {
        List<Student> studentList = new ArrayList<>();
        Student student = new Student();
        student.setFirstName("Meena");
        student.setLastName("Siva");
        student.setYear(10);
        student.setDob("25/06/2000");
        student.setDoj("17/12/2020");
        studentList.add(student);
        Student studentInvalid = new Student();
        studentInvalid.setFirstName("Meena");
        studentInvalid.setLastName("Siva");
        studentInvalid.setYear(10);
        studentInvalid.setDob("25/06/2YYY");
        studentInvalid.setDoj("17/12/2020");
        studentList.add(studentInvalid);
            try {
                List<Student> validateStudentList = validation.validateStudentdetails(studentList);
                String result = validation.validateStudent(student);
                assertEquals(1, validateStudentList.size());
            }
            catch (PSDException ex){}
     }
}