package net.shadowfacts.activator.proxy;

import cpw.mods.fml.relauncher.Side;
import net.shadowfacts.activator.Activator;
import net.shadowfacts.activator.network.PacketUpdateTE;

/**
 * @author shadowfacts
 */
public class ClientProxy extends CommonProxy {

	@Override
	protected void registerClientPackets() {
		Activator.network.registerMessage(PacketUpdateTE.ClientHandler.class, PacketUpdateTE.class, 0, Side.CLIENT);
	}
}
