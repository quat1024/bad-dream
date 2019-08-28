package quaternary.baddream.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.dimension.DimensionType;
import quaternary.baddream.BadDream;
import quaternary.baddream.mixin.MinecraftServerAccessor;
import quaternary.baddream.util.DeletingFileVisitor;
import quaternary.baddream.world.data.DreamPlayerData;
import quaternary.baddream.world.dim.DreamDimensionType;
import quaternary.baddream.world.dim.DreamDimensionUtils;
import quaternary.baddream.world.state.DreamDimensionState;

import java.nio.file.Files;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class BadDreamCommands {
	public static void initialize() {
		CommandRegistry.INSTANCE.register(false, (disp) -> {
			
			disp.register(
				literal("baddream")
				.requires(src -> src.hasPermissionLevel(2))
				.then(sleepWake("sleep", DreamDimensionUtils::beginDreaming))
				.then(sleepWake("wake", DreamDimensionUtils::endDreaming))
				.then(createDim())
				.then(swapInventories())
				.then(deleteDreams())
			);
		});
	}
	
	private static LiteralArgumentBuilder<ServerCommandSource> sleepWake(String literal, BiFunction<ServerPlayerEntity, Boolean, Boolean> action) {
		return literal(literal)
			.then(withPlayer(literal("forcefully"), "subject", (ctx, player) -> action.apply(player, false) ? 0 : 1))
			.then(withPlayer(literal("gracefully"), "subject", (ctx, player) -> action.apply(player, true) ? 0 : 1));
	}
	
	private static LiteralArgumentBuilder<ServerCommandSource> createDim() {
		return withPlayer(literal("create-dimension"), "subject", (ctx, player) -> {
			boolean existed = DreamDimensionUtils.ensureCreated(player);
			if(existed) {
				ctx.getSource().sendFeedback(new TranslatableText("baddream.command.createdimension.existed", player.getName()), true);
			} else {
				ctx.getSource().sendFeedback(new TranslatableText("baddream.command.createdimension.created", player.getName()), true);
			}
			return 0;
		});
	}
	
	private static LiteralArgumentBuilder<ServerCommandSource> swapInventories() {
		return withPlayer(literal("swap-inventories"), "subject", (ctx, player) -> {
			DreamPlayerData.get(player).swapInventories();
			return 0;
		});
	}
	
	private static LiteralArgumentBuilder<ServerCommandSource> deleteDreams() {
		return literal("delete")
			.then(
				literal("all-dreams-PLEASE-BE-CAREFUL-WITH-THIS")
					.requires(src -> src.hasPermissionLevel(4))
					.executes(ctx -> {
						MinecraftServer server = ctx.getSource().getMinecraftServer();
						Map<DimensionType, ServerWorld> worlds = ((MinecraftServerAccessor) server).getWorlds();
						Set<DreamDimensionType> deletThis = new HashSet<>();
						
						//TODO NORELEASE: change this to look for logged-out players, instead of just online players
						//TODO NORELEASE: delete files in dreams/ that are not listed in baddream-data (to avoid orphans)
						for(Map.Entry<DimensionType, ServerWorld> entry : worlds.entrySet()) {
							DimensionType type = entry.getKey();
							if(type instanceof DreamDimensionType) {
								DreamDimensionType dream = (DreamDimensionType) type; 
								ServerWorld world = entry.getValue();
								if(world.getPlayers().isEmpty()) {
									deletThis.add(dream);
								} else {
									ctx.getSource().sendFeedback(new TranslatableText("baddream.command.delete.cant", dream.getDreamerUuid().toString()), true);
								}
							}
						}
						
						if(deletThis.isEmpty()) {
							ctx.getSource().sendFeedback(new TranslatableText("baddream.command.delete.nothing"), true);
							return 1;
						}
						
						DreamDimensionState state = DreamDimensionState.forServer(server);
						boolean anyProblems = false;
						
						for(DreamDimensionType delet : deletThis) {
							state.remove(delet.getRawId());
							ServerWorld world = worlds.remove(delet);
							
							try {
								DeletingFileVisitor visitor = new DeletingFileVisitor();
								Files.walkFileTree(delet.getFile(world.getSaveHandler().getWorldDir()).toPath(), visitor);
								visitor.throwAllErrors();
							} catch(Exception e) {
								BadDream.LOGGER.error("Problem when deleting world", e);
								ctx.getSource().sendFeedback(new TranslatableText("baddream.command.delete.failed", delet.getDreamerUuid()), true);
								anyProblems = true;
							}
							
							ctx.getSource().sendFeedback(new TranslatableText("baddream.command.delete.deleted", delet.getDreamerUuid()), true);
						}
						
						return anyProblems ? 1 : 0;
					})
			)
			.then(
				literal("old")
				.executes(ctx -> {
					ctx.getSource().sendFeedback(new LiteralText("work in progress"), true);
					return 1;
				})
			);
	}
	
	private static LiteralArgumentBuilder<ServerCommandSource> withPlayer(LiteralArgumentBuilder<ServerCommandSource> root, String paramName, BiFunction<CommandContext<ServerCommandSource>, ServerPlayerEntity, Integer> action) {
		return root.then(
			argument(paramName, EntityArgumentType.player())
				.executes(ctx ->
					action.apply(ctx, EntityArgumentType.getPlayer(ctx, paramName))
				)
		)
		.executes(ctx ->
			action.apply(ctx, ctx.getSource().getPlayer())
		);
	}
}
