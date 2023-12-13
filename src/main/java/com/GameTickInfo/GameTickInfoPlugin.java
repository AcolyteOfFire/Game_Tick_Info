package com.GameTickInfo;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.GameTick;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.chat.ChatCommandManager;
import net.runelite.client.input.KeyListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@PluginDescriptor(
	name = "Game Tick Info",
	description = "Displays game tick counters and lets you mark tiles to track laps"
)
public class GameTickInfoPlugin extends Plugin implements KeyListener
{
	public static int timeOnTile = 0;
	public static int gameTickOnTile = 0;
	public static int timeSinceCycleStart;
	public static int lapStartTime=-1;
	public static int currentLapTime=-1;
	public static int previousLap = -1;
	public static int totalLaps = 0;
	public final List<GameTickTile> rememberedTiles = new ArrayList<>();

	private GameTickTile previousTile;
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
	@Inject
	private MarkedTilesOverlay markedTilesOverlay;
	@Inject
	private GameTickCycleOverlay gameTickCycleOverlay;


	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(markedTilesOverlay);
		overlayManager.add(gameTickLapsOverlay);
		overlayManager.add(gameTicksOnTileOverlay);
		overlayManager.add(gameTickCycleOverlay);
		chatCommandManager.registerCommand("!remember",this::rememberLocation);
		chatCommandManager.registerCommand("!clear",this::clearMemory);
		chatCommandManager.registerCommand("!forget",this::forgetLocation);
		chatCommandManager.registerCommand("!reset",this::resetLapCounter);
		client.getCanvas().addKeyListener(this);
	}



	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(gameTicksOnTileOverlay);
		overlayManager.remove(gameTickLapsOverlay);
		overlayManager.remove(markedTilesOverlay);
		overlayManager.remove(gameTickCycleOverlay);
		chatCommandManager.unregisterCommand("!remember");
		chatCommandManager.unregisterCommand("!clear");
		chatCommandManager.unregisterCommand("!forget");
		chatCommandManager.unregisterCommand("!reset");
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
	private void resetLapCounter(ChatMessage chatMessage, String s) {
		totalLaps=0;
		resetCurrentLapTime();
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
	public void clearMemory(ChatMessage chatMessage, String s) {
		rememberedTiles.clear();
		client.addChatMessage(ChatMessageType.GAMEMESSAGE,"","Memory has been cleared",null);
	}
	void clearMemory(){
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
				totalLaps++;
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
			totalLaps = 0;
		}
		previousTile=currentTile;
	}
	@Subscribe
	public void onGameTick(GameTick event){
		//Game Tick Cycle Logic
		if(timeSinceCycleStart>=config.gameTicksPerCycle()){
			timeSinceCycleStart = 1;
		}
		else{
			timeSinceCycleStart++;
		}
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
