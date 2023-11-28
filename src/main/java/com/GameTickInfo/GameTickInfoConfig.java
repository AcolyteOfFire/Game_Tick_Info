package com.GameTickInfo;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("example")
public interface GameTickInfoConfig extends Config
{
	@ConfigItem(
			keyName = "displayGameTicksOnTile",
			name = "Display Time On Tile",
			description = "Shows how many game ticks you have been on the current tile"
	)
	default boolean displayGameTicksOnTile() { return true; }
}
