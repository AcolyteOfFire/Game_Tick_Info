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
		return Color.cyan;
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
		return 2;
	}
}
