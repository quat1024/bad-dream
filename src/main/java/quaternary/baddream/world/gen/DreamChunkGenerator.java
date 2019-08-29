package quaternary.baddream.world.gen;

import net.minecraft.entity.EntityCategory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;
import net.minecraft.world.gen.chunk.OverworldChunkGenerator;
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig;
import quaternary.baddream.util.BetterNoiseSamplerLmao;
import quaternary.baddream.world.gen.biome.SpecialBiome;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DreamChunkGenerator extends OverworldChunkGenerator {
	public DreamChunkGenerator(IWorld iWorld_1, BiomeSource biomeSource_1, OverworldChunkGeneratorConfig overworldChunkGeneratorConfig_1) {
		super(iWorld_1, biomeSource_1, overworldChunkGeneratorConfig_1);
	}
	
	private final BetterNoiseSamplerLmao noiseSampleBias = new BetterNoiseSamplerLmao(random, 4);
	
	@Override
	protected void sampleNoiseColumn(double[] doubles, int x, int z) {
		super.sampleNoiseColumn(doubles, x, z);
		
		Biome biome = biomeSource.getBiomeForNoiseGen(x, z);
		if(biome instanceof SpecialBiome) {
			SpecialBiome special = (SpecialBiome) biome;
			
			int spikeRarity = special.getSpikeRarity(random);
			if(spikeRarity != 0 && random.nextInt(spikeRarity) == 0) {
				Arrays.sort(doubles, 0, doubles.length - special.getSpikeHeightSubtraction(random));
			} else {
				for(int i = 0; i < doubles.length; i++) {
					int perturbRarity = special.getPerturbRarity(random);
					if(perturbRarity != 0) {
						if(random.nextInt(special.getPerturbRarity(random)) == 0) doubles[i] *= 3;
						if(random.nextInt(special.getPerturbRarity(random)) == 0) doubles[i] /= 3;
					}
					
					
				}
			}
		}
		
		/*
		double bias = 20 * noiseSampleBias.sample(x / 10d, z / 10d, 0);
		
		{
			for(int i = 0; i < doubles.length; i++) {
				if(random.nextInt(40) == 0) doubles[i] *= 3;
				if(random.nextInt(40) == 0) doubles[i] /= 3;
				if(i < doubles.length - 4) {
					double difference = (random.nextDouble()) - bias;
					if(difference > 0) doubles[i] += 0.1 * difference * (doubles.length - i);
				}
			}
		}*/
	}
	
	@Override
	public List<Biome.SpawnEntry> getEntitySpawnList(EntityCategory entityCategory_1, BlockPos blockPos_1) {
		return Collections.emptyList();
	}
	
	@Override
	public void spawnEntities(ServerWorld serverWorld_1, boolean boolean_1, boolean boolean_2) {
		return;
	}
	
	@Override
	public void populateEntities(ChunkRegion chunkRegion_1) {
		return;
	}
	
	//Avoiding the private functional interface...
	//pass in "null" and override the 1 method that uses the functional interface
	public static final ChunkGeneratorType<OverworldChunkGeneratorConfig, DreamChunkGenerator> TYPE = new ChunkGeneratorType<OverworldChunkGeneratorConfig, DreamChunkGenerator>(null, false, OverworldChunkGeneratorConfig::new) {
		@Override
		public DreamChunkGenerator create(World world, BiomeSource source, OverworldChunkGeneratorConfig config) {
			return new DreamChunkGenerator(world, source, config);
		}
	};
}
