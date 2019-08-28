package quaternary.baddream.world.data;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.math.Vec3d;
import quaternary.baddream.util.DreamPlayerDataProvider;

public class DreamPlayerData {
	public DreamPlayerData(PlayerEntity player) {
		this.player = player;
		this.inactiveInventory = new PlayerInventory(player);
	}
	
	public static DreamPlayerData get(PlayerEntity player) {
		return ((DreamPlayerDataProvider) player).getOrCreateDreamPlayerData();
	}
	
	private final PlayerEntity player;
	
	private final PlayerInventory inactiveInventory;
	
	private boolean isDreaming;
	private Vec3d entryPos;
	private float entryYaw;
	private float entryPitch;
	
	///
	
	public void beginDreamPre() {
		entryPos = player.getPos();
		entryYaw = player.yaw;
		entryPitch = player.pitch;
	}
	
	public void beginDreamPost() {
		swapInventories();
		isDreaming = true;
	}
	
	public void endDreamPre() {
	}
	
	public void endDreamPost() {
		swapInventories();
		isDreaming = false;
	}
	
	public void swapInventories() {
		PlayerInventory temp = new PlayerInventory(player);
		//TODO... pr "clone" to "cloneFrom" lol
		temp.clone(player.inventory);
		player.inventory.clone(inactiveInventory);
		inactiveInventory.clone(temp);
	}
	
	public boolean isDreaming() {
		return isDreaming;
	}
	
	public Vec3d getEntryPos() {
		return entryPos;
	}
	
	public float getEntryYaw() {
		return entryYaw;
	}
	
	public float getEntryPitch() {
		return entryPitch;
	}
	
	public CompoundTag toTag() {
		CompoundTag tag = new CompoundTag();
		tag.put("inactiveInventory", inactiveInventory.serialize(new ListTag()));
		tag.putBoolean("isDreaming", isDreaming);
		
		if(isDreaming) {
			CompoundTag entryTag = new CompoundTag();
			entryTag.putDouble("x", entryPos.x);
			entryTag.putDouble("y", entryPos.y);
			entryTag.putDouble("z", entryPos.z);
			entryTag.putFloat("yaw", entryYaw);
			entryTag.putFloat("pitch", entryPitch);
			tag.put("dreamEntry", entryTag);
		}
		return tag;
	}
	
	public void fromTag(CompoundTag tag) {
		inactiveInventory.deserialize(tag.getList("inactiveInventory", 10));
		isDreaming = tag.getBoolean("isDreaming");
		if(isDreaming) {
			CompoundTag entryTag = tag.getCompound("dreamEntry");
			entryPos = new Vec3d(entryTag.getDouble("x"), entryTag.getDouble("y"), entryTag.getDouble("z"));
			entryYaw = entryTag.getFloat("yaw");
			entryPitch = entryTag.getFloat("pitch");
		}
	}
}
