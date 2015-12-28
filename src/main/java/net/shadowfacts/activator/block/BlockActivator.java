package net.shadowfacts.activator.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.stats.Achievement;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.shadowfacts.activator.Activator;
import net.shadowfacts.activator.ActivatorConfig;
import net.shadowfacts.activator.achievement.AchievementProvider;
import net.shadowfacts.activator.achievement.ModAchievements;
import net.shadowfacts.activator.gui.GUI;
import net.shadowfacts.activator.tileentity.TileEntityActivator;

/**
 * @author shadowfacts
 */
public class BlockActivator extends Block implements ITileEntityProvider, AchievementProvider {

	protected BlockActivator(Material material) {
		super(material);
	}

	BlockActivator() {
		this(Material.rock);
		if (ActivatorConfig.basicEnabled) setCreativeTab(CreativeTabs.tabMisc);
		setBlockName("activator");
		setBlockTextureName(Activator.modId + ":activator");
		setHardness(.5f);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof TileEntityActivator) {
			((TileEntityActivator)te).dropInventory();
		}

		super.breakBlock(world, x, y, z, block, meta);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		player.openGui(Activator.instance, GUI.BASIC.ordinal(), world, x, y, z);
		return true;
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		if (side == world.getBlockMetadata(x, y, z)) {
			return Blocks.diamond_block.getIcon(side, side);
		}
		return super.getIcon(world, x, y, z, side);
	}

	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
		return side;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityActivator();
	}

	@Override
	public Achievement getAchievement() {
		return ModAchievements.craftActivator;
	}
}
