package quaternary.baddream.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import quaternary.baddream.BadDream;

public class BadDreamItems {
	public static ItemGroup ITEMGROUP;
	
	public static DreamJournalItem DREAM_JOURNAL;
	
	public static void initialize() {
		ITEMGROUP = FabricItemGroupBuilder
			.create(new Identifier(BadDream.MODID, "group"))
			.icon(() -> new ItemStack(DREAM_JOURNAL))
			.build();
		
		DREAM_JOURNAL = register(
			new DreamJournalItem(settings().rarity(Rarity.UNCOMMON)),
			"dream_journal"
		);
	}
	
	private static <T extends Item> T register(T item, String name) {
		return Registry.register(Registry.ITEM, new Identifier(BadDream.MODID, name), item);
	}
	
	private static Item.Settings settings() {
		return new Item.Settings().group(ITEMGROUP);
	}
}
