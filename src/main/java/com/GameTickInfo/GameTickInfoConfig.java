package com.GameTickInfo;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

import java.awt.*;

@ConfigGroup("example")
public interface GameTickInfoConfig extends Config
{
	@ConfigItem(
			keyName = "displayGameTicksOnTile",
			name = "Display Time On Tile",
			description = "Shows how many game ticks you have been on the current tile"
	)
	default boolean displayGameTicksOnTile() { return true; }
	@ConfigItem(
			keyName = "gameTicksOnTileColor",
			name = "Time on Tile Text Color",
			description = "Chose a color for the game ticks on tile counter"
	)
	default Color gameTicksOnTileColor()
	{
		return Color.GREEN;
	}
	@ConfigItem(
			keyName = "displayGameTicksSinceCycleStart",
			name = "Display custom game tick cycle",
			description = "Shows how many game ticks ago you preformed an inventory action"
	)
	default boolean displayGameTicksSinceCycleStart() { return true; }
	@ConfigItem(
			keyName = "gameTicksPerCycle",
			name = "Number of ticks",
			description = "How many ticks to display per cycle"
	)
	default int gameTicksPerCycle() { return 4; }
	@ConfigItem(
			keyName = "startCountAtZeroToggle",
			name = "Start Tick Cycle With Zero",
			description = "Shows how many game ticks ago you preformed an inventory action"
	)
	default boolean startCountAtZeroToggle() { return false; }
	@ConfigItem(
			keyName = "gameTicksCycleColor",
			name = "Tick Cycle Color",
			description = "Choose a color for the game ticks since inventory action counter"
	)
	default Color gameTicksCycleColor()
	{
		return Color.CYAN;
	}
	@ConfigItem(
			keyName = "displayGameTickLaps",
			name = "Display Lap information",
			description = "Lets you mark a start location for a 'lap' and tracks time until you pass that location"
	)
	default boolean displayGameTickLaps() { return false; }

	@ConfigItem(
			keyName = "markerColor",
			name = "Tile color",
			description = "The default color for marked tiles"
	)
	default Color markerColor()
	{
		return Color.CYAN;
	}
	@ConfigItem(
			keyName = "fillOpacity",
			name = "Fill Opacity",
			description = "Opacity of the tile fill color"
	)
	@Range(
			max = 255
	)
	default int fillOpacity()
	{
		return 50;
	}
	@ConfigItem(
			keyName = "borderWidth",
			name = "Border Width",
			description = "Width of the marked tile border"
	)
	default double borderWidth()
	{
		return 3;
	}
}
