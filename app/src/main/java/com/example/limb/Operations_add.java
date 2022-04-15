package com.example.limb;
import java.util.Date;
public class Operations_add {
    private String Login;
    private String Password;
    private Double Balance;
    private Double Expense;
    private Double Income;
    private Double Transfer;
    private String First_Name;
    private String Last_Name;
    private String name_operation;
    private Float sum;
    private String categoty;
    private Date date;
    private Date time;
    private String detailed;
    private String geo;
    public Operations_add() { }
    public Double getExpense() {
        return Expense; }
    public void setExpense(Double expense) {
        Expense = expense; }
    public Double getIncome() {
        return Income; }
    public void setIncome(Double income) {
        Income = income; }
    public Double getTransfer() {
        return Transfer; }
    public void setTransfer(Double transfer) {
        Transfer = transfer; }
    public String getFirst_Name() {
        return First_Name;
    }
    public Double getBalance() {
        return Balance;
    }
    public void setBalance(Double balance) {
        Balance = balance;
    }
    public void setFirst_Name(String first_Name) {
        First_Name = first_Name;
    }
    public String getLast_Name() {
        return Last_Name;
    }
    public void setLast_Name(String last_Name) {
        Last_Name = last_Name;
    }
    public String getLogin() {
        return Login;
    }
    public void setLogin(String login) {
        Login = login;
    }
    public String getPassword() {
        return Password;
    }
    public void setPassword(String password) {
        Password = password;
    }
    public String getName_operation() {
        return name_operation;
    }
    public void setName_operation(String name_operation) {
        this.name_operation = name_operation;
    }
    public Float getSumm() {
        return sum;
    }
    public void setSumm(Float summ) {
        this.sum = summ;
    }
    public String getCategoty() {
        return categoty;
    }
    public void setCategoty(String categoty) {
        this.categoty = categoty;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public Date getTime() {
        return time;
    }
    public void setTime(Date time) {
        this.time = time;
    }
    public String getDetailed() {
        return detailed;
    }
    public void setDetailed(String detailed) {
        this.detailed = detailed;
    }
    public String getGeo() {
        return geo;
    }
    public void setGeo(String geo) {
        this.geo = geo;
    }}
