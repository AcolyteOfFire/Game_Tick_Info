package com.GameTickInfo;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.api.MenuAction;
import javax.inject.Inject;
import net.runelite.api.Client;

import java.awt.*;

public class GameTicksOnTileOverlay extends OverlayPanel{
    private final GameTickInfoConfig config;
    private final Client client;
    private final PanelComponent gameTicksOnTilePanelComponent = new PanelComponent();



    @Inject
    private GameTicksOnTileOverlay(GameTickInfoConfig config, Client client)
    {
        this.config = config;
        this.client = client;
        setPosition(OverlayPosition.TOP_LEFT);
        isResizable();
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        gameTicksOnTilePanelComponent.getChildren().clear();
        if( config.displayGameTicksOnTile()){
            String valueToDisplay = String.valueOf(GameTickInfoPlugin.timeOnTile);
            if(!config.startTicksOnTileAtZero()) valueToDisplay = String.valueOf(GameTickInfoPlugin.timeOnTile+1);
            gameTicksOnTilePanelComponent.getChildren().add(TitleComponent.builder()
                    .text(valueToDisplay)
                    .color(config.gameTicksOnTileColor())
                    .build()
            );
            gameTicksOnTilePanelComponent.setPreferredSize(new Dimension(graphics.getFontMetrics().stringWidth(String.valueOf(GameTickInfoPlugin.timeOnTile))+10,0));
        }
        return gameTicksOnTilePanelComponent.render(graphics);
    }

}
