package com.GameTickInfo;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;

public class GameTickCycleOverlay extends OverlayPanel{
    private final GameTickInfoConfig config;
    private final Client client;
    private final PanelComponent GameTickCyclePanelComponent = new PanelComponent();



    @Inject
    private GameTickCycleOverlay(GameTickInfoConfig config, Client client)
    {
        this.config = config;
        this.client = client;
        setPosition(OverlayPosition.TOP_LEFT);
        isResizable();
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        String timeToDisplay = String.valueOf(GameTickInfoPlugin.timeSinceCycleStart);
        if(config.startCountAtZeroToggle()){
            timeToDisplay = String.valueOf(GameTickInfoPlugin.timeSinceCycleStart - 1);
        }
        GameTickCyclePanelComponent.getChildren().clear();
        if( config.displayGameTicksOnTile()){
            GameTickCyclePanelComponent.getChildren().add(TitleComponent.builder()
                    .text(timeToDisplay)
                    .color(config.gameTicksCycleColor())
                    .build()
            );
            GameTickCyclePanelComponent.setPreferredSize(new Dimension(graphics.getFontMetrics().stringWidth(String.valueOf(GameTickInfoPlugin.timeSinceCycleStart))+10,0));
        }
        return GameTickCyclePanelComponent.render(graphics);
    }

}
