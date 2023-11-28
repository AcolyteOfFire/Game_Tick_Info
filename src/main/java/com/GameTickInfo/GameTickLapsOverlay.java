package com.GameTickInfo;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
import javax.inject.Inject;
import net.runelite.api.Client;
import java.awt.*;
public class GameTickLapsOverlay extends OverlayPanel{
    private final GameTickInfoConfig config;
    private final Client client;
    private final PanelComponent gameTickLapsPanelComponent = new PanelComponent();



    @Inject
    private GameTickLapsOverlay(GameTickInfoConfig config, Client client)
    {
        this.config = config;
        this.client = client;
        setPosition(OverlayPosition.TOP_LEFT);
        isResizable();
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        gameTickLapsPanelComponent.getChildren().clear();
        if( config.displayGameTicksOnTile()){
            gameTickLapsPanelComponent.getChildren().add(TitleComponent.builder()
                    .text(String.valueOf(GameTickInfoPlugin.timeOnTile))
                    .color(Color.GREEN)
                    .build()
            );
            gameTickLapsPanelComponent.setPreferredSize(new Dimension(graphics.getFontMetrics().stringWidth(String.valueOf(GameTickInfoPlugin.timeOnTile))+10,0));
        }
        return gameTickLapsPanelComponent.render(graphics);
    }
}
