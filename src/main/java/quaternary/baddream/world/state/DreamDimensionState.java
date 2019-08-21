package quaternary.baddream.world.state;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.dimension.DimensionType;

import java.util.UUID;

public class DreamDimensionState extends PersistentState {
	public DreamDimensionState() {
		super(name());
	}
	
	public static String name() {
		return "baddream-data";
	}
	
	public static DreamDimensionState forServer(MinecraftServer server) {
		return server.getWorld(DimensionType.OVERWORLD).getPersistentStateManager().getOrCreate(DreamDimensionState::new, name());
	}
	
	private Int2ObjectOpenHashMap<UUID> dimensionMap = new Int2ObjectOpenHashMap<>();
	
	public void forEachDimension(DimensionConsumer action) {
		if(dimensionMap.isEmpty()) return;
		
		dimensionMap.int2ObjectEntrySet().fastIterator().forEachRemaining(e -> action.consume(e.getIntKey(), e.getValue()));
	}
	
	public int count() {
		return dimensionMap.size();
	}
	
	public void put(int rawId, UUID dreamerUuid) {
		dimensionMap.put(rawId, dreamerUuid);
		markDirty();
	}
	
	public void remove(int rawId) {
		dimensionMap.remove(rawId);
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		ListTag list = new ListTag();
		
		ObjectIterator<Int2ObjectMap.Entry<UUID>> entryerator = dimensionMap.int2ObjectEntrySet().fastIterator();
		entryerator.forEachRemaining(e -> {
			CompoundTag pair = new CompoundTag();
			pair.putInt("rawId", e.getIntKey());
			pair.putUuid("dreamer", e.getValue());
			list.add(pair);
		});
		
		tag.put(name(), list);
		return tag;
	}
	
	@Override
	public void fromTag(CompoundTag tag) {
		dimensionMap.clear();
		
		ListTag list = tag.getList(name(), 10);
		for(Tag t : list) {
			CompoundTag pair = (CompoundTag) t;
			dimensionMap.put(pair.getInt("rawId"), pair.getUuid("dreamer"));
		}
	}
	
	public interface DimensionConsumer {
		void consume(int rawId, UUID dreamerUuid);
	}
}
