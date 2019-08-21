package quaternary.baddream.net;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.PacketByteBuf;
import quaternary.baddream.world.state.DreamDimensionState;

import java.util.UUID;

public class ServerMessages {
	public static void initialize() {
		
	}
	
	public static void sendAllDimensions(ServerPlayerEntity player) {
		MinecraftServer playerServer = player.server;
		DreamDimensionState state = DreamDimensionState.forServer(playerServer);
		
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		
		buf.writeInt(state.count());
		state.forEachDimension((rawId, dreamerUuid) -> {
			buf.writeInt(rawId);
			buf.writeUuid(dreamerUuid);
		});
		
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, MessageIds.ALL_DIMENSIONS, buf);
	}
}
