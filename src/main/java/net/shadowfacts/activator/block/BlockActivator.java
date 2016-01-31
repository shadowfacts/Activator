package net.shadowfacts.activator.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.shadowfacts.activator.Activator;
import net.shadowfacts.activator.ActivatorConfig;
import net.shadowfacts.activator.achievement.ModAchievements;
import net.shadowfacts.activator.gui.GUIs;
import net.shadowfacts.activator.tileentity.TileEntityActivator;
import net.shadowfacts.shadowmc.achievement.AchievementProvider;

/**
 * @author shadowfacts
 */
public class BlockActivator extends Block implements ITileEntityProvider, AchievementProvider {

	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	protected BlockActivator(Material material) {
		super(material);
	}

	BlockActivator() {
		this(Material.rock);
		setHardness(1f);
		setHarvestLevel("pickaxe", 2);
		setUnlocalizedName("activator");

		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));

		if (ActivatorConfig.basicEnabled) {
			setCreativeTab(CreativeTabs.tabMisc);
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileEntityActivator) {
			((TileEntityActivator)te).dropInventory();
		}

		super.breakBlock(world, pos, state);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
		player.openGui(Activator.instance, GUIs.BASIC.ordinal(), world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		world.setBlockState(pos, state.withProperty(FACING, getDirection(pos, placer)));
	}

	private EnumFacing getDirection(BlockPos pos, EntityLivingBase entity) {
		return EnumFacing.getFacingFromVector(
				(float)(entity.posX - pos.getX()),
				(float)(entity.posY - pos.getY()),
				(float)(entity.posZ - pos.getZ()));
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, FACING);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityActivator();
	}

	@Override
	public Achievement getAchievement(ItemStack stack) {
		return ModAchievements.craftActivator;
	}

}
