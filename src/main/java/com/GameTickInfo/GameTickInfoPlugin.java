package com.GameTickInfo;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ClientTick;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Game Tick Info"
)
public class GameTickInfoPlugin extends Plugin
{
	public static int timeOnTile = 0;

	public static int gameTickOnTile = 0;
	public int prevLocX;
	public int prevLocY;
	@Inject
	private Client client;

	@Inject
	private GameTickInfoConfig config;
	@Inject
	private OverlayManager overlayManager;
	@Inject
	private GameTickInfoOverlay overlay;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
	}


	@Subscribe
	public void onClientTick(ClientTick clientTick) {
		WorldPoint currentTile = client.getLocalPlayer().getWorldLocation();
		int currentLocX = currentTile.getX();
		int currentLocY = currentTile.getY();
		if(currentLocX==prevLocX&&currentLocY==prevLocY){
			timeOnTile = client.getTickCount()-gameTickOnTile-1;
		}
		else{
			timeOnTile = 0;
			gameTickOnTile= client.getTickCount();
		}
		prevLocX=currentLocX;
		prevLocY=currentLocY;
	}




	@Provides
	GameTickInfoConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(GameTickInfoConfig.class);
	}
}
