package dev.cubxity.server;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.*;
import net.minestom.server.extras.velocity.VelocityProxy;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MicroQueue {
    public static void main(String[] args) {
        long startTime = System.nanoTime();
        Logger logger = LoggerFactory.getLogger(MicroQueue.class);

        logger.info("Starting MicroQueue.");

        // Configuration
        MicroQueueConfig config = new MicroQueueConfig();
        config.saveDefaults();
        config.load();

        System.setProperty("minestom.chunk-view-distance", String.valueOf(config.getViewDistance()));

        // Initialization
        MinecraftServer server = MinecraftServer.init();
        MinecraftServer.setBrandName(config.getBrandName());
        MinecraftServer.setCompressionThreshold(config.getCompressionThreshold());

        GlobalEventHandler eventHandler = MinecraftServer.getGlobalEventHandler();
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();

        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();
        instanceContainer.setTime(18_000);
        instanceContainer.setTimeRate(0);
        instanceContainer.setTimeUpdate(null);
        instanceContainer.setChunkGenerator(new VoidChunkGenerator());
        instanceContainer.enableAutoChunkLoad(true);

        /* Y=62 */
        instanceContainer.setBlock(0, 62, 0, Block.BARRIER);

        /* Y=63 */
        instanceContainer.setBlock(1, 63, 0, Block.BARRIER);
        instanceContainer.setBlock(-1, 63, 0, Block.BARRIER);
        instanceContainer.setBlock(0, 63, 1, Block.BARRIER);
        instanceContainer.setBlock(0, 63, -1, Block.BARRIER);

        /* Y=64 */
        instanceContainer.setBlock(1, 64, 0, Block.BARRIER);
        instanceContainer.setBlock(-1, 64, 0, Block.BARRIER);
        instanceContainer.setBlock(0, 64, 1, Block.BARRIER);
        instanceContainer.setBlock(0, 64, -1, Block.BARRIER);

        /* Y=65 */
        instanceContainer.setBlock(0, 65, 0, Block.BARRIER);

        Pos spawnPoint = new Pos(0.5, 63.0, 0.5);

        // Events
        eventHandler.addListener(PlayerLoginEvent.class, event -> {
            Player player = event.getPlayer();
            player.setFlying(true);
            player.setAutoViewable(false);
            player.setRespawnPoint(spawnPoint);

            event.setSpawningInstance(instanceContainer);
        });

        eventHandler.addListener(PlayerSpawnEvent.class, event -> {
            Player player = event.getPlayer();

            player.setGameMode(GameMode.ADVENTURE);
            player.setEnableRespawnScreen(false);

            player.setNoGravity(true);

            player.addEffect(new Potion(PotionEffect.BLINDNESS, Byte.MAX_VALUE, Integer.MAX_VALUE));
            player.addEffect(new Potion(PotionEffect.INVISIBILITY, Byte.MAX_VALUE, Integer.MAX_VALUE));
            player.addEffect(new Potion(PotionEffect.SLOWNESS, Byte.MAX_VALUE, Integer.MAX_VALUE));
        });

        eventHandler.addListener(PlayerMoveEvent.class, event -> {
            if (!event.getNewPosition().sameBlock(event.getPlayer().getPosition())) {
                event.setCancelled(true);
            }
        });

        eventHandler.addListener(PlayerChatEvent.class, event -> {
            event.setCancelled(true);
        });

        // Velocity support
        String velocitySecret = System.getProperty("VELOCITY_SECRET");
        if (velocitySecret != null) {
            logger.info("Velocity supported enabled.");
            VelocityProxy.enable(velocitySecret);
        }

        // Start
        server.start("0.0.0.0", config.getServerPort());

        // Start complete
        double timeElapsed = (System.nanoTime() - startTime) / 1.0E6;
        logger.info("Done (" + timeElapsed + " ms)! For help, type nothing.");
    }
}
