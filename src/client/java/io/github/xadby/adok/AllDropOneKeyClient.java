package io.github.xadby.adok;

import com.mojang.blaze3d.platform.InputConstants;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.HashedStack;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.InventoryMenu;

public class AllDropOneKeyClient implements ClientModInitializer {
	public static final String MOD_ID = "adok";

	private static final KeyMapping DROP_ALL_KEY = new KeyMapping(
			"key.adok.drop_all",
			InputConstants.Type.KEYBOARD,
			InputConstants.KEY_U,
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
		ClientPacketListener connection = client.getConnection();
		if (player == null || connection == null || player.isSpectator()) {
			return;
		}

		// Send the click packets directly instead of going through MultiPlayerGameMode:
		// its client-side simulation also sends a creative drop packet in Creative mode,
		// which makes the server spawn every stack twice.
		InventoryMenu menu = player.inventoryMenu;
		HashedStack carried = HashedStack.create(menu.getCarried(), connection.decoratedHashOpsGenenerator());
		for (int slot = InventoryMenu.ARMOR_SLOT_START; slot <= InventoryMenu.SHIELD_SLOT; slot++) {
			if (!menu.getSlot(slot).getItem().isEmpty()) {
				// Button 1 with THROW drops the whole stack, like Ctrl+Q.
				connection.send(new ServerboundContainerClickPacket(
						menu.containerId,
						menu.getStateId(),
						(short) slot,
						(byte) 1,
						ContainerInput.THROW,
						Int2ObjectMaps.emptyMap(),
						carried
				));
			}
		}
	}
}
