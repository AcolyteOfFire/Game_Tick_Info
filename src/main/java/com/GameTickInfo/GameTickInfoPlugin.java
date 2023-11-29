package com.GameTickInfo;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.Tile;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ClientTick;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.chat.ChatCommandManager;
import net.runelite.client.input.KeyListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@PluginDescriptor(
	name = "Game Tick Info"
)
public class GameTickInfoPlugin extends Plugin implements KeyListener
{
	public static int timeOnTile = 0;
	public static int gameTickOnTile = 0;
	public static int lapStartTime=-1;
	public static int currentLapTime=-1;
	public static int previousLap = -1;
	public final List<GameTickTile> rememberedTiles = new ArrayList<>();

	private GameTickTile startTile;
	private GameTickTile previousTile;
	private GameTickTile location;
	private GameTickTile currentTile;
	private boolean shiftHeld = false;

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
	@Inject MarkedTilesOverlay markedTilesOverlay;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(gameTicksOnTileOverlay);
		overlayManager.add(gameTickLapsOverlay);
		overlayManager.add(markedTilesOverlay);
		chatCommandManager.registerCommand("!remember",this::rememberLocation);
		chatCommandManager.registerCommand("!check",this::checkLocation);
		chatCommandManager.registerCommand("!clear",this::clearMemory);
		chatCommandManager.registerCommand("!forget",this::forgetLocation);
		client.getCanvas().addKeyListener(this);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(gameTicksOnTileOverlay);
		overlayManager.remove(gameTickLapsOverlay);
		overlayManager.remove(markedTilesOverlay);
		chatCommandManager.unregisterCommand("!remember");
		chatCommandManager.unregisterCommand("!check");
		chatCommandManager.unregisterCommand("!clear");
		chatCommandManager.unregisterCommand("!forget");
		client.getCanvas().removeKeyListener(this);
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			shiftHeld = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			shiftHeld = false;
		}
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
	private void clearMemory(){
		rememberedTiles.clear();
		client.addChatMessage(ChatMessageType.GAMEMESSAGE,"","Memory has been cleared",null);
	}
	private void rememberLocation(ChatMessage chatMessage, String s) {
		if(!rememberedTiles.contains(new GameTickTile(client.getLocalPlayer().getWorldLocation()))) {
            rememberedTiles.add(new GameTickTile(client.getLocalPlayer().getWorldLocation()));
			client.addChatMessage(ChatMessageType.GAMEMESSAGE,"","Tile added to memory",null);
		}
		else{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE,"","Tile is already in memory",null);
		}
	}
	private void checkLocation(ChatMessage chatMessage, String s) {
		GameTickTile currentLocation = new GameTickTile(client.getLocalPlayer().getWorldLocation());
		boolean checkSuccess=false;
		for (GameTickTile tile: rememberedTiles
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
	public Collection<GameTickTile> getRememberedTiles(){
		return this.rememberedTiles;
	}
	private void resetCurrentLapTime(){
		lapStartTime = -1;
		currentLapTime = -1;
	}

	@Subscribe
	public void onClientTick(ClientTick clientTick) {
		//logic for game tick on tile counter
		currentTile = new GameTickTile(client.getLocalPlayer().getWorldLocation());
		if(currentTile.equals(previousTile)){
			timeOnTile = client.getTickCount()-gameTickOnTile-1;
		}
		else{
			timeOnTile = 0;
			gameTickOnTile= client.getTickCount();
		}
		//logic for the lap timer
		if(rememberedTiles.contains(currentTile)){
			if(currentLapTime!=-1){
				previousLap=currentLapTime;
			}
			resetCurrentLapTime();
		}
		//begin lap when you leave the start tile
		if(!rememberedTiles.contains(currentTile)&&rememberedTiles.contains(previousTile)){
			lapStartTime = client.getTickCount();
		}
		if(lapStartTime!=-1){
			currentLapTime = client.getTickCount()-lapStartTime;
		}
		//reset lap counter if there are no start tiles
		if(rememberedTiles.isEmpty()){
			resetCurrentLapTime();
			previousLap = -1;
		}


		previousTile=currentTile;
	}
	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event) {
		if(!config.displayGameTickLaps()) return;
		if (shiftHeld&&event.getOption().equals("Walk here")) {
			Tile selectedSceneTile = this.client.getSelectedSceneTile();
			if (selectedSceneTile == null){
				return;
			}
			this.client.createMenuEntry(-1).setOption("Mark/Unmark Start Zone").setTarget("Tile").setType(MenuAction.RUNELITE).onClick((e)->{
				Tile target = this.client.getSelectedSceneTile();
				if(target != null){
					GameTickTile targetGameTickTile = new GameTickTile(target.getWorldLocation()) ;
					if(!rememberedTiles.contains(targetGameTickTile)){
						rememberedTiles.add(targetGameTickTile);
					}
					else{
						rememberedTiles.remove(targetGameTickTile);
					}
				}
			});

		}
	}

	@Provides
	GameTickInfoConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(GameTickInfoConfig.class);
	}
}
