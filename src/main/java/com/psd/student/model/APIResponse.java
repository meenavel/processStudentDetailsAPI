package com.psd.student.model;


import lombok.Data;

import java.util.List;

@Data

public class APIResponse {
    // 0 - Success / -1 Error
    private int status;
    private String operation;
    private List<Student> studentList;
    private ErrorDetails error;
}
