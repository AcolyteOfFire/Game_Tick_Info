package com.GameTickInfo;

import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayUtil;

import javax.inject.Inject;
import java.awt.*;
import java.util.Collection;

public class MarkedTilesOverlay extends Overlay{
    private static final int MAX_DRAW_DISTANCE = 32;

    private final Client client;
    private final GameTickInfoConfig config;
    private final GameTickInfoPlugin plugin;

    @Inject
    private MarkedTilesOverlay(Client client, GameTickInfoConfig config, GameTickInfoPlugin plugin)
    {
        this.client = client;
        this.config = config;
        this.plugin = plugin;
        setPosition(OverlayPosition.DYNAMIC);
        setPriority(OverlayPriority.LOW);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }
    @Override
    public Dimension render(Graphics2D graphics)
    {
        if(config.displayGameTickLaps()) {
            final Collection<GameTickTile> startTiles = plugin.getRememberedTiles();
            if (startTiles.isEmpty()) {
                return null;
            }
            float[] dash1 = {3f,0f,3f};
            Stroke stroke = new BasicStroke((float) config.borderWidth(),BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10,dash1,1);
            for (GameTickTile startTile : startTiles) {
                WorldPoint worldPoint = startTile.getWorldPoint();
                if (worldPoint.getPlane() != client.getPlane()) {
                    continue;
                }
                drawTile(graphics, worldPoint, stroke);
            }
        }
        return null;
    }
    private void drawTile(Graphics2D graphics, WorldPoint point, Stroke stroke)
    {
        WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
        if(point.distanceTo(playerLocation)>=MAX_DRAW_DISTANCE)
            {
                return;
            }
        LocalPoint lp = LocalPoint.fromWorld(client, point);
        if (lp == null)
            {
                return;
            }
        Polygon poly = Perspective.getCanvasTilePoly(client,lp);
        if(poly != null){
            OverlayUtil.renderPolygon(graphics,poly,config.markerColor(),stroke);
        }
    }

}

