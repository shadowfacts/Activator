package net.shadowfacts.activator.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.shadowfacts.activator.Activator;

/**
 * @author shadowfacts
 */
public class ClientProxy extends CommonProxy {

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);

		registerInvModel(Activator.blocks.activator, 0, "activator");
		registerInvModel(Activator.blocks.redstoneActivator, 0, "activator");
		registerInvModel(Activator.blocks.rfActivator, 0, "activator");
		registerInvModel(Activator.items.gear, 0, "gear");
	}

	private static void registerInvModel(Block block, int meta, String id) {
		registerInvModel(Item.getItemFromBlock(block), meta, id);
	}

	private static void registerInvModel(Item item, int meta, String id) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
				.register(item, meta, new ModelResourceLocation(Activator.modId + ":" + id, "inventory"));
	}

	@Override
	public World getClientWorld() {
		return Minecraft.getMinecraft().theWorld;
	}

}
