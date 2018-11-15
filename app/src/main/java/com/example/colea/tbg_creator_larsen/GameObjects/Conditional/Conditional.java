package com.example.colea.tbg_creator_larsen.GameObjects.Conditional;

import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;

import org.json.JSONObject;

abstract public class Conditional {
    //Can Do something
    //Have something
    //Has Done something
        //Consider using Post / Getting / Deleting / Rest API
        //Key: Unique, Value: True/False , Timestamp
    //Timeouts

    private Conditional and;
    private Conditional or;

    //Gets unique id
    public abstract String getUUID();

    //Gets display text in edit Text
    public abstract String getMainId();

    //Converts to JSON
    public abstract JSONObject toJSON();

    //Link objects together
    public abstract void link(GameObjects gameObjects);

    //Checks to see if valid conditional
    public abstract boolean check();

    //Inverts the check
    public abstract void not();

    //Returns id
    public abstract int getId();

    //Sets the conditional
    public abstract void setConditional(String obj1, Conditional an, Conditional o);
}
