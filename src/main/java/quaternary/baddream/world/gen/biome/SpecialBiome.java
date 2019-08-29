package quaternary.baddream.world.gen.biome;

import java.util.Random;

public interface SpecialBiome {
	default int getSpikeRarity(Random random) {
		return 1000;
	}
	
	default int getSpikeHeightSubtraction(Random random) {
		return 3;
	}
	
	default	int getPerturbRarity(Random random) {
		return 15;
	}
}
