package com.GameTickInfo;

import net.runelite.api.coords.WorldPoint;

public class GameTickTile {
    private final WorldPoint worldPoint;

    GameTickTile(WorldPoint worldPoint){
        this.worldPoint = worldPoint;
    }
    public int getX(){
        return worldPoint.getX();
    }
    public int getY(){
        return worldPoint.getY();
    }

    public WorldPoint getWorldPoint() {
        return worldPoint;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GameTickTile comparedTile = (GameTickTile) obj;
        return this.getX() == comparedTile.getX() && this.getY() == comparedTile.getY();
    }
}
