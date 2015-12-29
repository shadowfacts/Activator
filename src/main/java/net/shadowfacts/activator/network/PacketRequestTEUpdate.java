package net.shadowfacts.activator.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.shadowfacts.activator.tileentity.BaseTileEntity;

/**
 * @author shadowfacts
 */
public class PacketRequestTEUpdate implements IMessage {

	private int x;
	private int y;
	private int z;
	private int dim;

	public PacketRequestTEUpdate() {

	}

	public PacketRequestTEUpdate(int x, int y, int z, int dim) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.dim = dim;
	}

	public PacketRequestTEUpdate(BaseTileEntity te) {
		this(te.xCoord, te.yCoord, te.zCoord, te.getWorldObj().provider.dimensionId);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		dim = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(dim);
	}

	public static class ServerHandler implements IMessageHandler<PacketRequestTEUpdate, PacketUpdateTE> {

		@Override
		public PacketUpdateTE onMessage(PacketRequestTEUpdate msg, MessageContext ctx) {
			return new PacketUpdateTE(((BaseTileEntity)MinecraftServer.getServer().worldServerForDimension(msg.dim).getTileEntity(msg.x, msg.y, msg.z)));
		}

	}
}
