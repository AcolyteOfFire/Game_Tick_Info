package com.GameTickInfo;

import net.runelite.api.coords.WorldPoint;

public class SelectedTile {
    private final WorldPoint thisLocation;

    SelectedTile(WorldPoint worldPoint){
        thisLocation = worldPoint;
    }
    public int getX(){
        return thisLocation.getX();
    }
    public int getY(){
        return thisLocation.getY();
    }
    public boolean equals(SelectedTile comparedTile) {
        return this.getX() == comparedTile.getX() && this.getY() == comparedTile.getY();
    }
}
