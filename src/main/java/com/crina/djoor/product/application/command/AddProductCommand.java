package com.crina.djoor.product.application.command;

public class AddProductCommand {
    public String userId;
    public int quantity;
    public String productId;
    public String commandId;


    private AddProductCommand() {}

    public AddProductCommand(
            String userId,
            String productId,
            int quantity
    ) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
   }
}
