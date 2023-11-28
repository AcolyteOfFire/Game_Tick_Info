package com.GameTickInfo;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Player;

import java.awt.*;

public class GameTickInfoOverlay extends OverlayPanel{
    private final GameTickInfoConfig config;
    private final Client client;
    private final PanelComponent gameTickInfoPanelComponent = new PanelComponent();



    @Inject
    private GameTickInfoOverlay(GameTickInfoConfig config, Client client)
    {
        this.config = config;
        this.client = client;
        setPosition(OverlayPosition.TOP_LEFT);
        isResizable();
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        gameTickInfoPanelComponent.getChildren().clear();
        if( config.displayGameTicksOnTile()){
            gameTickInfoPanelComponent.getChildren().add(TitleComponent.builder()
                    .text(String.valueOf(GameTickInfoPlugin.timeOnTile))
                    .color(Color.GREEN)
                    .build()
            );
        }
        return gameTickInfoPanelComponent.render(graphics);
    }
}
