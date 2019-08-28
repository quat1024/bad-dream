package quaternary.baddream.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import quaternary.baddream.util.DreamPlayerDataProvider;
import quaternary.baddream.world.data.DreamPlayerData;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements DreamPlayerDataProvider {
	private static final String BADDREAM$KEY = "baddream-playerdata";
	
	private DreamPlayerData baddream$playerData = null;
	
	@Override
	public DreamPlayerData getOrCreateDreamPlayerData() {
		if(baddream$playerData == null) {
			baddream$playerData = new DreamPlayerData((PlayerEntity) (Object) this);
		}
		
		return baddream$playerData;
	}
	
	@Inject(
		at = @At("RETURN"),
		method = "writeCustomDataToTag"
	)
	private void onWriteCustomDataToTag(CompoundTag tag, CallbackInfo ci) {
		if(baddream$playerData != null) {
			tag.put(BADDREAM$KEY, baddream$playerData.toTag());
		}
	}
	
	@Inject(
		at = @At("RETURN"),
		method = "readCustomDataFromTag"
	)
	private void onReadCustomDataFromTag(CompoundTag tag, CallbackInfo ci) {
		if(tag.containsKey(BADDREAM$KEY)) {
			getOrCreateDreamPlayerData().fromTag(tag.getCompound(BADDREAM$KEY));
		}
	}
}
