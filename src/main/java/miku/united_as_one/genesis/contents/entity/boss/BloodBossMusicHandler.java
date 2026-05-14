package miku.united_as_one.genesis.contents.entity.boss;

import io.redspace.ironsspellbooks.api.util.IMusicHandler;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.FadeableSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

import java.util.HashSet;
import java.util.Set;

public class BloodBossMusicHandler implements IMusicHandler {
    private final Set<FadeableSoundInstance> layers = new HashSet<>();
    private final SoundManager soundManager;
    private final SoundEvent musicEvent;
    private FadeableSoundInstance mainMusic;
    private boolean done = false;

    public BloodBossMusicHandler(SoundEvent musicEvent) {
        this.soundManager = Minecraft.getInstance().getSoundManager();
        this.musicEvent = musicEvent;
    }

    private void addLayer(FadeableSoundInstance soundInstance) {
        // 清理已停止的音效
        layers.removeIf(sound -> sound.isStopped() || !soundManager.isActive(sound));

        soundManager.play(soundInstance);
        layers.add(soundInstance);
    }

    @Override
    public void init() {
        // 停止其他音乐
        soundManager.stop(null, SoundSource.MUSIC);

        // 创建并播放主音乐（循环播放）
        mainMusic = new FadeableSoundInstance(musicEvent, SoundSource.RECORDS, true);
        mainMusic.fadeIn(40); // 淡入效果
        addLayer(mainMusic);
        done = false;
    }

    @Override
    public void stop() {
        if (mainMusic != null) {
            mainMusic.triggerStop(); // 淡出停止
        }
    }

    @Override
    public void tick() {

    }

    @Override
    public boolean isDone() {
        return done || (mainMusic != null && mainMusic.isStopped());
    }

    @Override
    public void hardStop() {
        if (mainMusic != null) {
            soundManager.stop(mainMusic);
        }
        layers.clear();
        done = true;
    }

    @Override
    public void triggerResume() {
        if (mainMusic != null) {
            mainMusic.triggerStart();
            if (!soundManager.isActive(mainMusic)) {
                soundManager.play(mainMusic);
            }
        }
    }
}