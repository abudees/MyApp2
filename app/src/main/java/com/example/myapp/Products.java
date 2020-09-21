package com.example.myapp;

public class Products {


    private int cartId;
    private int productId;
    private int qty;


    Products(int Id, int pQty) {

        this.productId = pId;
        this.qty = pQty;
    }

    Products(int pCartId ,int pId,   int pQty) {

        this.cartId = pCartId;
        this.productId = pId;

        this.qty = pQty;
    }

    int getCartId() {
        return cartId;
    }
    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    int getProductId() {
        return productId;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }

    int getQty() {
        return qty;
    }
    public void setQty(int qtyAvilable) {
        this.qty = qtyAvilable;
    }
}


