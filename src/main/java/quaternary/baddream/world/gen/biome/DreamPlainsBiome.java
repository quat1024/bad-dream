package quaternary.baddream.world.gen.biome;

import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.CountExtraChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.LakeDecoratorConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.LakeFeatureConfig;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;

public class DreamPlainsBiome extends Biome {
	public DreamPlainsBiome() {
		super(
			new Biome.Settings()
				.configureSurfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_CONFIG)
				.precipitation(Precipitation.RAIN)
				.category(Category.PLAINS)
				.depth(0.3f) //a bit more "depth" whatever this is
				.scale(0.4f)
				.temperature(0.6f) //a bit colder than plains biome
				.downfall(0.4f)
				.waterColor(0x3fa6e4) //more blue-green
				.waterFogColor(0x050533)
				.parent("plains")
		);
		
		addFeature(GenerationStep.Feature.LOCAL_MODIFICATIONS, Biome.configureFeature(Feature.LAKE, new LakeFeatureConfig(Blocks.WATER.getDefaultState()), Decorator.WATER_LAKE, new LakeDecoratorConfig(15)));
		
		DefaultBiomeFeatures.addPlainsFeatures(this);
		DefaultBiomeFeatures.addExtraDefaultFlowers(this);
		DefaultBiomeFeatures.addSprings(this);
		DefaultBiomeFeatures.addDefaultMushrooms(this);
		DefaultBiomeFeatures.addDefaultVegetation(this);
		DefaultBiomeFeatures.addDefaultDisks(this);
		
		addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Biome.configureFeature(Feature.NORMAL_TREE, FeatureConfig.DEFAULT, Decorator.COUNT_EXTRA_HEIGHTMAP, new CountExtraChanceDecoratorConfig(0, 0.1F, 2)));
	}
}
