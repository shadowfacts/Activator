package net.shadowfacts.activator.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.Achievement;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.shadowfacts.activator.Activator;
import net.shadowfacts.activator.ActivatorConfig;
import net.shadowfacts.activator.achievement.ModAchievements;
import net.shadowfacts.activator.gui.GUI;
import net.shadowfacts.activator.tileentity.TileEntityRFActivator;

/**
 * @author shadowfacts
 */
public class BlockRFActivator extends BlockActivator {

	BlockRFActivator() {
		super(Material.rock);
		if (ActivatorConfig.rfEnabled) setCreativeTab(CreativeTabs.tabMisc);
		setBlockName("activator.rf");
		setBlockTextureName(Activator.modId + ":activator-rf");
		setHardness(.5f);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		player.openGui(Activator.instance, GUI.RF.ordinal(), world, x, y, z);
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityRFActivator();
	}

	@Override
	public Achievement getAchievement() {
		return ModAchievements.craftRFActivator;
	}
}
