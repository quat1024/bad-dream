package quaternary.baddream.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.WorldSaveHandler;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(MinecraftServer.class)
public interface MinecraftServerAccessor {
	@Accessor
	Map<DimensionType, ServerWorld> getWorlds();
	
	@Accessor
	WorldGenerationProgressListenerFactory getWorldGenerationProgressListenerFactory();
}
