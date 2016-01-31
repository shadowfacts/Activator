package net.shadowfacts.activator.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.shadowfacts.activator.Activator;
import net.shadowfacts.activator.ActivatorConfig;
import net.shadowfacts.activator.achievement.ModAchievements;
import net.shadowfacts.activator.gui.GUIs;
import net.shadowfacts.activator.tileentity.TileEntityRFActivator;

/**
 * @author shadowfacts
 */
public class BlockRFActivator extends BlockActivator {

	BlockRFActivator() {
		super(Material.rock);

		setUnlocalizedName("activator-rf");
		setHardness(1f);
		setHarvestLevel("pickaxe", 2);

		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));

		if (ActivatorConfig.rfEnabled) {
			setCreativeTab(CreativeTabs.tabMisc);
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
		player.openGui(Activator.instance, GUIs.RF.ordinal(), world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public boolean shouldCheckWeakPower(IBlockAccess world, BlockPos pos, EnumFacing side) {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityRFActivator();
	}

	@Override
	public Achievement getAchievement(ItemStack stack) {
		return ModAchievements.craftRFActivator;
	}

}
