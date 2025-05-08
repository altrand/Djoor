package com.crina.djoor.order.domain.exceptions;


import com.crina.djoor.shared.cqrs.TransactionalException;

public class ErrorOnSaveOrderException extends TransactionalException {
    public ErrorOnSaveOrderException(String message) {
        super(message);
    }
}
