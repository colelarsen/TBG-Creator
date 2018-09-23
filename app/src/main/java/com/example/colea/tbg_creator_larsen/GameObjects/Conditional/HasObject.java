package com.example.colea.tbg_creator_larsen.GameObjects.Conditional;

import com.example.colea.tbg_creator_larsen.GameObjects.Conditional.Conditional;

public class HasObject extends Conditional {
    private String object;
    private Conditional or;
    private Conditional and;

    @Override
    public boolean check() {
        if(or != null)
        {
            //If(player.inventory.contains(object) || or.check())
        }
        else if(and != null)
        {
            //If(player.inventory.contains(object) && and.check())
        }
        else
        {
            //If(player.inventory.contains(object))
        }
        return true;
    }

    @Override
    public void setConditional(String obj1, Conditional an, Conditional o) {
        object = obj1;
        and = an;
        or = o;
    }
}
