package quaternary.baddream.mixin.prstole;

import net.minecraft.entity.Entity;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import quaternary.baddream.util.KnockoffFabricDimensionInternals;

@Mixin(Entity.class)
public abstract class EntityMixin {
	// Inject right before the direction vector is retrieved by the game
	@Inject(method = "changeDimension", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getLastPortalDirectionVector()Lnet/minecraft/util/math/Vec3d;"))
	private void onGetPortal(DimensionType dimension, CallbackInfoReturnable<Entity> cir) {
		KnockoffFabricDimensionInternals.prepareDimensionalTeleportation((Entity)(Object)this);
	}
}
