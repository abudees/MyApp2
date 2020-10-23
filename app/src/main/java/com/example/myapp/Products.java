package com.example.myapp;

public class Products {


    private int id;
    private int productId;
    private int qty;

/*
    public String ItemId;
    public String ItemName;
    public String Size;
    public String Company;
    public String Rate;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String Status;

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String Quantity;

    public void setItemId(String ItemId){
        this.ItemId=ItemId;
    }
    public String getItemId(){
        return ItemId;
    }
    public void setItemName(String ItemName){
        this.ItemName=ItemName;
    }
    public String getItemName(){
        return ItemName;
    }
    public void setSize(String Size){
        this.Size=Size;
    }
    public String getSize(){
        return Size;
    }
    public void setCompany(String Company){
        this.Company=Company;
    }
    public String getCompany(){
        return Company;
    }
    public void setRate(String Rate){
        this.Rate=Rate;
    }
    public String getRate(){
        return Rate;
    }

    public String getJsonObject(){
        return "{ItemId:"+ItemId+",ItemName:"+ItemName+",Size:"+Size+",Company:"+Company+",Rate:"+Rate+"}";
    }

*/
    Products(int pId, int pQty) {

        this.productId = pId;
        this.qty = pQty;
    }

    Products(int id ,int pId,   int pQty) {

        this.id = id;
        this.productId = pId;
        this.qty = pQty;
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


