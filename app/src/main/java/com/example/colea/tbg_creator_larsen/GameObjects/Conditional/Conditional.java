package com.example.colea.tbg_creator_larsen.GameObjects.Conditional;

abstract public class Conditional {
    //Can Do something
    //Have something
    //Has Done something
        //Consider using Post / Getting / Deleting / Rest API
        //Key: Unique, Value: True/False , Timestamp
    //Timeouts

    private Conditional and;
    private Conditional or;

    public abstract boolean check();

    public abstract int getId();

    public abstract void setConditional(String obj1, Conditional an, Conditional o);
}
