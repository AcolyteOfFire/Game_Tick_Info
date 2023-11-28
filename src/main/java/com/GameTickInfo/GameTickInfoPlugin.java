package com.GameTickInfo;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ClientTick;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.chat.ChatCommandManager;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@PluginDescriptor(
	name = "Game Tick Info"
)
public class GameTickInfoPlugin extends Plugin
{
	public static int timeOnTile = 0;
	public static int gameTickOnTile = 0;
	private final List<SelectedTile> rememberedTiles = new ArrayList<>();
	public int prevLocX;
	public int prevLocY;
	private SelectedTile startTile;
	private SelectedTile previousTile;
	private SelectedTile location;
	@Inject
	private Client client;
	@Inject
	private ChatCommandManager chatCommandManager;
	@Inject
	private GameTickInfoConfig config;
	@Inject
	private OverlayManager overlayManager;
	@Inject
	private GameTicksOnTileOverlay overlay;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
		chatCommandManager.registerCommand("!remember",this::rememberLocation);
		chatCommandManager.registerCommand("!check",this::checkLocation);
	}




	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
		chatCommandManager.unregisterCommand("!remember");
		chatCommandManager.unregisterCommand("!check");
	}

	private void rememberLocation(ChatMessage chatMessage, String s) {
		if(!rememberedTiles.contains(new SelectedTile(client.getLocalPlayer().getWorldLocation()))) {
            rememberedTiles.add(new SelectedTile(client.getLocalPlayer().getWorldLocation()));
			client.addChatMessage(ChatMessageType.GAMEMESSAGE,"","Tile added to memory",null);
		}
		else{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE,"","Tile is already in memory",null);
		}
	}
	private void checkLocation(ChatMessage chatMessage, String s) {
		SelectedTile currentLocation = new SelectedTile(client.getLocalPlayer().getWorldLocation());
		boolean checkSuccess=false;
		for (SelectedTile tile: rememberedTiles
			 ) {
			if(currentLocation.equals(tile)){
				client.addChatMessage(ChatMessageType.GAMEMESSAGE,"","This tile is remembered!",null);
				checkSuccess=true;
			}
		}
		if (!checkSuccess){
			client.addChatMessage(ChatMessageType.GAMEMESSAGE,"","Tile not in memory",null);
		}
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
