package com.GameTickInfo;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TextComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
import javax.inject.Inject;
import net.runelite.api.Client;
import java.awt.*;
public class GameTickLapsOverlay extends OverlayPanel{
    private final GameTickInfoConfig config;
    private final Client client;
    private final PanelComponent gameTickLapsPanelComponent = new PanelComponent();
    private final GameTickInfoPlugin plugin;



    @Inject
    private GameTickLapsOverlay(GameTickInfoConfig config, Client client, GameTickInfoPlugin plugin)
    {
        this.config = config;
        this.client = client;
        this.plugin = plugin;
        setPosition(OverlayPosition.TOP_LEFT);
        isResizable();
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        gameTickLapsPanelComponent.getChildren().clear();
        if(config.displayGameTickLaps()) {
            String currentLap = String.valueOf(GameTickInfoPlugin.currentLapTime);
            String previousLap = String.valueOf(GameTickInfoPlugin.previousLap);

            if (currentLap.equals("-1")) {
                currentLap = "Not started";
            }

            gameTickLapsPanelComponent.getChildren().clear();
            if (config.displayGameTickLaps()) {
                gameTickLapsPanelComponent.getChildren().add(TitleComponent.builder()
                        .text("Laps")
                        .color(Color.GREEN)
                        .build());
                gameTickLapsPanelComponent.getChildren().add(LineComponent.builder()
                        .left("Lap Time: ")
                        .leftColor(Color.WHITE)
                        .right(currentLap)
                        .rightColor(Color.GREEN)
                        .build());
                if (!previousLap.equals("-1")) {
                    gameTickLapsPanelComponent.getChildren().add(LineComponent.builder()
                            .left("Previous Lap: ")
                            .leftColor(Color.WHITE)
                            .rightColor(Color.GREEN)
                            .right(previousLap)
                            .build()
                    );
                }
                gameTickLapsPanelComponent.setPreferredSize(new Dimension(graphics.getFontMetrics()
                        .stringWidth(String.valueOf("Previous Lap: ")) + 10 + (Math.max(graphics
                        .getFontMetrics()
                        .stringWidth(previousLap), graphics.getFontMetrics().stringWidth(currentLap))), 0));
            }
        }
        return gameTickLapsPanelComponent.render(graphics);
    }
}
