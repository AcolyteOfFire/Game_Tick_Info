package com.GameTickInfo;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
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
	private SelectedTile currentTile;
	@Inject
	private Client client;
	@Inject
	private ChatCommandManager chatCommandManager;
	@Inject
	private GameTickInfoConfig config;
	@Inject
	private OverlayManager overlayManager;
	@Inject
	private GameTicksOnTileOverlay gameTicksOnTileOverlay;
	@Inject
	private GameTickLapsOverlay gameTickLapsOverlay;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(gameTicksOnTileOverlay);
		overlayManager.add(gameTickLapsOverlay);
		chatCommandManager.registerCommand("!remember",this::rememberLocation);
		chatCommandManager.registerCommand("!check",this::checkLocation);
		chatCommandManager.registerCommand("!clear",this::clearMemory);
		chatCommandManager.registerCommand("!forget",this::forgetLocation);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(gameTicksOnTileOverlay);
		overlayManager.remove(gameTickLapsOverlay);
		chatCommandManager.unregisterCommand("!remember");
		chatCommandManager.unregisterCommand("!check");
		chatCommandManager.unregisterCommand("!clear");
		chatCommandManager.unregisterCommand("!forget");
	}
	private void forgetLocation(ChatMessage chatMessage, String s) {
		if(rememberedTiles.contains(currentTile)) {
			rememberedTiles.remove(currentTile);
			client.addChatMessage(ChatMessageType.GAMEMESSAGE,"","Tile removed from memory",null);
		}
		else {
			client.addChatMessage(ChatMessageType.GAMEMESSAGE,"","Tile was not in memory",null);
		}
	}

	private void clearMemory(ChatMessage chatMessage, String s) {
		rememberedTiles.clear();
		client.addChatMessage(ChatMessageType.GAMEMESSAGE,"","Memory has been cleared",null);
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
			client.addChatMessage(ChatMessageType.GAMEMESSAGE,"","Tile is not in memory",null);
		}
	}

	@Subscribe
	public void onClientTick(ClientTick clientTick) {
		currentTile = new SelectedTile(client.getLocalPlayer().getWorldLocation());
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
