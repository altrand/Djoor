package com.crina.djoor.shared.cqrs;

import org.springframework.transaction.TransactionException;

public class TransactionalException extends TransactionException {
    public TransactionalException(String message) {
        super(message);
    }
}
