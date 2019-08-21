package quaternary.baddream;

import net.fabricmc.api.ClientModInitializer;
import quaternary.baddream.net.ClientMessages;

public class BadDreamClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientMessages.initialize();
	}
}
