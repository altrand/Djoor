package com.crina.djoor.shared.cqrs;

import com.crina.djoor.shared.http.GenericResponse;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionTemplate;

public class TransactionalCommandHandler<C, R> implements CommandHandler<C, R> {

    private final CommandHandler<C, R> handler;
    private final TransactionTemplate tx;

    public TransactionalCommandHandler(
            CommandHandler<C, R> handler,
            PlatformTransactionManager txManager
    ) {
        this.handler = handler;
        this.tx = new TransactionTemplate(txManager);
        this.tx.setReadOnly(false);
    }

    @Override
    public R handle(C command) {
        var r = new TransactionResponse();
        var g = new GenericResponse<>();
        try {
            return tx.execute(status -> handler.handle(command));
        } catch (TransactionException e) {
            // TODO sent e.getMessage() to support
            r.message = "Une erreur est survenue lors du traitement de votre requêtte, ressayer plus tard.";
            g.error = r;
        }
        return (R)g;
    }
}
