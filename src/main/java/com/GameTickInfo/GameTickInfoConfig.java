package com.GameTickInfo;

import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup("Game Tick Information")
public interface GameTickInfoConfig extends Config
{
	@ConfigSection(
			name = "Time On Tile",
			description = "Displays a counter for time spent on the current tile",
			position = 0
	)
	String timeOnTileSection = "timeOnTileSection";
	@ConfigItem(
			position = 1,
			keyName = "displayGameTicksOnTile",
			name = "Display Time on Tile",
			description = "Shows how many game ticks you have been on the current tile",
			section = timeOnTileSection
	)
	default boolean displayGameTicksOnTile() { return true; }
	@ConfigItem(
			position = 2,
			keyName = "startTicksOnTileAtZero",
			name = "Start Time on Tile at Zero",
			description = "Starts the time on tile counter at zero instead of one",
			section = timeOnTileSection
	)
	default boolean startTicksOnTileAtZero() { return true; }
	@ConfigItem(
			position = 3,
			keyName = "gameTicksOnTileColor",
			name = "Time on Tile Text Color",
			description = "Chose a color for the game ticks on tile counter",
			section = timeOnTileSection
	)
	default Color gameTicksOnTileColor()
	{
		return Color.GREEN;
	}
	@ConfigSection(
			name = "Custom Game Tick Cycle",
			description = "Display a cycle of game ticks",
			position = 2
	)
	String customGameTickCycle = "customGameTickCycle";
	@ConfigItem(
			position = 1,
			keyName = "displayGameTicksSinceCycleStart",
			name = "Display custom game tick cycle",
			description = "Displays a counter to track game ticks in a set cycle",
			section = customGameTickCycle
	)
	default boolean displayGameTicksSinceCycleStart() { return true; }

	@ConfigItem(
			position = 2,
			keyName = "gameTicksPerCycle",
			name = "Number of Ticks",
			description = "How many ticks to display per cycle",
			section = customGameTickCycle
	)
	default int gameTicksPerCycle() { return 4; }
	@ConfigItem(
			position = 3,
			keyName = "startCountAtZeroToggle",
			name = "Start Tick Cycle at Zero",
			description = "Starts the cycle count at zero instead of one",
			section = customGameTickCycle
	)
	default boolean startCountAtZeroToggle() { return false; }
	@ConfigItem(
			position = 4,
			keyName = "gameTicksCycleColor",
			name = "Tick Cycle Text Color",
			description = "Choose a color for the game tick cycle counter",
			section = customGameTickCycle
	)
	default Color gameTicksCycleColor()
	{
		return Color.CYAN;
	}
	@ConfigSection(
			name = "Game Tick Laps",
			description = "Lets you mark tiles with 'Shift + Right Click' to mark lap start locations, and displays related lap information",
			position = 3
	)
	String gameTickLapsSection = "gameTickLapsSection";
	@ConfigItem(
			position = 1,
			keyName = "displayGameTickLaps",
			name = "Display Lap Information",
			description = "Lets you mark a start location for a lap using 'Shift + Right Click' and tracks time until you pass that location",
			section = gameTickLapsSection
	)
	default boolean displayGameTickLaps() { return false; }

	@ConfigItem(
			position = 2,
			keyName = "markerColor",
			name = "Tile Color",
			description = "The color for lap tiles",
			section = gameTickLapsSection
	)
	default Color markerColor()
	{
		return Color.CYAN;
	}

	@ConfigItem(
			position = 3,
			keyName = "borderWidth",
			name = "Border Width",
			description = "Width of the marked tile border",
			section = gameTickLapsSection
	)
	default double borderWidth()
	{
		return 3;
	}
}
