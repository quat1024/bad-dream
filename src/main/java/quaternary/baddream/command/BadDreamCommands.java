package quaternary.baddream.command;

import net.fabricmc.fabric.api.registry.CommandRegistry;

public class BadDreamCommands {
	public static void initialize() {
		CommandRegistry.INSTANCE.register(false, (disp) -> {
			SleepCommand.register(disp);
			WakeCommand.register(disp);
			CreateDimensionCommand.register(disp);
		});
	}
}
