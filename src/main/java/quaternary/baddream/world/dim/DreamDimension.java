package quaternary.baddream.world.dim;

import net.minecraft.block.Blocks;
import net.minecraft.util.SystemUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.biome.source.FixedBiomeSourceConfig;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorLayer;
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig;
import quaternary.baddream.world.gen.DreamChunkGenerator;
import quaternary.baddream.world.gen.biome.BadDreamBiomes;

import java.util.UUID;

public class DreamDimension extends Dimension {
	public DreamDimension(World world, DreamDimensionType type, UUID dreamerUuid) {
		super(world, type);
		this.type = type;
		this.dreamerUuid = dreamerUuid;
	}
	
	private final DreamDimensionType type;
	private final UUID dreamerUuid;
	
	@Override
	public ChunkGenerator<?> createChunkGenerator() {
		//TODO
		FixedBiomeSourceConfig biomeConfig = new FixedBiomeSourceConfig().setBiome(BadDreamBiomes.DREAM_PLAINS);
		BiomeSource biomeSource = new FixedBiomeSource(biomeConfig);
		
		return new DreamChunkGenerator(world, biomeSource, new OverworldChunkGeneratorConfig());
	}
	
	@Override
	public BlockPos getSpawningBlockInChunk(ChunkPos var1, boolean var2) {
		return var1.getCenterBlockPos(); //TODO what is this
	}
	
	@Override
	public BlockPos getTopSpawningBlockPosition(int var1, int var2, boolean var3) {
		return new BlockPos(30, 30, 30);
	}
	
	@Override
	public float getSkyAngle(long var1, float var3) {
		return 0;
	}
	
	@Override
	public boolean hasVisibleSky() {
		return true;
	}
	
	@Override
	public Vec3d getFogColor(float var1, float var2) {
		return new Vec3d(
			0.25, 
			0.27,
			0.26
		);
	}
	
	@Override
	public boolean canPlayersSleep() {
		return false;
	}
	
	@Override
	public boolean shouldRenderFog(int var1, int var2) {
		return false;
	}
	
	@Override
	public DimensionType getType() {
		return type;
	}
}
