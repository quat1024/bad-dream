package quaternary.baddream.mixin.prstole;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Stolen from the dimension PR #319
@Mixin(DimensionType.class)
public abstract class DimensionTypeMixin {
	@Inject(
		at = @At("RETURN"),
		method = "byRawId",
		cancellable = true
	)
	private static void byRawId(int id, CallbackInfoReturnable<DimensionType> cir) {
		if(cir.getReturnValue() == null || cir.getReturnValue().getRawId() != id) {
			for(DimensionType type : Registry.DIMENSION) {
				if(type.getRawId() == id) {
					cir.setReturnValue(type);
					return;
				}
			}
		}
	}
}
