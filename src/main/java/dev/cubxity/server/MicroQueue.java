package dev.cubxity.server;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;

public final class MicroQueue {
    public static void main(String[] args) {
        MinecraftServer server = MinecraftServer.init();
        GlobalEventHandler eventHandler = MinecraftServer.getGlobalEventHandler();
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();

        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();
        instanceContainer.setTime(18_000);
        instanceContainer.setTimeRate(0);
        instanceContainer.setTimeUpdate(null);
        instanceContainer.setChunkGenerator(new VoidChunkGenerator());
        instanceContainer.enableAutoChunkLoad(true);

        instanceContainer.setBlock(0, 62, 0, Block.BEDROCK);

        Pos spawnPoint = new Pos(0.5, 63.0, 0.5);

        eventHandler.addListener(PlayerLoginEvent.class, event -> {
            Player player = event.getPlayer();
            player.setFlying(true);
            player.setAutoViewable(false);
            player.setRespawnPoint(spawnPoint);

            event.setSpawningInstance(instanceContainer);
        });

        eventHandler.addListener(PlayerSpawnEvent.class, event -> {
            Player player = event.getPlayer();

            player.setGameMode(GameMode.SPECTATOR);
            player.setEnableRespawnScreen(false);

            player.setGravity(0.0, 0.0);
            player.setNoGravity(true);

            player.addEffect(new Potion(PotionEffect.BLINDNESS, Byte.MAX_VALUE, Integer.MAX_VALUE));
            player.addEffect(new Potion(PotionEffect.INVISIBILITY, Byte.MAX_VALUE, Integer.MAX_VALUE));
        });

        eventHandler.addListener(PlayerMoveEvent.class, event -> {
            event.setCancelled(true);
        });

        server.start("0.0.0.0", 25565);
    }
}
