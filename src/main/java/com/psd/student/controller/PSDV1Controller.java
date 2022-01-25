package com.psd.student.controller;

import com.psd.student.service.PSDService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.psd.student.model.APIResponse;
import com.psd.student.model.Student;

import java.util.List;

@RestController
@RequestMapping("/psd/v1")
public class PSDV1Controller {

    Logger logger = LoggerFactory.getLogger(PSDV1Controller.class);

    @Autowired
    PSDService psdService;

    //Add Student details
    @PostMapping(path = "/addStudent", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<APIResponse> addStudentDetails(@RequestBody List<Student> studentList){
        logger.info("Add Students details");
        return psdService.addStudent(studentList);
    }

    // Update Student Details by multiple ids
    @PutMapping(path="/updateStudent")
    public ResponseEntity<APIResponse> updateStudentDetails(@RequestBody List<Student> studentList){
        logger.info("Update Student Details");
        return psdService.updateStudentDetails(studentList);
    }

    //Deleting All Student Details
    @DeleteMapping(path = "/deleteAllStudent")
    public ResponseEntity<APIResponse> deleteAllStudentDetails(){
        logger.info("Delete All Student Details");
        return psdService.deleteAllStudentDetails();
    }

    //Delete Student Details by single or multiple ids
    @DeleteMapping(path = "/deleteStudentByIDCollection")
    public  ResponseEntity<APIResponse> deleteStudentDetails(@RequestBody List<Integer> idList){
        logger.info("Delete Student Details");
        return psdService.deleteStudentDetailsbyidList(idList);
    }

    //Get All Student details
    @GetMapping(path = "/getAllStudent")
    public ResponseEntity<APIResponse> getAllStudentDetails(){
        logger.info("Get all Student Details");
        return psdService.getAllStudentDetails();
    }

    //Get Student(Single id)
    @GetMapping(path = "/getStudent/{id}")
    public ResponseEntity<APIResponse> getStudentDetailsById(@PathVariable ("id") int id){
        logger.info("Get Student details");
        return psdService.getStudentDetailsbyId(id);
    }

    //Get Student Details by multiple ids using SQL Query
    @PostMapping(path = "/getStudentByIdCollection", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<APIResponse> getStudentDetailsByIdCollection(@RequestBody List<Integer> idList){
        logger.info("Get Student List By Id");
        return psdService.getStudentDetailsByIdCollection(idList);
    }

    //Get Student details by First Name
    @GetMapping(path = "/getStudentByFirstName/{firstName}")
    public ResponseEntity<APIResponse> getStudentDetailsByFirstName(@PathVariable ("firstName") String firstName){
        logger.info("Get Student Details by First Name");
        return psdService.getStudentDetailsByFirstName(firstName);
    }

    //Get Student details by Last Name
    @GetMapping(path = "/getStudentByLastName/{lastName}")
    public ResponseEntity<APIResponse> getStudentDetailsByLastName(@PathVariable ("lastName") String lastName){
        logger.info("Get Student Details by Last Name");
        return psdService.getStudentDetailsByLastName(lastName);
    }

    //Get Student details by Search Param - First Name or Last Name or Year or all 3 params
    @GetMapping(path = "/getStudentBySearchParam")
    public ResponseEntity<APIResponse> getStudentDetailsBySearchParam(
            @RequestParam (required = false, name="firstName") String firstName,
            @RequestParam (required = false, name="lastName") String lastName,
            @RequestParam (required = false, name="year") Integer year
            ){
        logger.info("Get all Student Details");
        return psdService.getStudentDetailsBySearchParam(firstName, lastName, year);
    }
}
