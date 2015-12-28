package net.shadowfacts.activator.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.Achievement;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.shadowfacts.activator.Activator;
import net.shadowfacts.activator.achievement.ModAchievements;
import net.shadowfacts.activator.gui.GUI;
import net.shadowfacts.activator.tileentity.TileEntityRedstoneActivator;

/**
 * @author shadowfacts
 */
public class BlockRedstoneActivator extends BlockActivator {

	BlockRedstoneActivator() {
		super(Material.rock);
		setCreativeTab(CreativeTabs.tabMisc);
		setBlockName("activator.redstone");
		setBlockTextureName(Activator.modId + "activator-rf");
		setHardness(.5f);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		player.openGui(Activator.instance, GUI.REDSTONE.ordinal(), world, x, y, z);
		return true;
	}

	@Override
	public boolean shouldCheckWeakPower(IBlockAccess world, int x, int y, int z, int side) {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityRedstoneActivator();
	}

	@Override
	public Achievement getAchievement() {
		return ModAchievements.craftRedstoneActivator;
	}
}
