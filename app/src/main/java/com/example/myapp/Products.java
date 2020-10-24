package com.example.myapp;

public class Products {


    private int id;
    private int productId;
    private int qty;



    Products(int pId) {

        this.productId = pId;
        //this.qty = pQty;
    }

    Products(int id ,int pId , int pQty) {

        this.id = id;
        this.productId = pId;
       // this.qty = pQty;
    }

    int getCartId() {
        return id;
    }
    public void setCartId(int cartId) {
        this.id = cartId;
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


