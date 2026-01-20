package miku.united_as_one.genesis.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class AfterImageManager {
    private static final List<AfterImageData> afterImages = new ArrayList<>();
    private static final Map<String, Vec3> lastPlayerPositions = new HashMap<>();

    public static void tick() {
        afterImages.forEach(AfterImageData::tick);
        afterImages.removeIf(AfterImageData::isExpired);
        checkPlayerMovement();
    }

    private static void checkPlayerMovement() {
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer == null) return;
        String uuid = localPlayer.getStringUUID();
        Vec3 currentPos = localPlayer.position();
        Vec3 lastPos = lastPlayerPositions.get(uuid);

        if (lastPos != null) {
            double distance = currentPos.distanceTo(lastPos);
            if (distance > 0.2D && localPlayer.getDeltaMovement().horizontalDistance() > 0.2D) {
                createAfterImage(localPlayer);
            }
        }

        lastPlayerPositions.put(uuid, currentPos);
    }

    public static void createAfterImage(Player player) {
        if (afterImages.size() >= 10) {
            afterImages.remove(0);
        }
        
        afterImages.add(new AfterImageData(player));
    }

    public static List<AfterImageData> getAfterImages() {
        return afterImages;
    }

    public static void clear() {
        afterImages.clear();
        lastPlayerPositions.clear();
    }
}