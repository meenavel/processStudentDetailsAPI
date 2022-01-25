package com.psd.student.validation;


import com.psd.student.model.Student;
import com.psd.student.constants.PSDConstants;
import com.psd.student.exception.PSDException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StudentValidation {

    public List<Student> validateStudentdetails(List<Student> studentList) throws PSDException {

        Logger logger = LoggerFactory.getLogger(StudentValidation.class);
        logger.info("Validation entered");

        if (studentList.size() < 1) {
            throw new PSDException(100, "Student Details cannot be empty");
        }
        if(studentList.size() == 1){
            String result = validateStudent(studentList.get(0));
            if(StringUtils.isNotEmpty(result)){
                throw new PSDException(100, "Invalid Student Record. Following attributes have issues -"+result);
            }
            else{
                return studentList;
            }
        }
        List<Student> validStudent = new ArrayList<>();
        for (Student student : studentList) {
            if(StringUtils.isEmpty(validateStudent(student)))
                validStudent.add(student);
        }
        return validStudent;
    }

    public String validateStudent(Student student){

        StringBuilder sb = new StringBuilder("");

        if (StringUtils.isEmpty(student.getFirstName())
                || !student.getFirstName().matches(PSDConstants.NAME_REGEX))
            sb.append("  First Name,");
        if (StringUtils.isEmpty(student.getLastName())
                || !student.getLastName().matches(PSDConstants.NAME_REGEX))
            sb.append("  Last Name,");
        if (student.getYear() < 1
                || student.getYear() > 12)
            sb.append("  Year,");
        if (StringUtils.isEmpty(student.getDob())
                || !student.getDob().matches(PSDConstants.DATE_REGEX))
            sb.append("  DOB,");
        if (StringUtils.isEmpty(student.getDoj())
                || !student.getDoj().matches(PSDConstants.DATE_REGEX))
            sb.append("  DOJ");

        return sb.toString();
    }
}

