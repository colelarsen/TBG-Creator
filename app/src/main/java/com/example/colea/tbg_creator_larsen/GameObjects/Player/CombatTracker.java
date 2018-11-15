package com.example.colea.tbg_creator_larsen.GameObjects.Player;

import com.example.colea.tbg_creator_larsen.GameObjects.Effect_Spell_Item.Effect;

public interface CombatTracker {

    void turnOver();
    String applyEffect(Effect e);
    boolean shouldSkipTurn();
    void combatOver();
    boolean isDefending();
    void turnStarts();
}
