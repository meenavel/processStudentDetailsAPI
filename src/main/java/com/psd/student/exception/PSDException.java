package com.psd.student.exception;


import com.psd.student.model.ErrorDetails;
import lombok.Getter;

@Getter
public class PSDException extends Exception{

    private ErrorDetails error;
    public PSDException(int code, String description){
            error= new ErrorDetails(code, description);
    }

}
