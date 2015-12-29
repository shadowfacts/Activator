package net.shadowfacts.activator.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.Achievement;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
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

	@SideOnly(Side.CLIENT)
	protected IIcon top;
	@SideOnly(Side.CLIENT)
	protected IIcon frontHorizontal;
	@SideOnly(Side.CLIENT)
	protected IIcon frontVertical;

	protected BlockActivator(Material material) {
		super(material);
	}

	BlockActivator() {
		this(Material.rock);
		if (ActivatorConfig.basicEnabled) setCreativeTab(CreativeTabs.tabMisc);
		setBlockName("activator");
		setHardness(1f);
		setHarvestLevel("pickaxe", 2);
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
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		if (side == 0 || side == 1) {
			if (side == meta) {
				return frontVertical;
			} else {
				return top;
			}
		} else {
			if (side == meta) {
				return frontHorizontal;
			} else {
				return blockIcon;
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		this.blockIcon = p_149651_1_.registerIcon("furnace_side");
		this.top = p_149651_1_.registerIcon("furnace_top");
		this.frontHorizontal = p_149651_1_.registerIcon("dispenser_front_horizontal");
		this.frontVertical = p_149651_1_.registerIcon("dispenser_front_vertical");
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
