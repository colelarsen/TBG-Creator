package com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item;

import com.example.colea.tbg_creator_larsen.GameObjects.Controllers.GameObjects;

import org.json.JSONObject;

abstract public class Effect {
    //Types of effects to add

    //Weakens / Buffs Attack
    //Weakens / Buffs Defence
    //Stuns single target and multi target
    //Damage to single target
    //Damage to all enemy targets
    //Automatic Escape (Teleporting?)
    //Healing

    public abstract String getUUID();

    public abstract String getMainId();

    public abstract String getDescription();

    public abstract JSONObject toJSON();

    public abstract void link(GameObjects gameObjects);

    public abstract String getName();

    //This object can be Player or [Enemy]
    public abstract String effect(Object o);

    //Will always be Player or Enemy
    public abstract void undo(Object o);

    public  abstract int getDuration();

    public abstract boolean skipsTurn();

    public abstract boolean combatOnly();

    public abstract int getId();

}
