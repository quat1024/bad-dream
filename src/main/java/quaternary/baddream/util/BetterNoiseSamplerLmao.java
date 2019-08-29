package quaternary.baddream.util;

import net.minecraft.util.math.noise.NoiseSampler;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.util.math.noise.PerlinNoiseSampler;

import java.util.Random;

/**
 * OctavePerlinNoiseSamplers return values in an unexpected range.
 * The range of the values seems to depend on the number of octaves.
 * I like my noise generators to return values between 0 and 1, though.
 * So here we are.
 */
public class BetterNoiseSamplerLmao implements NoiseSampler {
	public BetterNoiseSamplerLmao(Random random, int octaves) {
		samplers = new PerlinNoiseSampler[octaves];
		
		for(int i = 0; i < octaves; i++) {
			samplers[i] = new PerlinNoiseSampler(random);
		}
		
		//Don't judge me it works
		//1 over the series (1 + 1/2 + 1/4 + 1/8...), divided by 2
		outputScale = 1d / (2d - (1d / Math.pow(2, octaves))) / 2d;
	}
	
	private final double outputScale;
	
	private final PerlinNoiseSampler[] samplers;
	
	public double sample(double x, double y, double z) {
		double acc = 0;
		double amplitude = 1;
		
		for(PerlinNoiseSampler sampler : samplers) {
			acc += amplitude * sampler.sample(
				OctavePerlinNoiseSampler.maintainPrecision(x * amplitude),
				OctavePerlinNoiseSampler.maintainPrecision(y * amplitude),
				OctavePerlinNoiseSampler.maintainPrecision(z * amplitude),
				0, //what are these even for
				0
			);
			
			amplitude *= 0.5;
		}
		
		return (acc * outputScale) + 0.5;
	}
	
	@Override
	public double sample(double x, double y, double z, double wtf) {
		return sample(x, y, z);
	}
}
