package net.shadowfacts.activator.proxy;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.shadowfacts.activator.Activator;
import net.shadowfacts.activator.network.PacketUpdateTE;

/**
 * @author shadowfacts
 */
public class ClientProxy extends CommonProxy {

	@Override
	public World getClientWorld() {
		return Minecraft.getMinecraft().theWorld;
	}

}
