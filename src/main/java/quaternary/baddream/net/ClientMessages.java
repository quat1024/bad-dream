package quaternary.baddream.net;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.dimension.DimensionType;
import quaternary.baddream.world.dim.DreamDimensionType;
import quaternary.baddream.world.state.DreamDimensionState;

public class ClientMessages {
	public static void initialize() {
		ClientSidePacketRegistry.INSTANCE.register(MessageIds.ALL_DIMENSIONS, (ctx, buffer) -> {
			System.out.println("HELLO SERVER!!!!");
			
			DreamDimensionState receivedState = new DreamDimensionState();
			int count = buffer.readInt();
			for(int i = 0; i < count; i++) {
				receivedState.put(buffer.readInt(), buffer.readUuid());
			}
			
			ctx.getTaskQueue().execute(() -> {
				receivedState.forEachDimension((rawId, dreamerUuid) -> {
					System.out.println("HELLO SERVER " + rawId + " " + dreamerUuid);
					
					DimensionType type = Registry.DIMENSION.get(rawId);
					if(type == null) {
						DreamDimensionType.forceRegister(rawId, dreamerUuid);
					} else if(type instanceof DreamDimensionType) {
						DreamDimensionType existingType = (DreamDimensionType) type;
						if(!existingType.getDreamerUuid().equals(dreamerUuid)) {
							//Apparently a different dimension exists with this raw ID?
							DreamDimensionType.forceRegister(rawId, dreamerUuid);
						}
					} else {
						//would this ever happen? anyway, I don't wanna have to deal
						//with managing differing raw IDs between client and server...
						throw new RuntimeException("Refusing to stomp on existing dimension with raw ID " + rawId + " even though the server told me to!");
					}
				});
			});
		});
	}
}
