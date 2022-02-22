package dev.cubxity.server;

import net.minestom.server.instance.ChunkGenerator;
import net.minestom.server.instance.ChunkPopulator;
import net.minestom.server.instance.batch.ChunkBatch;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class VoidChunkGenerator implements ChunkGenerator {
    @Override
    public void generateChunkData(@NotNull ChunkBatch batch, int chunkX, int chunkZ) {

    }

    @Override
    public @Nullable List<ChunkPopulator> getPopulators() {
        return null;
    }
}
