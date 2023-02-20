package com.example.sdaassign4_2022;

public class Order {
    private String bookId;
    private String bookName;
    private String borrower;
    private String orderDetails;
    private long orderTime;

    public Order(String bookId, String bookName, String borrower, String orderDetails, long orderTime) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.borrower = borrower;
        this.orderDetails = orderDetails;
        this.orderTime = orderTime;
    }

    // getters and setters for the private properties
}
