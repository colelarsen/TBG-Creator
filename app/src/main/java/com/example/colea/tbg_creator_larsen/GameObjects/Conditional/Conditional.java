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

    public abstract String getUUID();

    public abstract String getMainId();

    public abstract JSONObject toJSON();

    public abstract void link(GameObjects gameObjects);

    public abstract boolean check();

    public abstract void not();

    public abstract int getId();

    public abstract void setConditional(String obj1, Conditional an, Conditional o);
}
