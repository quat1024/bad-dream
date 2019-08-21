package quaternary.baddream.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.SecondaryServerWorld;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.dimension.DimensionType;
import quaternary.baddream.mixin.MinecraftServerAccessor;

import java.util.Map;

public class MinecraftServerUtils {
	public static void addNewDimensionWorld(MinecraftServer server, DimensionType type) {
		Map<DimensionType, ServerWorld> worldsMap = ((MinecraftServerAccessor) server).getWorlds();
		
		worldsMap.put(type, new SecondaryServerWorld(
			server.getWorld(DimensionType.OVERWORLD),
			server,
			server.getWorkerExecutor(),
			server.getWorld(DimensionType.OVERWORLD).getSaveHandler(),
			type,
			server.getProfiler(),
			((MinecraftServerAccessor) server).getWorldGenerationProgressListenerFactory().create(11)
		));
	}
}
