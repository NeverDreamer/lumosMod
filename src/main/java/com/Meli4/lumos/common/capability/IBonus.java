package com.Meli4.lumos.common.capability;

import com.Meli4.lumos.common.event.SetBonus;

public interface IBonus {

    int getCooldown();

    int setCooldown(int var);

    int getDuration();

    int setDuration(int var);

    //NO hasMode
    boolean getMode();

    boolean setMode(boolean var);



}
