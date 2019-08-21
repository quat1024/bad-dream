package quaternary.baddream.mixin;

import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import quaternary.baddream.net.ServerMessages;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
	@Inject(
		at = @At("RETURN"),
		method = "onPlayerConnect"
	)
	public void onOnPlayerConnect(ClientConnection conn, ServerPlayerEntity player, CallbackInfo ci) {
		ServerMessages.sendAllDimensions(player);
	}
}
