package com.psd.student.controller;

import com.psd.student.model.APIResponse;
import com.psd.student.model.Student;

import static com.psd.student.constants.PSDConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PSDV1ControllerTests {

    @Autowired
    PSDV1Controller controller;

    @Test
    @Order(1)
    public void testAddStudent() {
        List<Student> studentList = new ArrayList<>();
        Student student = new Student();
        student.setFirstName("Meena");
        student.setLastName("Siva");
        student.setYear(1);
        student.setDob("25/06/1991");
        student.setDoj("08/12/2021");
        studentList.add(student);
        ResponseEntity<APIResponse> response = controller.addStudentDetails(studentList);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(0, response.getBody().getStatus());
        assertEquals(OP_ADD, response.getBody().getOperation());
        assertEquals(1, response.getBody().getStudentList().size());
        assertEquals(null, response.getBody().getError());
        assertEquals("Meena", response.getBody().getStudentList().get(0).getFirstName());
    }

    @Test
    @Order(2)
    public void testGetStudentById() {
        int id = 1000;
        ResponseEntity<APIResponse> response = controller.getStudentDetailsById(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().getStatus());
        assertEquals(OP_GET, response.getBody().getOperation());
        assertEquals(1, response.getBody().getStudentList().size());
        assertEquals(null, response.getBody().getError());
        assertEquals("Karthik", response.getBody().getStudentList().get(0).getLastName());
    }


    @Test
    @Order(3)
    public void testGetStudentDetailsByIdListCollection() {
        List<Integer> idList = new ArrayList<>();
        idList.add(1000);
        ResponseEntity<APIResponse> response = controller.getStudentDetailsByIdCollection(idList);
        assertEquals(1, response.getBody().getStudentList().size());
        assertEquals(0, response.getBody().getStatus());
        assertEquals(OP_GET, response.getBody().getOperation());
        assertEquals(null, response.getBody().getError());
        assertEquals("12/10/2021", response.getBody().getStudentList().get(0).getDob());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(4)
    public void testUpdateStudentDetails() {
        List<Student> studentList = new ArrayList<>();
        Student student = new Student();
        student.setFirstName("Meena");
        student.setLastName("Siva");
        student.setYear(1);
        student.setDob("25/06/1991");
        student.setDoj("25/12/2021");
        studentList.add(student);
        ResponseEntity<APIResponse> response = controller.updateStudentDetails(studentList);
        assertEquals(0, response.getBody().getStatus());
        assertEquals(OP_UPD, response.getBody().getOperation());
        assertEquals("25/12/2021", response.getBody().getStudentList().get(0).getDoj());
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    @Order(5)
    public void testDeleteStudentbyId() {
        List<Integer> idList = new ArrayList<>();
        idList.add(1000);
        ResponseEntity<APIResponse> response = controller.deleteStudentDetails(idList);
        assertEquals(0, response.getBody().getStatus());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(OP_REM, response.getBody().getOperation());
    }

    @Test
    @Order(6)
    public void testGetAllStudentDetails() {
        ResponseEntity<APIResponse> response = controller.getAllStudentDetails();
        assertEquals(0, response.getBody().getStatus());
        assertEquals(OP_GET, response.getBody().getOperation());
        assertEquals("08/12/2021", response.getBody().getStudentList().get(0).getDoj());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(7)
    public void testGetStudentDetailsbyFirstName() {
        String firstName = "Meena";
        ResponseEntity<APIResponse> response = controller.getStudentDetailsByFirstName(firstName);
        assertEquals(0, response.getBody().getStatus());
        assertEquals(OP_GET, response.getBody().getOperation());
        assertEquals("Siva", response.getBody().getStudentList().get(0).getLastName());
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    @Order(8)
    public void testGetStudentDetailsbyLastName() {
        String lastName = "Siva";
        ResponseEntity<APIResponse> response = controller.getStudentDetailsByLastName(lastName);
        assertEquals(0, response.getBody().getStatus());
        assertEquals(OP_GET, response.getBody().getOperation());
        assertEquals("Meena", response.getBody().getStudentList().get(0).getFirstName());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(9)
    public void testGetStudentDetailsBySearchParam(){
        String firstName = "Meena";
        String lastName = "Siva";
        int year = 1;
        ResponseEntity<APIResponse> response = controller.getStudentDetailsBySearchParam(firstName,lastName,year);
        assertEquals(0, response.getBody().getStatus());
        assertEquals(OP_GET, response.getBody().getOperation());
        assertEquals("25/06/1991", response.getBody().getStudentList().get(0).getDob());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(10)
    public void testDeleteAllStudents(){
        ResponseEntity<APIResponse> response = controller.deleteAllStudentDetails();
        assertEquals(0, response.getBody().getStatus());
        assertEquals(OP_REM, response.getBody().getOperation());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}