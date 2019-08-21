package quaternary.baddream.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import quaternary.baddream.world.dim.DreamDimensionUtils;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class WakeCommand {
	public static void register(CommandDispatcher<ServerCommandSource> disp) {
		disp.register(
			literal("baddream-wake")
				.requires(src -> src.hasPermissionLevel(2))
				.then(
					argument("player", EntityArgumentType.player())
						.executes(ctx ->
							execute(EntityArgumentType.getPlayer(ctx, "player"))
						)
				)
				.executes(ctx ->
					execute(ctx.getSource().getPlayer())
				)
		);
	}
	
	public static int execute(ServerPlayerEntity player) {
		DreamDimensionUtils.endDreaming(player);
		return 0;
	}
}
