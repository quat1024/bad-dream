package quaternary.baddream.util;

import com.google.common.base.Preconditions;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.DimensionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import quaternary.baddream.BadDream;
import quaternary.baddream.mixin.prstole.EntityAccessor;
import quaternary.baddream.world.data.DreamPlayerData;
import quaternary.baddream.world.dim.DreamDimensionType;

public final class KnockoffFabricDimensionInternals {
	private KnockoffFabricDimensionInternals() { throw new AssertionError(); }
	
	/** The entity currently being transported to another dimension */
	private static final ThreadLocal<Entity> PORTAL_ENTITY = new ThreadLocal<>();
	///** The custom placement logic passed from {@link FabricDimensions#teleport(Entity, DimensionType, EntityPlacer)}*/
	//private static final ThreadLocal<EntityPlacer> CUSTOM_PLACEMENT = new ThreadLocal<>();
	
	/*
	 * The dimension change hooks consist of two steps:
	 * - First, we memorize the currently teleported entity, and set required fields
	 * - Then, we retrieve the teleported entity in the placement logic in PortalForcer#getPortal
	 *   and use it to call the entity placers
	 * This lets us use the exact same logic for any entity and prevent the vanilla getPortal (which has unwanted
	 * side effects) from running, while keeping the patches minimally invasive.
	 *
	 * Shortcomings: bugs may arise if another patch cancels the teleportation method between
	 * #prepareDimensionalTeleportation and #tryFindPlacement, AND a mod calls PortalForcer#getPortal directly
	 * right after.
	 */
	
	public static void prepareDimensionalTeleportation(Entity entity) {
		Preconditions.checkNotNull(entity);
		PORTAL_ENTITY.set(entity);
		// Set values used by `PortalForcer#changeDimension` to prevent a NPE crash.
		EntityAccessor access = ((EntityAccessor) entity);
		if (entity.getLastPortalDirectionVector() == null) {
			access.setLastPortalDirectionVector(entity.getRotationVector());
		}
		if (entity.getLastPortalDirection() == null) {
			access.setLastPortalDirection(entity.getHorizontalFacing());
		}
	}
	
	/* Nullable */
	public static BlockPattern.TeleportTarget tryFindPlacement(ServerWorld destination, Direction portalDir, double portalX, double portalY) {
		Preconditions.checkNotNull(destination);
		Entity teleported = PORTAL_ENTITY.get();
		PORTAL_ENTITY.set(null);
		// If the entity is null, the call does not come from a vanilla context
		if (teleported == null) {
			return null;
		}
		
		//entering a dream
		DimensionType dimType = destination.getDimension().getType();
		if(dimType instanceof DreamDimensionType) {
			return new BlockPattern.TeleportTarget(
				new Vec3d(0, 100, 0), //pos (TODO)
				new Vec3d(0, 0, 0), //velocity
				0 //yaw
			);
		}
		
		//leaving a dream
		if(teleported instanceof PlayerEntity && dimType == DimensionType.OVERWORLD) {
			DreamPlayerData data = DreamPlayerData.get((PlayerEntity) teleported);
			if(data.isDreaming()) {
				return new BlockPattern.TeleportTarget(
					data.getEntryPos(),
					new Vec3d(0, 0, 0),
					(int) data.getEntryYaw()
				);
			}
		}
		
		// Vanilla / other implementations logic, undefined behaviour on custom dimensions
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <E extends Entity> E changeDimension(E teleported, DimensionType dimension/*, EntityPlacer placement*/) {
		try {
			//CUSTOM_PLACEMENT.set(placement);
			return (E) teleported.changeDimension(dimension);
		} finally {
			//CUSTOM_PLACEMENT.set(null);
		}
	}
	
}