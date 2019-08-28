package quaternary.baddream.world.gen;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import quaternary.baddream.BadDream;

public class BadDreamChunkGenerators {
	public static void initialize() {
		Registry.register(Registry.CHUNK_GENERATOR_TYPE, new Identifier(BadDream.MODID, "dream_chunkgen"), DreamChunkGenerator.TYPE);
	}
}
