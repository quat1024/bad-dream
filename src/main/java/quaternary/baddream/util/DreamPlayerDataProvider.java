package quaternary.baddream.util;

import net.minecraft.entity.player.PlayerEntity;
import quaternary.baddream.world.data.DreamPlayerData;

//Mixed on to PlayerEntity
public interface DreamPlayerDataProvider {
	DreamPlayerData getOrCreateDreamPlayerData();
}
