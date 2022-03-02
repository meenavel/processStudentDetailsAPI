package com.psd.student.validation;


import com.psd.student.model.Student;
import com.psd.student.constants.PSDConstants;
import com.psd.student.exception.PSDException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.psd.student.constants.PSDConstants.DATE_REGEX;

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
            logger.info("Valid Student " + validStudent);
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
        if(dateValidation(student.getDob()))
            sb.append("  Dob," );

        if(dateValidation(student.getDoj()))
            sb.append("  doj");

//        if (StringUtils.isEmpty(student.getDob())
//                || !student.getDob().matches(PSDConstants.DATE_REGEX))
//            sb.append("  DOB,");
//        if (StringUtils.isEmpty(student.getDoj())
//                || !student.getDoj().matches(PSDConstants.DATE_REGEX))
//            sb.append("  DOJ");

        return sb.toString();
    }

    private boolean dateValidation(String dob) {
        boolean status = false;
        if (checkDate(dob)) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateFormat.setLenient(false);
            try {
                dateFormat.parse(dob);

            } catch ( ParseException e) {
                status = true;
                e.printStackTrace();
            }
        }
        return status;
    }
    static boolean checkDate(String date) {
        String pattern = DATE_REGEX ;                  //"(0?[1-9]|[12][0-9]|3[01])\\/(0?[1-9]|1[0-2])\\/([0-9]{4})";
        boolean flag = false;
        if (date.matches(pattern)) {
            flag = true;
        }
        return flag;
    }
    }



