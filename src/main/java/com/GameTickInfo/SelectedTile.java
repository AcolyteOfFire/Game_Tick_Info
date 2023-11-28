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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SelectedTile comparedTile = (SelectedTile) obj;
        return this.getX() == comparedTile.getX() && this.getY() == comparedTile.getY();
    }
}
