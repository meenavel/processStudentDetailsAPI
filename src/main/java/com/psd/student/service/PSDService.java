package com.psd.student.service;

import com.psd.student.model.ErrorDetails;
import org.hibernate.LazyInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.psd.student.model.APIResponse;
import com.psd.student.model.Student;
import com.psd.student.dao.PSDDAO;
import com.psd.student.exception.PSDException;
import com.psd.student.validation.StudentValidation;

import javax.persistence.EntityNotFoundException;;
import java.util.ArrayList;;
import java.util.List;
import java.util.stream.Collectors;

import static com.psd.student.constants.PSDConstants.*;


@Service
public class PSDService {

    Logger logger = LoggerFactory.getLogger(PSDService.class);

    @Autowired
    StudentValidation studentValidation;

    @Autowired
    PSDDAO psdDAO;

    public ResponseEntity<APIResponse> addStudent(List<Student> studentList) {
        logger.info("Add Student details");
        return upsertStudentDetails(studentList, OP_ADD, HttpStatus.CREATED);
    }

    public ResponseEntity<APIResponse> updateStudentDetails(List<Student> studentList) {
        logger.info("Update Student Details ");
        return upsertStudentDetails(studentList, OP_UPD, HttpStatus.OK);
    }

    private ResponseEntity<APIResponse> upsertStudentDetails(List<Student> studentList, String operation, HttpStatus status){
        APIResponse response = new APIResponse();
        response.setStatus(0);
        response.setOperation(operation);
        try {
            response.setStudentList(psdDAO.saveAll(studentValidation.validateStudentdetails(studentList)));
            if(response.getStudentList().size() != studentList.size()){
                response.setStatus(-1);
                response.setError(new ErrorDetails(100, "Unable to upsert "
                        + (studentList.size() - response.getStudentList().size()) + " Student(s) records"));
                return new ResponseEntity<APIResponse>(response, HttpStatus.PARTIAL_CONTENT);
            }
        }
        catch (PSDException ex) {
            response.setStatus(-1);
            response.setError(ex.getError());
            return new ResponseEntity<APIResponse>(response, HttpStatus.BAD_REQUEST);
        }
        catch (Exception ex) {
            response.setStatus(-1);
            response.setError(new ErrorDetails(500, ex.getMessage()));
            return new ResponseEntity<APIResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //HttpHeaders headerMap = new HttpHeaders();
        //headerMap.add("serviceOperation",operation);
        //return new ResponseEntity<APIResponse>(response, headerMap, status);
        return new ResponseEntity<APIResponse>(response, status);
    }

    public ResponseEntity<APIResponse> getStudentDetailsbyId(int id){
        logger.info("Get Student Details by Id ");
        APIResponse response = new APIResponse();
        response.setOperation(OP_GET);
        response.setStatus(0);
        List<Student> studList = new ArrayList<>();
        try {
            Student student = psdDAO.getById(id);
            if (student.getFirstName() != null)
                studList.add(student);
            response.setStudentList(studList);
            return new ResponseEntity<APIResponse>(response, HttpStatus.OK);
        }
        catch (EntityNotFoundException | LazyInitializationException ex ){
            logger.info("Exception ::: " + ex.getMessage());
            response.setStatus(-1);
            response.setError(new ErrorDetails(100,"Record Not Found"));
            return new ResponseEntity<APIResponse>(response,HttpStatus.NOT_FOUND);
        }
        catch (Exception ex){
            logger.info("Exception ::: " + ex.getMessage());
            response.setStatus(-1);
            response.setError(new ErrorDetails(500,ex.getMessage()));
            return new ResponseEntity<APIResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<APIResponse> getStudentDetailsByIdCollection(List<Integer> idList){
        logger.info("Get Student Details By Id List" + idList);
        APIResponse response = new APIResponse();
        response.setOperation(OP_GET);
        response.setStatus(0);
        if(idList.size()==0){
            response.setStatus(-1);
            response.setError(new ErrorDetails(100, "ID List is Empty"));
            return new ResponseEntity<APIResponse>(response,HttpStatus.BAD_REQUEST);
        }
        try {
            List<Student> studList = psdDAO.getByIDCollection(idList);
            List<Integer> idNFList = new ArrayList<Integer>();
            idNFList = idList
                    .stream()
                    .filter(id -> studList.stream().noneMatch(s -> id.equals(s.getStudId())))
                    .collect(Collectors.toList());
            response.setStudentList(studList);
            if (idNFList.size() > 0) {
                response.setStatus(-1);
                response.setError(new ErrorDetails(100, "No Record found for " + idNFList));
                return new ResponseEntity<APIResponse>(response, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<APIResponse>(response, HttpStatus.OK);
        }
        catch (Exception ex){
            logger.info("Exception ::: " + ex.getMessage());
            response.setStatus(-1);
            response.setError(new ErrorDetails(500, ex.getMessage()));
            return new ResponseEntity<APIResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<APIResponse> getAllStudentDetails(){
        logger.info("Get All Student Details");
        APIResponse response = new APIResponse();
        response.setStatus(0);
        response.setOperation(OP_GET);
        try {
            List<Student> studentList = psdDAO.findAll();
            return loadStudentDetails(studentList, response);
        }
        catch(PSDException ex){
            response.setStatus(-1);
            response.setError(ex.getError());
            return new ResponseEntity<APIResponse>(response, HttpStatus.NOT_FOUND);
        }
        catch (Exception ex) {
            response.setStatus(-1);
            response.setError(new ErrorDetails(500, ex.getMessage()));
            return new ResponseEntity<APIResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<APIResponse> getStudentDetailsByFirstName(String firstName){
        logger.info("Get Student Details by First Name");
        APIResponse response = new APIResponse();
        response.setStatus(0);
        response.setOperation(OP_GET);
        try {
            List<Student> studentList = psdDAO.getByFirstName(firstName);
            return loadStudentDetails(studentList, response);
        }
        catch (PSDException ex)
        {
            response.setStatus(-1);
            response.setError(ex.getError());
            return new ResponseEntity<APIResponse>(response, HttpStatus.NOT_FOUND);
        }
        catch (Exception ex) {
            response.setStatus(-1);
            response.setError(new ErrorDetails(500, ex.getMessage()));
            return new ResponseEntity<APIResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<APIResponse> getStudentDetailsByLastName(String lastName){
        logger.info("Get Student Details by Last Name");
        APIResponse response = new APIResponse();
        response.setStatus(0);
        response.setOperation(OP_GET);
        try {
            List<Student> studentList = psdDAO.getByLastName(lastName);
            return loadStudentDetails(studentList, response);
        }
        catch (PSDException ex) {
            response.setStatus(-1);
            response.setError(ex.getError());
            return new ResponseEntity<APIResponse>(response, HttpStatus.NOT_FOUND);
        }
        catch (Exception ex) {
            response.setStatus(-1);
            response.setError(new ErrorDetails(500, ex.getMessage()));
            return new ResponseEntity<APIResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<APIResponse> getStudentDetailsBySearchParam(String firstName, String lastName, Integer year){
        logger.info("Get Student Details by Search Param");
        APIResponse response = new APIResponse();
        response.setStatus(0);
        response.setOperation(OP_GET);
        try {
            List<Student> studentList = psdDAO.getBySearchParam(firstName, lastName, year);
            return loadStudentDetails(studentList, response);
        }
        catch (PSDException ex)
        {
            response.setStatus(-1);
            response.setError(ex.getError());
            return new ResponseEntity<APIResponse>(response, HttpStatus.NOT_FOUND);
        }
        catch (Exception ex) {
            response.setStatus(-1);
            response.setError(new ErrorDetails(500, ex.getMessage()));
            return new ResponseEntity<APIResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<APIResponse> loadStudentDetails(List<Student> studentList, APIResponse response) throws PSDException{
        if (studentList.size() == 0) {
            throw new PSDException(100, "No Records Found");
        }
        response.setStudentList(studentList);
        return new ResponseEntity<APIResponse>(response, HttpStatus.OK);
    }

    public ResponseEntity<APIResponse> deleteAllStudentDetails() {
        logger.info("Delete Student Details");
        APIResponse response = new APIResponse();
        response.setStatus(0);
        response.setOperation(OP_REM);
        try{
            psdDAO.deleteAll();
        }
        catch(Exception ex){
            response.setStatus(-1);
            response.setError(new ErrorDetails(500, ex.getMessage()));
            return new ResponseEntity<APIResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<APIResponse> deleteStudentDetailsbyidList(List <Integer> idList){
        logger.info("Delete Student Details");
        APIResponse response = new APIResponse();
        response.setStatus(0);
        response.setOperation(OP_REM);
        if(idList.size() == 0){
            response.setError(new ErrorDetails(100, "ID List Empty"));
            response.setStatus(-1);
            return new ResponseEntity<APIResponse>(response,HttpStatus.BAD_REQUEST);
        }
        try {
            psdDAO.deleteAllById(idList);
            return new ResponseEntity<APIResponse>(response,HttpStatus.OK);
        }
        catch(EmptyResultDataAccessException ex)
        {
            response.setError(new ErrorDetails(100, "ID cannot be Found"));
            response.setStatus(-1);
            return new ResponseEntity<APIResponse>(response,HttpStatus.NOT_FOUND);
        }
        catch (Exception ex) {
            response.setStatus(-1);
            response.setError(new ErrorDetails(500, ex.getMessage()));
            return new ResponseEntity<APIResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
