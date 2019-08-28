package quaternary.baddream.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.world.WorldSaveHandler;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import quaternary.baddream.BadDream;
import quaternary.baddream.world.dim.DreamDimensionType;
import quaternary.baddream.world.state.DreamDimensionState;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
	@Inject(
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/dimension/DimensionType;getAll()Ljava/lang/Iterable;"
		),
		method = "createWorlds"
	)
	public void onCreateWorlds(WorldSaveHandler wsl, LevelProperties props, LevelInfo info, WorldGenerationProgressListener progressListener, CallbackInfo ci) {
		DreamDimensionState state = DreamDimensionState.forServer((MinecraftServer) (Object) this);
		
		//state.forEachDimension(DreamDimensionType::forceRegister);
		state.forEachDimension((rawId, dreamerUuid) -> {
			BadDream.LOGGER.info("[baddream] Just-in-time registering dream dimension for {} to raw ID {}", dreamerUuid, rawId);
			DreamDimensionType.forceRegister(rawId, dreamerUuid);
		});
	}
}
