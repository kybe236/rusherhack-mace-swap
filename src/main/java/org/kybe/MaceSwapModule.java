package org.kybe;

import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import org.rusherhack.client.api.accessors.packet.IMixinServerboundInteractPacket;
import org.rusherhack.client.api.events.client.EventUpdate;
import org.rusherhack.client.api.events.network.EventPacket;
import org.rusherhack.client.api.feature.module.ModuleCategory;
import org.rusherhack.client.api.feature.module.ToggleableModule;
import org.rusherhack.core.event.subscribe.Subscribe;
import org.rusherhack.core.setting.BooleanSetting;

public class MaceSwapModule extends ToggleableModule {

	BooleanSetting onlyPlayers = new BooleanSetting("Only Players", true);

	public MaceSwapModule() {
		super("Mace Swap Module", "Mace Swap Module", ModuleCategory.CLIENT);

		this.registerSettings(onlyPlayers);
	}

	private int last_slot = -1;

	@Subscribe
	public void onUpdate(EventUpdate event) {
		if (mc.player == null || last_slot == -1 || mc.getConnection() == null) return;
		if (mc.player.getInventory().selected == last_slot) mc.getConnection().send(new ServerboundSetCarriedItemPacket(last_slot));
		last_slot = -1;
	}

	@Subscribe
	public void onPacketSend(EventPacket.Send event) {
		if (mc.player == null || mc.getConnection() == null || mc.level == null) return;
		if (event.getPacket() instanceof ServerboundInteractPacket packet) {
			if (packet.isUsingSecondaryAction()) return;

			if (onlyPlayers.getValue()) {
				Entity target = mc.level.getEntity(((IMixinServerboundInteractPacket) packet).getEntityId());
				if (!(target instanceof Player)) return;
			}

			last_slot = mc.player.getInventory().selected;
			int slot_of_mace = -1;
			for (int i = 0; i < 9; i++) {
				if (mc.player.getInventory().getItem(i).getItem() == Items.MACE) {
					slot_of_mace = i;
					break;
				}
			}

			if (slot_of_mace == -1) {
				last_slot = -1;
				return;
			}

			mc.getConnection().send(new ServerboundSetCarriedItemPacket(slot_of_mace));
		}
	}
}