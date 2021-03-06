package com.example.colea.tbg_creator_larsen.GameObjects.Conditional;

import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;
import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.Effect;
import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameController;
import com.example.colea.tbg_creator_larsen.GameObjects.Player.Player;

import org.json.JSONException;
import org.json.JSONObject;

public class HasSpell extends Conditional {
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

    public JSONObject toJSON()
    {
        try {
            /*
            private Conditional or;
            private Conditional and;
            private String object;
            private int id;
            private boolean not = false;
            */
            JSONObject stateObject = new JSONObject();
            stateObject.put("OBJECT TYPE", "HasSpell");
            stateObject.put("uuid", uniqueUserId);

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

    public HasSpell()
    {
        id = GameController.getId();
    }

    public int orId;
    public int andId;
    public HasSpell(int i, String objec, int oId, int aId, boolean no)
    {
        id = i;
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

    public static HasSpell fromJSON(JSONObject nextObject)
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
            String uuid = "";
            if(nextObject.has("uuid"))
            {
                uuid = nextObject.getString("uuid");
            }
            if(nextObject.has("or"))
            {
                orId = nextObject.getInt("or");
            }
            if(nextObject.has("and"))
            {
                andId = nextObject.getInt("and");
            }
            boolean not = nextObject.getBoolean("not");
            HasSpell hasSpell = new HasSpell(id, object, orId, andId, not);
            hasSpell.uniqueUserId = uuid;
            return hasSpell;
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

    //Check to see if player has the given effect name
    public boolean check() {
        boolean hasSpell = false;
        boolean ret = false;
        for(Effect spell : Player.getPlayer().spells)
        {
            if(hasSpell == false) {
                hasSpell = spell.getName().compareTo(object) == 0;
            }
        }

        if(or != null)
        {
            ret = (hasSpell || or.check());
        }
        else if(and != null)
        {
            ret = (hasSpell && and.check());
        }
        else
        {
            ret = hasSpell;
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

    @Override
    public void setConditional(String obj1, Conditional an, Conditional o) {
        object = obj1;
        and = an;
        or = o;
    }
}
