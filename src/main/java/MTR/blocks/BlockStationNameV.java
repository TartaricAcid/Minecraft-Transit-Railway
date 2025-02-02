package MTR.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStationNameV extends BlockStationNameBase {

	private static final String name = "BlockStationNameLMC";

	public BlockStationNameV() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 21;
	}

	public static String getName() {
		return name;
	}
}