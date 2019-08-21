package quaternary.baddream.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import quaternary.baddream.net.ServerMessages;
import quaternary.baddream.world.dim.DreamDimensionUtils;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CreateDimensionCommand {
	public static void register(CommandDispatcher<ServerCommandSource> disp) {
		disp.register(
			literal("baddream-createDimension")
				.requires(src -> src.hasPermissionLevel(2))
				.then(
					argument("player", EntityArgumentType.player())
						.executes(ctx ->
							execute(ctx.getSource(), EntityArgumentType.getPlayer(ctx, "player"))
						)
				)
				.executes(ctx ->
					execute(ctx.getSource(), ctx.getSource().getPlayer())
				)
		);
	}
	
	public static int execute(ServerCommandSource source, ServerPlayerEntity player) {
		boolean existed = DreamDimensionUtils.ensureCreated(player);
		if(existed) {
			source.sendFeedback(new TranslatableText("baddream.command.createdimension.existed", player.getName()), false);
		} else {
			source.sendFeedback(new TranslatableText("baddream.command.createdimension.created", player.getName()), false);
		}
		
		ServerMessages.sendAllDimensions(player);
		
		return 0;
	}
}
