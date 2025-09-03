package com.baizeli.eternisstarrysky;

import com.baizeli.eternisstarrysky.config.ConfigEffect;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import net.minecraftforge.common.ForgeConfigSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Configuration
{
	private static final String KEY_ETERNIS_APPLE_EFFECTS = "effects";
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final File ETERNIS_APPLE_CONFIG = new File("./config/eternisstarrysky-EternisApple.json");
	public static final Logger LOGGER = LoggerFactory.getLogger("Configuration");
	public static final AtomicReference<List<ConfigEffect>> ETERNIS_APPLE_EFFECTS;
	public static final ForgeConfigSpec.DoubleValue WHISPER_OF_THE_PAST_DAMAGE;
	public static final ForgeConfigSpec SPECIFICATION;

	public static void save()
	{
		saveEternisApple();
	}

	public static void saveEternisApple()
	{
		JsonObject eternisApple = new JsonObject();
		JsonArray effects = new JsonArray();
		for (ConfigEffect eff : ETERNIS_APPLE_EFFECTS.get())
		{
			JsonArray effect = new JsonArray();
			effect.add(eff.key);
			effect.add(eff.duration);
			effect.add(eff.amplifier);
			effects.add(effect);
		}
		eternisApple.add(KEY_ETERNIS_APPLE_EFFECTS, effects);
		String config = GSON.toJson(eternisApple);
		try (FileWriter writer = new FileWriter(ETERNIS_APPLE_CONFIG))
		{
			writer.write(config);
			writer.flush();
		}
		catch (IOException e)
		{
			LOGGER.error("Cannot save Eternis Apple config", e);
		}
	}

	static
	{
		// Eternis Apple
		List<ConfigEffect> defaultEffects = List.of(
			new ConfigEffect("minecraft:regeneration", 600, 9),
			new ConfigEffect("minecraft:resistance", 600, 3),
			new ConfigEffect("minecraft:absorption", 600, 9)
		);
		ETERNIS_APPLE_EFFECTS = new AtomicReference<>(defaultEffects);

		boolean newFile = false;
		try
		{
			newFile = ETERNIS_APPLE_CONFIG.createNewFile();
			if (newFile)
				saveEternisApple();
		}
		catch (IOException e)
		{
			LOGGER.error("Cannot create Eternis Apple config", e);
		}

		if (!newFile)
		{
			try (FileReader reader = new FileReader(ETERNIS_APPLE_CONFIG))
			{
				JsonObject eternisApple = JsonParser.parseReader(reader).getAsJsonObject();
				if (eternisApple.get(KEY_ETERNIS_APPLE_EFFECTS).isJsonArray())
				{
					JsonArray effects = eternisApple.get(KEY_ETERNIS_APPLE_EFFECTS).getAsJsonArray();
					List<ConfigEffect> configEffects = new LinkedList<>();
					for (JsonElement element : effects)
					{
						if (!element.isJsonArray())
							continue;

						JsonArray effect = element.getAsJsonArray();
						if (effect.size() != 3)
							continue;

						JsonElement e0 = effect.get(0);
						JsonElement e1 = effect.get(1);
						JsonElement e2 = effect.get(2);
						if (!(e0.isJsonPrimitive()) || !(e1.isJsonPrimitive()) || !(e2.isJsonPrimitive()))
							continue;

						JsonPrimitive p0 = e0.getAsJsonPrimitive();
						JsonPrimitive p1 = e1.getAsJsonPrimitive();
						JsonPrimitive p2 = e2.getAsJsonPrimitive();
						if (!p0.isString())
							continue;
						if (!p1.isNumber())
							continue;
						if (!p2.isNumber())
							continue;

						ConfigEffect eff = new ConfigEffect();
						eff.key = p0.getAsString();
						eff.duration = p1.getAsInt();
						eff.amplifier = p2.getAsInt();
						configEffects.add(eff);
					}
					ETERNIS_APPLE_EFFECTS.set(List.copyOf(configEffects));
				}
			}
			catch (IOException e)
			{
				LOGGER.error("Cannot read Eternis Apple config", e);
			}
		}

		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		WHISPER_OF_THE_PAST_DAMAGE = builder
			.comment("Damage of Whisper of the Past")
			.defineInRange("WHISPER_OF_THE_PAST_DAMAGE", 60, 0.0, Double.MAX_VALUE);
		SPECIFICATION = builder.build();
	}
}
