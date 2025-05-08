package com.crina.djoor.shared.http;


import com.crina.djoor.shared.cqrs.TransactionResponse;

public class GenericResponse<R>{
    public R response;
    public TransactionResponse error;
}
