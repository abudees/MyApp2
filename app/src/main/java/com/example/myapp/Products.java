package com.example.myapp;

public class Products {

    private int id;
    private int productId;
    private int qty;

    Products(int pId, int pQty) {

        this.productId = pId;
        this.qty = pQty;
    }

    Products(int id, int pId, int pQty) {
        this.id = id;
        this.productId = id;
        this.qty = pQty;
    }

    int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getProductId() {
        return productId;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }
    int getQty() {
        return qty;
    }
    public void setQty(int qty) {
        this.qty = qty;
    }
}


