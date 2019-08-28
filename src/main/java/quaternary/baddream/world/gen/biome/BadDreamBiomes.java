package quaternary.baddream.world.gen.biome;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import quaternary.baddream.BadDream;

public class BadDreamBiomes {
	public static DreamPlainsBiome DREAM_PLAINS;
	
	public static void initialize() {
		DREAM_PLAINS = register(new DreamPlainsBiome(), "dream_plains");
	}
	
	private static <B extends Biome> B register(B biome, String name) {
		return Registry.register(Registry.BIOME, new Identifier(BadDream.MODID, name), biome);
	}
}
