package com.crina.djoor.order.application.command.findtopselling;

import java.util.Date;

public class FindTopSellingProductsCommand {
    public Date from;
    public Date to;

    public FindTopSellingProductsCommand(Date from, Date to) {
        this.from = from;
        this.to = to;
    }
}
