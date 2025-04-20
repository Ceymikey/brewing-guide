package dev.ceymikey;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrewingGuide implements ModInitializer {
	public static final String MOD_ID = "brewing-guide";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
		LOGGER.info("Initializing Brewing Guide mod by Ceymikey!");
		PotionRecipeRegistry.registerVanillaRecipes();
		LOGGER.info("Registered vanilla brewing recipes!");
	}
}