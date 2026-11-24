package io.github.xadby.adok;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.InventoryMenu;

public class AllDropOneKeyClient implements ClientModInitializer {
	public static final String MOD_ID = "adok";

	private static final KeyMapping DROP_ALL_KEY = new KeyMapping(
			"key.adok.drop_all",
			InputConstants.Type.KEYBOARD,
			InputConstants.KEY_O,
			KeyMapping.Category.INVENTORY
	);

	@Override
	public void onInitializeClient() {
		KeyMappingHelper.registerKeyMapping(DROP_ALL_KEY);
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			boolean pressed = false;
			while (DROP_ALL_KEY.consumeClick()) {
				pressed = true;
			}
			if (pressed) {
				dropAll(client);
			}
		});
	}

	private static void dropAll(Minecraft client) {
		LocalPlayer player = client.player;
		if (player == null || client.gameMode == null || player.isSpectator()) {
			return;
		}

		InventoryMenu menu = player.inventoryMenu;
		for (int slot = InventoryMenu.ARMOR_SLOT_START; slot <= InventoryMenu.SHIELD_SLOT; slot++) {
			if (!menu.getSlot(slot).getItem().isEmpty()) {
				// Button 1 with THROW drops the whole stack, like Ctrl+Q.
				client.gameMode.handleContainerInput(menu.containerId, slot, 1, ContainerInput.THROW, player);
			}
		}
	}
}
