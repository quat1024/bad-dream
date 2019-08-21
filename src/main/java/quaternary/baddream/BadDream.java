package quaternary.baddream;

import net.fabricmc.api.ModInitializer;
import quaternary.baddream.command.BadDreamCommands;
import quaternary.baddream.item.BadDreamItems;
import quaternary.baddream.net.ServerMessages;

public class BadDream implements ModInitializer {
	public static final String MODID = "baddream";
	
	@Override
	public void onInitialize() {
		BadDreamCommands.initialize();
		ServerMessages.initialize();
		//blocks
		BadDreamItems.initialize();
	}
}
