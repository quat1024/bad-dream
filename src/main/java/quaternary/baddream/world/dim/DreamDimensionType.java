package quaternary.baddream.world.dim;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.SecondaryServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.dimension.DimensionType;
import quaternary.baddream.BadDream;
import quaternary.baddream.mixin.MinecraftServerAccessor;
import quaternary.baddream.util.MinecraftServerUtils;
import quaternary.baddream.world.state.DreamDimensionState;

import java.io.File;
import java.util.UUID;

public class DreamDimensionType extends DimensionType {
	public static int RAW_ID_START = 69;
	
	public static DreamDimensionType getOrCreate(ServerPlayerEntity player) {
		UUID dreamerUuid = player.getUuid();
		Identifier id = idFor(dreamerUuid);
		
		if(Registry.DIMENSION.containsId(id)) {
			return (DreamDimensionType) Registry.DIMENSION.get(id);
		} else {
			//Register the new dimension (using a raw ID so it sticks across world reloads)
			//TODO, don't play raw ID games when the dimensions PR is merged haha
			int nextRawId = findFreeRawId((SimpleRegistry<?>) Registry.DIMENSION, RAW_ID_START);
			DreamDimensionType type = forceRegister(nextRawId, dreamerUuid);
			
			MinecraftServer server = player.server;
			
			//Create the World on the server (very important and doesn't happen automatically!)
			MinecraftServerUtils.addNewDimensionWorld(server, type);
			
			//Save that this new dimension exists in the auxillary file so it will be recreated next server startup
			DreamDimensionState.forServer(server).put(nextRawId, dreamerUuid);
			
			return type;
		}
	}
	
	public static boolean has(PlayerEntity player) {
		return has(player.getUuid());
	}
	
	public static boolean has(UUID dreamerUuid) {
		return Registry.DIMENSION.containsId(idFor(dreamerUuid));
	}
	
	public static int findFreeRawId(SimpleRegistry<?> reg, int start) {
		while(reg.get(start) != null) {
			start++;
		}
		return start;
	}
	
	public static DreamDimensionType forceRegister(int rawId, UUID dreamerUuid) {
		return ((SimpleRegistry<DimensionType>) Registry.DIMENSION).set(rawId, idFor(dreamerUuid), createNew(dreamerUuid));
	}
	
	private static Identifier idFor(UUID dreamerUuid) {
		return new Identifier(BadDream.MODID, "dreamdim_" + uuidToString(dreamerUuid));
	}
	
	private static DreamDimensionType createNew(UUID dreamerUuid) {
		return new DreamDimensionType(BadDream.MODID + "_dreamdim_" + uuidToString(dreamerUuid), dreamerUuid);
	}
	
	private static String uuidToString(UUID uuid) {
		//dashes in registry names just look weird... lol
		return uuid.toString().replace('-', '_');
	}
	
	//Cribbed a little bit abt. how to do dimensions from pyrofab's and draylar's dimension PR
	public DreamDimensionType(String name, UUID dreamerUuid) {
		super(
			3, //"Raw ID" (goes unused due to overriding getRawID)
			name, //name
			"dreams" + File.pathSeparator + name, //save directory
			(world, type) -> new DreamDimension(world, (DreamDimensionType) type, dreamerUuid), //`Dimension` factory
			true //yes, they have skylight
		);
		
		this.dreamerUuid = dreamerUuid;
	}
	
	private final UUID dreamerUuid;
	
	public UUID getDreamerUuid() {
		return dreamerUuid;
	}
	
	@Override
	public int getRawId() {
		return Registry.DIMENSION.getRawId(this);
	}
	
	@Override
	public File getFile(File baseSaveDir) {
		//use regular toString because dashes in directory names are more acceptable looking
		return new File(new File(baseSaveDir, "dreams"), dreamerUuid.toString());
	}
}
