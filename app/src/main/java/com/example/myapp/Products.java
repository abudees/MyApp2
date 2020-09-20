package com.example.myapp;

public class Products {


    private int cartId;
    private int productId;
  //  private String productTitle;
  //  private String imageURL;
  //  private int price;
    private int qty;


    Products(int pId, String pTitle, String pURL, int pPrice , int pQty) {

        this.productId = pId;
     /*   this.productTitle = pTitle;
        this.imageURL = pURL;
        this.price = pPrice; */
        this.qty = pQty;
    }

    Products(int pCartId ,int pId, String pTitle, String pURL, int pPrice , int pQty) {

        this.cartId = pCartId;
        this.productId = pId;
     /*   this.productTitle = pTitle;
        this.imageURL = pURL;
        this.price = pPrice;  */
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
/*
    String getProductTitle() {
        return productTitle;
    }
    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    String getImageURL() {
        return imageURL;
    }
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
*/
    int getQty() {
        return qty;
    }
    public void setQty(int qtyAvilable) {
        this.qty = qtyAvilable;
    }
}


