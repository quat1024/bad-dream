package quaternary.baddream;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import quaternary.baddream.command.BadDreamCommands;
import quaternary.baddream.item.BadDreamItems;
import quaternary.baddream.net.ServerMessages;
import quaternary.baddream.world.gen.BadDreamChunkGenerators;
import quaternary.baddream.world.gen.biome.BadDreamBiomes;

public class BadDream implements ModInitializer {
	public static final String MODID = "baddream";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	
	@Override
	public void onInitialize() {
		BadDreamCommands.initialize();
		ServerMessages.initialize();
		
		//blocks
		BadDreamItems.initialize();
		
		BadDreamChunkGenerators.initialize();
		BadDreamBiomes.initialize();
	}
}
