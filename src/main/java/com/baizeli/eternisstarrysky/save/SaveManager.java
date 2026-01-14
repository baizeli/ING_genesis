package com.baizeli.eternisstarrysky.save;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.client.renderer.spell.chaos.WireBoxRenderer;
import com.baizeli.eternisstarrysky.genesis_core.utils.EventUtil;
import com.baizeli.eternisstarrysky.network.DeadListSyncPacket;
import com.baizeli.eternisstarrysky.network.WireBoxSyncPacket;
import com.baizeli.eternisstarrysky.spell.chaos.ReversePlagueSpell;
import com.google.gson.*;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.network.PacketDistributor;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

import static com.baizeli.eternisstarrysky.EternisStarrySky.CHANNEL;

public class SaveManager {
    private static Path SAVE_PATH;
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
            .create();

    public static void init(MinecraftServer server) {
        SAVE_PATH = server.getWorldPath(LevelResource.ROOT)
                .resolve(EternisStarrySky.MODID + "_save.json");
    }


    public static void save() {
        JsonObject root = new JsonObject();

        JsonObject dataMap = new JsonObject();
        for (Map.Entry<UUID, Long> entry : WireBoxRenderer.entitiesForRenderWireBoxRenderer.entrySet()) {
            dataMap.addProperty(entry.getKey().toString(), entry.getValue());
        }
        root.add("entityData", dataMap);

        JsonObject rotMap = new JsonObject();
        for (Map.Entry<UUID, Float> entry : WireBoxRenderer.entityRotationMap.entrySet()) {
            rotMap.addProperty(entry.getKey().toString(), entry.getValue());
        }
        root.add("rotations", rotMap);

        JsonObject axisMap = new JsonObject();
        for (Map.Entry<UUID, Direction.Axis> entry : WireBoxRenderer.entityAxisMap.entrySet()) {
            axisMap.addProperty(entry.getKey().toString(), entry.getValue().name());
        }
        root.add("axes", axisMap);

        JsonObject entityMap = new JsonObject();
        for (Map.Entry<UUID, UUID> entry : ReversePlagueSpell.entityMap.entrySet()) {
            entityMap.addProperty(entry.getKey().toString(), entry.getValue().toString());
        }
        root.add("entity", entityMap);

        JsonArray deadList = new JsonArray();
        for (UUID entityUuid : EventUtil.deadList) {
            deadList.add(entityUuid.toString());
        }
        root.add("deadList", deadList);

        try (Writer writer = Files.newBufferedWriter(SAVE_PATH)) {
            GSON.toJson(root, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        if (!Files.exists(SAVE_PATH)) return;

        try (Reader reader = Files.newBufferedReader(SAVE_PATH)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();

            JsonObject dataMap = root.getAsJsonObject("entityData");
            for (String key : dataMap.keySet()) {
                UUID uuid = UUID.fromString(key);
                long time = dataMap.get(key).getAsLong();
                WireBoxRenderer.entitiesForRenderWireBoxRenderer.put(uuid, time);
            }

            JsonObject rotMap = root.getAsJsonObject("rotations");
            for (String key : rotMap.keySet()) {
                UUID uuid = UUID.fromString(key);
                float rot = rotMap.get(key).getAsFloat();
                WireBoxRenderer.entityRotationMap.put(uuid, rot);
            }

            JsonObject axisMap = root.getAsJsonObject("axes");
            for (String key : axisMap.keySet()) {
                UUID uuid = UUID.fromString(key);
                Direction.Axis axis = Direction.Axis.valueOf(axisMap.get(key).getAsString());
                WireBoxRenderer.entityAxisMap.put(uuid, axis);
            }

            JsonObject entityMap = root.getAsJsonObject("entity");
            for (String key : entityMap.keySet()) {
                UUID uuid = UUID.fromString(key);
                UUID uuid1 = UUID.fromString(entityMap.get(key).getAsString());
                ReversePlagueSpell.entityMap.put(uuid, uuid1);
            }

            JsonArray deadList = root.getAsJsonArray("deadList");
            if (deadList != null) {
                for (JsonElement entityUuid : deadList) {
                    EventUtil.deadList.add(UUID.fromString(entityUuid.getAsString()));
                }
            }

            CHANNEL.send(
                    PacketDistributor.ALL.noArg(),
                    new DeadListSyncPacket(EventUtil.deadList)
            );

            CHANNEL.send(PacketDistributor.ALL.noArg(),
                    new WireBoxSyncPacket(WireBoxRenderer.entitiesForRenderWireBoxRenderer,
                            WireBoxRenderer.entityRotationMap,
                            WireBoxRenderer.entityAxisMap));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class UUIDTypeAdapter implements JsonSerializer<UUID>, JsonDeserializer<UUID> {
        public JsonElement serialize(UUID src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }

        public UUID deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return UUID.fromString(json.getAsString());
        }
    }
}