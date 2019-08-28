package quaternary.baddream.world.dim;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.dimension.DimensionType;
import quaternary.baddream.BadDream;
import quaternary.baddream.util.KnockoffFabricDimensionInternals;
import quaternary.baddream.world.data.DreamPlayerData;

public class DreamDimensionUtils {
	public static boolean beginDreaming(ServerPlayerEntity player, boolean graceful) {
		try {
			if(graceful) {
				DreamPlayerData.get(player).beginDreamPre();
			}
			
			KnockoffFabricDimensionInternals.changeDimension(player, DreamDimensionType.getOrCreate(player));
			
			if(graceful) {
				DreamPlayerData.get(player).beginDreamPost();
			}
			
			return true;
		} catch(Exception e) {
			BadDream.LOGGER.fatal("[baddream] Encountered a problem when beginning " + player.getEntityName() + "'s dream!", e);
			return false;
		}
	}
	
	public static boolean endDreaming(ServerPlayerEntity player, boolean graceful) {
		try {
			if(graceful) {
				DreamPlayerData.get(player).endDreamPre();
			}
			
			KnockoffFabricDimensionInternals.changeDimension(player, DimensionType.OVERWORLD);
			
			if(graceful) {
				DreamPlayerData.get(player).endDreamPost();
			}
			
			return true;
		} catch(Exception e) {
			BadDream.LOGGER.fatal("[baddream] Encountered a problem when waking up " + player.getEntityName(), e);
			return false;
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
