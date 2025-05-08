package com.crina.djoor.shared.http;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomResponse<T>{
    public T data;
    public List<Error> errors;
    public String message;
    public String status;

    public CustomResponse() {
    }
}
