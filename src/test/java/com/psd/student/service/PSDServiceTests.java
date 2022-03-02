package com.psd.student.service;

import com.psd.student.dao.PSDDAO;
import com.psd.student.exception.PSDException;
import com.psd.student.model.APIResponse;
import com.psd.student.model.Student;
import com.psd.student.validation.StudentValidation;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.psd.student.constants.PSDConstants.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class PSDServiceTests {

    @Autowired
    PSDService service;

    @InjectMocks
    PSDService serviceIM;

    @Mock
    PSDDAO dao;

    @Mock
    StudentValidation validation;

    @Test
    public void testAddEmptyStudent() {
        List<Student> studentList = new ArrayList<>();
        ResponseEntity<APIResponse> response = service.addStudent(studentList);
        assertEquals(100, response.getBody().getError().getCode());
        assertEquals("Student Details cannot be empty", response.getBody().getError().getDescription());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(-1, response.getBody().getStatus());
        assertEquals(OP_ADD, response.getBody().getOperation());
        assertEquals(null, response.getBody().getStudentList());
    }
    @Test
    public void testAddStudentIncorrect(){
        List<Student> studentList = new ArrayList<>();
        Student student = new Student();
        student.setFirstName("Meena");
        student.setLastName("Siva");
        student.setYear(10);
        student.setDob("25/06/1991");
        student.setDoj("13/08/2016");
        studentList.add(student);
        Student studentIncorrect = new Student();
        studentIncorrect.setFirstName("meena");
        studentIncorrect.setLastName("Siva");
        studentIncorrect.setYear(10);
        studentIncorrect.setDob("25/06/1991");
        studentIncorrect.setDoj("13/08/2016");
        studentList.add(studentIncorrect);
        ResponseEntity<APIResponse> response = service.addStudent(studentList);
        assertEquals(100, response.getBody().getError().getCode());
        assertEquals("Unable to upsert " + 1 + " Student(s) records", response.getBody().getError().getDescription());
        assertEquals(HttpStatus.PARTIAL_CONTENT, response.getStatusCode());
        assertEquals(-1, response.getBody().getStatus());
        assertEquals(OP_ADD, response.getBody().getOperation());
        assertEquals(10, response.getBody().getStudentList().get(0).getYear());
    }



    @Test
    public void testAddStudentDBError() throws PSDException {
        List<Student> studentList = new ArrayList<>();
        Mockito.when(validation.validateStudentdetails(studentList)).thenReturn(studentList);
        Mockito.when(dao.saveAll(studentList)).thenThrow(new IllegalArgumentException("Table doesn't exist"));
        ResponseEntity<APIResponse> response = serviceIM.addStudent(studentList);
        assertEquals(500, response.getBody().getError().getCode());
        assertEquals("Table doesn't exist", response.getBody().getError().getDescription());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(-1, response.getBody().getStatus());
        assertEquals(OP_ADD, response.getBody().getOperation());
        assertEquals(null, response.getBody().getStudentList());
    }

    @Test
    public void testgetStudentByIdEmpty() {
        int id = 0;
        ResponseEntity<APIResponse> response = service.getStudentDetailsbyId(id);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(-1, response.getBody().getStatus());
        assertEquals(OP_GET, response.getBody().getOperation());
        assertEquals(null, response.getBody().getStudentList());
        assertEquals(100, response.getBody().getError().getCode());
        assertEquals("Record Not Found", response.getBody().getError().getDescription());
    }
    @Test
    public void testgetStudentByIdDBError(){
        int id = 0;
        Mockito.when(dao.getById(id)).thenThrow(new IllegalArgumentException("Table Doesn't Exist"));
        ResponseEntity<APIResponse> response = serviceIM.getStudentDetailsbyId(id);
        assertEquals(-1, response.getBody().getStatus());
        assertEquals(OP_GET,response.getBody().getOperation());
        assertEquals(null,response.getBody().getStudentList());
        assertEquals(500, response.getBody().getError().getCode());
        assertEquals("Table Doesn't Exist",response.getBody().getError().getDescription());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
    @Test
    public void testgetStudentByIdCollectionListSize0(){
        List<Integer> idList = new ArrayList<>();
        ResponseEntity<APIResponse> response = service.getStudentDetailsByIdCollection(idList);
        assertEquals(-1,response.getBody().getStatus());
        assertEquals(OP_GET,response.getBody().getOperation());
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        assertEquals(null,response.getBody().getStudentList());
        assertEquals(100,response.getBody().getError().getCode());
        assertEquals("ID List is Empty", response.getBody().getError().getDescription());
    }

    @Test
    public void testgetStudentDetailsByIdCollectionEmpty(){
        List<Integer> idList = new ArrayList<>();
        idList.add(9);
        ResponseEntity<APIResponse> response = service.getStudentDetailsByIdCollection(idList);
        assertEquals(-1,response.getBody().getStatus());
        assertEquals(OP_GET,response.getBody().getOperation());
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertEquals(100,response.getBody().getError().getCode());
    }

    @Test
    public void testgetStudentByIdCollectionDBError(){
        List<Integer> idList = new ArrayList<>();
        idList.add(10);
        Mockito.when(dao.getByIDCollection(idList)).thenThrow(new IllegalArgumentException("Table Doesn't Exist"));
        ResponseEntity<APIResponse> response = serviceIM.getStudentDetailsByIdCollection(idList);
        assertEquals(-1,response.getBody().getStatus());
        assertEquals(OP_GET,response.getBody().getOperation());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
        assertEquals(null,response.getBody().getStudentList());
        assertEquals(500,response.getBody().getError().getCode());
        assertEquals("Table Doesn't Exist", response.getBody().getError().getDescription());
    }


    @Test
    public  void testupdateStudentDetailsInvalid(){
        List<Student> studentList = new ArrayList<>();
        ResponseEntity<APIResponse> response = service.updateStudentDetails(studentList);
        assertEquals(100, response.getBody().getError().getCode());
        assertEquals("Student Details cannot be empty", response.getBody().getError().getDescription());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(-1, response.getBody().getStatus());
        assertEquals(OP_UPD, response.getBody().getOperation());
        assertEquals(null, response.getBody().getStudentList());
    }

    @Test
    public void testUpdateStudentDetailsIncorrect(){
        List<Student> studentList = new ArrayList<>();
        Student student = new Student();
        student.setStudId(11);
        student.setFirstName("Meena");
        student.setLastName("Siva");
        student.setYear(10);
        student.setDob("25/06/1991");
        student.setDoj("13/08/2016");
        studentList.add(student);
        Student studentIncorrect = new Student();
        student.setStudId(12);
        studentIncorrect.setFirstName("meena");
        studentIncorrect.setLastName("Siva");
        studentIncorrect.setYear(10);
        studentIncorrect.setDob("25/06/1991");
        studentIncorrect.setDoj("13/08/2016");
        studentList.add(studentIncorrect);
        ResponseEntity<APIResponse> response = service.updateStudentDetails(studentList);
        assertEquals(100, response.getBody().getError().getCode());
        assertEquals("Unable to upsert " + 1 + " Student(s) records", response.getBody().getError().getDescription());
        assertEquals(HttpStatus.PARTIAL_CONTENT, response.getStatusCode());
        assertEquals(-1, response.getBody().getStatus());
        assertEquals(OP_UPD, response.getBody().getOperation());
        assertEquals(10, response.getBody().getStudentList().get(0).getYear());
    }

    @Test
    public void testupdateStudentDetailsDBError() throws PSDException{
        List<Student> studentList = new ArrayList<>();
        Mockito.when(validation.validateStudentdetails(studentList)).thenReturn(studentList);
        Mockito.when(dao.saveAll(studentList)).thenThrow(new IllegalArgumentException("Table Doesn't Exist"));
        ResponseEntity<APIResponse> response = serviceIM.updateStudentDetails(studentList);
        assertEquals(500, response.getBody().getError().getCode());
        assertEquals(-1, response.getBody().getStatus());
        assertEquals(OP_UPD, response.getBody().getOperation());
        assertEquals("Table Doesn't Exist",response.getBody().getError().getDescription());
        assertEquals(null, response.getBody().getStudentList());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testdeleteStudentByIdnotFound(){
        List<Integer> idList = new ArrayList<>();
        idList.add(9);
        ResponseEntity<APIResponse> response = service.deleteStudentDetailsbyidList(idList);
        assertEquals(-1, response.getBody().getStatus());
        assertEquals(OP_REM , response.getBody().getOperation());
        assertEquals(100, response.getBody().getError().getCode());
        assertEquals("ID cannot be Found" ,response.getBody().getError().getDescription());
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());

    }

    @Test
    public void testDeleteStudentByIdListEmpty(){
        List<Integer> idList = new ArrayList<>();
        ResponseEntity<APIResponse> response = service.deleteStudentDetailsbyidList(idList);
        assertEquals(-1,response.getBody().getStatus());
        assertEquals(OP_REM , response.getBody().getOperation());
        assertEquals(100, response.getBody().getError().getCode());
        assertEquals("ID List Empty",response.getBody().getError().getDescription());
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
    }
    @Test
    public void testDeleteStudentByIdListDBError(){
        List<Integer> idList = new ArrayList<>();
        idList.add(10);
        Mockito.when(serviceIM.deleteStudentDetailsbyidList(idList)).thenThrow(new IllegalArgumentException("Table Doesn't Exist"));
        ResponseEntity<APIResponse> response = serviceIM.deleteStudentDetailsbyidList(idList);
        assertEquals(-1,response.getBody().getStatus());
        assertEquals(OP_REM,response.getBody().getOperation());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
        assertEquals(null,response.getBody().getStudentList());
        assertEquals(500,response.getBody().getError().getCode());
        assertEquals("Table Doesn't Exist", response.getBody().getError().getDescription());
    }

   @Test
    public void testDeleteAllStudentDetailsDBError(){
        List<Integer> idList = new ArrayList<>();
        idList.add(10);
        Mockito.when(serviceIM.deleteAllStudentDetails()).thenThrow(new IllegalArgumentException("Table Doesn't Exist"));
        ResponseEntity<APIResponse> response = serviceIM.deleteAllStudentDetails();
        assertEquals(-1,response.getBody().getStatus());
        assertEquals(OP_REM,response.getBody().getOperation());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
        assertEquals(null,response.getBody().getStudentList());
        assertEquals(500,response.getBody().getError().getCode());
        assertEquals("Table Doesn't Exist", response.getBody().getError().getDescription());
    }
    @Test
    public void testgetAllStudentDetailsNoRecordsFound(){
        ResponseEntity<APIResponse> response = serviceIM.getAllStudentDetails();
        assertEquals(-1,response.getBody().getStatus());
        assertEquals(OP_GET , response.getBody().getOperation());
        assertEquals(100, response.getBody().getError().getCode());
        assertEquals("No Records Found",response.getBody().getError().getDescription());
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testgetAllStudentDetailsDBError(){
        Mockito.when(dao.findAll()).thenThrow(new IllegalArgumentException("Table Doesn't Exist"));
        ResponseEntity<APIResponse> response = serviceIM.getAllStudentDetails();
        assertEquals(500, response.getBody().getError().getCode());
        assertEquals(-1, response.getBody().getStatus());
        assertEquals(OP_GET, response.getBody().getOperation());
        assertEquals("Table Doesn't Exist",response.getBody().getError().getDescription());
        assertEquals(null, response.getBody().getStudentList());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());

    }

    @Test
    public void testgetByFirstNameEmpty(){
        ResponseEntity<APIResponse> response = service.getStudentDetailsByFirstName("Rakshaaaa");
        assertEquals(-1, response.getBody().getStatus());
        assertEquals(OP_GET, response.getBody().getOperation());
        assertEquals(null, response.getBody().getStudentList());
        assertEquals(100,response.getBody().getError().getCode());
        assertEquals("No Records Found",response.getBody().getError().getDescription());
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }
    @Test
    public void testgetByFirstNameDBError(){
        Mockito.when(dao.getByFirstName("Karthik")).thenThrow(new IllegalArgumentException("Table Doesn't Exist"));
        ResponseEntity<APIResponse> response = serviceIM.getStudentDetailsByFirstName("Karthik");
        assertEquals(-1, response.getBody().getStatus());
        assertEquals(OP_GET, response.getBody().getOperation());
        assertEquals(null, response.getBody().getStudentList());
        assertEquals(500,response.getBody().getError().getCode());
        assertEquals("Table Doesn't Exist",response.getBody().getError().getDescription());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());

    }

    @Test
    public void testgetByLastNameDBError(){
        Mockito.when(dao.getByLastName("Raksha")).thenThrow(new IllegalArgumentException("Table Doesn't Exist"));
        ResponseEntity<APIResponse> response = serviceIM.getStudentDetailsByLastName("Raksha");
        assertEquals(-1, response.getBody().getStatus());
        assertEquals(OP_GET, response.getBody().getOperation());
        assertEquals(null, response.getBody().getStudentList());
        assertEquals(500,response.getBody().getError().getCode());
        assertEquals("Table Doesn't Exist",response.getBody().getError().getDescription());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());

    }

    @Test
    public void testgetByLastNameEmpty(){
        ResponseEntity<APIResponse> response = service.getStudentDetailsByLastName("");
        assertEquals(-1, response.getBody().getStatus());
        assertEquals(OP_GET, response.getBody().getOperation());
        assertEquals(null, response.getBody().getStudentList());
        assertEquals(100,response.getBody().getError().getCode());
        assertEquals("No Records Found",response.getBody().getError().getDescription());
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }
    @Test
    public void testGetStudentDetailsBySearchParamFail(){
        ResponseEntity<APIResponse> response = service.getStudentDetailsBySearchParam("Rakshaaa","",0);
        assertEquals(-1, response.getBody().getStatus());
        assertEquals(OP_GET, response.getBody().getOperation());
        assertEquals(null, response.getBody().getStudentList());
        assertEquals(100,response.getBody().getError().getCode());
        assertEquals("No Records Found",response.getBody().getError().getDescription());
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetStudentDetailsBySearchparamDBError(){
        Mockito.when(dao.getBySearchParam("Raksha","Karthik",10)).thenThrow(new IllegalArgumentException("Table Doesn't Exist"));
        ResponseEntity<APIResponse> response = serviceIM.getStudentDetailsBySearchParam("Raksha","Karthik",10);
        assertEquals(-1, response.getBody().getStatus());
        assertEquals(OP_GET, response.getBody().getOperation());
        assertEquals(null, response.getBody().getStudentList());
        assertEquals(500,response.getBody().getError().getCode());
        assertEquals("Table Doesn't Exist",response.getBody().getError().getDescription());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }


}