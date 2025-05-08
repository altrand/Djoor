package com.crina.djoor.order.application.command.create;

public class OrderProductCommand {

    public int nbOfProduct;
    public String productId;

    public OrderProductCommand(String productId,int nbOfProduct) {
        this.nbOfProduct = nbOfProduct;
        this.productId = productId;
    }


}
