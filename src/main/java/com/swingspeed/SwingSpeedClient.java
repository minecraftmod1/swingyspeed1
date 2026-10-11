package com.swingspeed;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class SwingSpeedClient implements ClientModInitializer {
	private static KeyMapping menuKey;

	@Override
	public void onInitializeClient() {
		SwingConfig.load();

		KeyMapping.Category category = KeyMapping.Category.register(
				Identifier.fromNamespaceAndPath("swingspeed", "main"));

		menuKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key.swingspeed.menu",
				InputConstants.Type.KEYSYM,
				InputConstants.KEY_F9,
				category));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (menuKey.consumeClick()) {
				if (client.screen == null) {
					client.setScreen(new SwingSpeedScreen());
				}
			}
		});
	}
}
