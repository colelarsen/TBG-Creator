package com.example.colea.tbg_creator_larsen.GameObjects.Conditional;

import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Player;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HasObject extends Conditional {
    public String object;
    public Conditional or;
    public Conditional and;
    public int id;
    public boolean not = false;
    public String uniqueUserId = "";

    public String getUUID()
    {
        return uniqueUserId;
    }

    public String getMainId()
    {
        if(uniqueUserId.isEmpty())
        {
            return ""+id;
        }
        else
        {
            return getUUID();
        }
    }

    public HasObject()
    {
        id = GameController.getId();
    }

    public int orId;
    public int andId;
    public HasObject(int i, String objec, int aId, int oId, boolean no)
    {
        object = objec;
        id = i;
        orId = oId;
        andId = aId;
        not = no;
    }

    @Override
    public void link(GameObjects gameObjects) {
        or = (Conditional) gameObjects.findObjectById(orId);
        and = (Conditional) gameObjects.findObjectById(andId);
    }

    public static HasObject fromJSON(JSONObject nextObject)
    {
        /*
            private Conditional or;
            private Conditional and;
            private String object;
            private int id;
            private boolean not = false;
            */
        try {
            int id = nextObject.getInt("id");
            String object = nextObject.getString("object");
            int orId = -1;
            int andId = -1;
            if(nextObject.has("or"))
            {
                orId = nextObject.getInt("or");
            }
            if(nextObject.has("and"))
            {
                andId = nextObject.getInt("and");
            }
            String uuid = "";
            if(nextObject.has("uuid"))
            {
                uuid = nextObject.getString("uuid");
            }
            boolean not = nextObject.getBoolean("not");
            HasObject hasObject = new HasObject(id, object, andId, orId, not);
            hasObject.uniqueUserId = uuid;
            return hasObject;
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return null;
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

            stateObject.put("id", id);
            stateObject.put("OBJECT TYPE", "HasObject");
            stateObject.put("uuid", uniqueUserId);

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

    public boolean check() {
        boolean ret = false;
        if(or != null)
        {
            ret = Player.getPlayer().inventory.findItemByName(object) != null || or.check();
        }
        else if(and != null)
        {
            ret = Player.getPlayer().inventory.findItemByName(object) != null && and.check();
        }
        else
        {
            ret = Player.getPlayer().inventory.findItemByName(object) != null;
        }
        if(not)
        {
            return !ret;
        }
        else
        {
            return ret;
        }
    }

    public void not()
    {
        not = !not;
    }

    @Override
    public void setConditional(String obj1, Conditional an, Conditional o) {
        object = obj1;
        and = an;
        or = o;
    }
}
