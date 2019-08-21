package quaternary.baddream.world.dim;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.dimension.DimensionType;
import quaternary.baddream.util.KnockoffFabricDimensionInternals;

public class DreamDimensionUtils {
	public static void beginDreaming(ServerPlayerEntity player) {
		try {
			KnockoffFabricDimensionInternals.changeDimension(player, DreamDimensionType.getOrCreate(player));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void endDreaming(ServerPlayerEntity player) {
		try {
			KnockoffFabricDimensionInternals.changeDimension(player, DimensionType.OVERWORLD);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return if the dimension already existed and didn't need to be registered
	 */
	public static boolean ensureCreated(ServerPlayerEntity player) {
		if(DreamDimensionType.has(player)) {
			return true; //existed
		}
		
		//used for sideeffects, ignore value
		DreamDimensionType.getOrCreate(player);
		return false;
	}
}
