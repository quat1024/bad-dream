package quaternary.baddream.world.dim;

import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.SystemUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.biome.source.FixedBiomeSourceConfig;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorLayer;

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
		FixedBiomeSourceConfig biomeConfig = new FixedBiomeSourceConfig().setBiome(Biomes.DESERT);
		BiomeSource biomeSource = new FixedBiomeSource(biomeConfig);
		
		FlatChunkGeneratorConfig flatConfig = new FlatChunkGeneratorConfig();
		flatConfig.setBiome(biomeConfig.getBiome());
		flatConfig.getLayers().add(new FlatChunkGeneratorLayer(10, Blocks.BONE_BLOCK));
		flatConfig.updateLayerBlocks();
		
		return ChunkGeneratorType.FLAT.create(
			world,
			biomeSource,
			flatConfig
		);
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
			0.05, 
			0.10,
			(Math.sin(SystemUtil.getMeasuringTimeMs() / 1000f) / 2f) + 0.5
		);
	}
	
	@Override
	public boolean canPlayersSleep() {
		return false;
	}
	
	@Override
	public boolean shouldRenderFog(int var1, int var2) {
		return true;
	}
	
	@Override
	public DimensionType getType() {
		return type;
	}
}
