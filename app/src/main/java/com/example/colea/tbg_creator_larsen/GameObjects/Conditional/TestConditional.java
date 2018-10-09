package com.example.colea.tbg_creator_larsen.GameObjects.Conditional;

import com.example.colea.tbg_creator_larsen.GameObjects.GameController;

public class TestConditional extends Conditional {
    private String object;
    private Conditional or;
    private Conditional and;
    private int id;

    public int getId()
    {
        return id;
    }

    @Override
    public boolean check() {
        if(or != null)
        {
            return (object.equals("true") || or.check());
        }
        else if(and != null)
        {
            return (object.equals("true") && and.check());
        }
        else
        {
            return (object.equals("true"));
        }
    }

    @Override
    public void setConditional(String obj1, Conditional an, Conditional o) {
        object = obj1;
        and = an;
        or = o;
        id = GameController.getId();
    }
}
