package com.GameTickInfo;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
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
            gameTicksOnTilePanelComponent.getChildren().add(TitleComponent.builder()
                    .text(String.valueOf(GameTickInfoPlugin.timeOnTile))
                    .color(Color.GREEN)
                    .build()
            );
            gameTicksOnTilePanelComponent.setPreferredSize(new Dimension(graphics.getFontMetrics().stringWidth(String.valueOf(GameTickInfoPlugin.timeOnTile))+10,0));
        }
        return gameTicksOnTilePanelComponent.render(graphics);
    }

}