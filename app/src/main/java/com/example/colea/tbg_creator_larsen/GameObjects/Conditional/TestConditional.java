package com.example.colea.tbg_creator_larsen.GameObjects.Conditional;

import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;

import org.json.JSONException;
import org.json.JSONObject;

public class TestConditional extends Conditional {
    private String object;
    private Conditional or;
    private Conditional and;
    private int id;
    private boolean not = false;

    @Override
    public void link(GameObjects gameObjects) {
    }

    public JSONObject toJSON()
    {
        try {
            /*
            private static ArrayList<String> switches = new ArrayList<>();
            private static ArrayList<Boolean> switchesValue = new ArrayList<>();
            private Conditional or;
            private Conditional and;
            private String singleSwitch;
            private int id;
            private boolean not = false;
            */
            JSONObject stateObject = new JSONObject();
            stateObject.put("OBJECT TYPE", "TestConditional");
            stateObject.put("id", id);
            stateObject.put("object", object);
            if(or != null) {
                stateObject.put("or", or.getId());
            }
            if(and != null) {
                stateObject.put("and", and.getId());
            }
            stateObject.put("not", not);

            return stateObject;
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }


        return null;

    }

    public int getId()
    {
        return id;
    }

    public void not()
    {
        not = !not;
    }

    @Override
    public boolean check() {
        return true;
    }

    @Override
    public void setConditional(String obj1, Conditional an, Conditional o) {
        object = obj1;
        and = an;
        or = o;
        id = GameController.getId();
    }
}
