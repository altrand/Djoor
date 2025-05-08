package com.crina.djoor.shared.cqrs;

@FunctionalInterface
public interface CommandHandler<C, R> {
    R handle(C command) throws TransactionalException;
}
