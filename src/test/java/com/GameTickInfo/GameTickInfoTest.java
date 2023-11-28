package com.GameTickInfo;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class GameTickInfoTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(GameTickInfoPlugin.class);
		RuneLite.main(args);
	}
}