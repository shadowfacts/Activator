package net.shadowfacts.activator.misc;

import com.mojang.authlib.GameProfile;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.shadowfacts.activator.tileentity.TileEntityActivator;

import java.util.UUID;

/**
 * @author shadowfacts
 */
public class ActivatorFakePlayer extends FakePlayer {

	private TileEntityActivator activator;

	public ActivatorFakePlayer(TileEntityActivator activator) {
		super((WorldServer)activator.getWorldObj(), new GameProfile(UUID.fromString("52693A97-8EDB-5135-FE67-24B3600AEC87"), "[Activator]"));
		this.activator = activator;
	}

}
