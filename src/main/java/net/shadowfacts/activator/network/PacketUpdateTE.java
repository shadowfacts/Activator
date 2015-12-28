package net.shadowfacts.activator.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.shadowfacts.activator.Activator;
import net.shadowfacts.activator.tileentity.BaseTileEntity;

/**
 * @author shadowfacts
 */
public class PacketUpdateTE implements IMessage {

	private int x;
	private int y;
	private int z;
	private int dim;
	private NBTTagCompound data;

	public PacketUpdateTE() {

	}

	public PacketUpdateTE(int x, int y, int z, int dim, NBTTagCompound data) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.dim = dim;
		this.data = data;
	}

	public PacketUpdateTE(BaseTileEntity te) {
		this(te.xCoord, te.yCoord, te.zCoord, te.getWorldObj().provider.dimensionId, te.save(new NBTTagCompound()));
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		dim = buf.readInt();
		data = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(dim);
		ByteBufUtils.writeTag(buf, data);
	}

	public static class ServerHandler implements IMessageHandler<PacketUpdateTE, IMessage> {
		@Override
		public IMessage onMessage(PacketUpdateTE msg, MessageContext ctx) {

			World world = MinecraftServer.getServer().worldServerForDimension(msg.dim);
			TileEntity te = world.getTileEntity(msg.x, msg.y, msg.z);
			if (te instanceof BaseTileEntity) {
				((BaseTileEntity)te).load(msg.data);
			}

			return null;
		}
	}

	public static class ClientHandler implements IMessageHandler<PacketUpdateTE, IMessage> {
		@Override
		public IMessage onMessage(PacketUpdateTE msg, MessageContext ctx) {

			World world = Activator.proxy.getClientWorld();
			TileEntity te = world.getTileEntity(msg.x, msg.y, msg.z);
			if (te instanceof BaseTileEntity) {
				((BaseTileEntity)te).load(msg.data);
			}

			return null;
		}
	}
}
