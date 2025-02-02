package MTR.blocks;

import MTR.MTR;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockMTRMapSide extends BlockWithDirection {

	private static final String name = "BlockMTRMapSide";
	public static final PropertyBool SIDE = PropertyBool.create("side");
	public static final PropertyBool TOP = PropertyBool.create("top");

	public BlockMTRMapSide() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
		setLightLevel(1F);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(TOP, false));
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer) {
		if (facing != EnumFacing.UP && facing != EnumFacing.DOWN) {
			BlockPos pos2 = pos;
			facing = facing.rotateYCCW();
			switch (facing) {
			case NORTH:
				pos2 = pos.add(0, 0, -1);
				break;
			case EAST:
				pos2 = pos.add(1, 0, 0);
				break;
			case SOUTH:
				pos2 = pos.add(0, 0, 1);
				break;
			case WEST:
				pos2 = pos.add(-1, 0, 0);
				break;
			default:
				break;
			}
			if (worldIn.getBlockState(pos2).getBlock().isReplaceable(worldIn, pos2)
					&& worldIn.getBlockState(pos2.up()).getBlock().isReplaceable(worldIn, pos2.up())
					&& worldIn.getBlockState(pos.up()).getBlock().isReplaceable(worldIn, pos.up())) {
				worldIn.setBlockState(pos2, getDefaultState().withProperty(FACING, facing).withProperty(SIDE, true)
						.withProperty(TOP, false));
				worldIn.setBlockState(pos2.add(0, 1, 0), getDefaultState().withProperty(FACING, facing)
						.withProperty(SIDE, true).withProperty(TOP, true));
				worldIn.setBlockState(pos.add(0, 1, 0), getDefaultState().withProperty(FACING, facing)
						.withProperty(SIDE, false).withProperty(TOP, true));
				return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer)
						.withProperty(FACING, facing).withProperty(SIDE, false).withProperty(TOP, false);
			} else
				return Blocks.air.getDefaultState();
		}
		return Blocks.air.getDefaultState();
	}

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		BlockPos pos2 = pos;
		boolean side = state.getValue(SIDE), top = state.getValue(TOP);
		boolean b = false;
		switch (state.getValue(FACING)) {
		case NORTH:
			pos2 = pos.add(0, 0, side ? 1 : -1);
			break;
		case EAST:
			pos2 = pos.add(side ? -1 : 1, 0, 0);
			break;
		case SOUTH:
			pos2 = pos.add(0, 0, side ? -1 : 1);
			break;
		case WEST:
			pos2 = pos.add(side ? 1 : -1, 0, 0);
			break;
		default:
			break;
		}
		BlockPos pos3 = pos.add(0, top ? -1 : 1, 0), pos4 = pos2.add(0, top ? -1 : 1, 0);
		if (!(worldIn.getBlockState(pos2).getBlock() instanceof BlockMTRMapSide)
				|| !(worldIn.getBlockState(pos3).getBlock() instanceof BlockMTRMapSide)
				|| !(worldIn.getBlockState(pos4).getBlock() instanceof BlockMTRMapSide))
			worldIn.setBlockToAir(pos);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
		EnumFacing var3 = access.getBlockState(pos).getValue(FACING);
		switch (var3) {
		case NORTH:
			setBlockBounds(0.0F, 0.0F, 0.0F, 0.125F, 1.0F, 1.0F);
			break;
		case SOUTH:
			setBlockBounds(0.875F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			break;
		case EAST:
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.125F);
			break;
		case WEST:
			setBlockBounds(0.0F, 0.0F, 0.875F, 1.0F, 1.0F, 1.0F);
			break;
		default:
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote)
			MTR.proxy.openMapGUI();
		return true;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta % 4))
				.withProperty(SIDE, (meta & 4) > 0).withProperty(TOP, (meta & 8) > 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex() + (state.getValue(SIDE) ? 4 : 0)
				+ (state.getValue(TOP) ? 8 : 0);
	}

	@Override
	public BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, SIDE, TOP });
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		return null;
	}

	public static String getName() {
		return name;
	}
}
