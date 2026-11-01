package dev.doctor4t.wathe.mixin.compat.sodium;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.doctor4t.wathe.client.WatheClient;
import net.caffeinemc.mods.sodium.client.render.chunk.LocalSectionIndex;
import net.caffeinemc.mods.sodium.client.render.chunk.RenderSection;
import net.caffeinemc.mods.sodium.client.render.chunk.occlusion.OcclusionCuller;
import net.caffeinemc.mods.sodium.client.render.viewport.Viewport;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(OcclusionCuller.class)
public abstract class OcclusionCullerMixin {

//    @WrapOperation(
//            method = "isWithinFrustum",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/caffeinemc/mods/sodium/client/render/viewport/Viewport;isBoxVisible(III)Z"
//            ),
//            remap = false
//    )
//    private static boolean wrapIsBoxVisible(
//            Viewport instance, int intOriginX, int intOriginY, int intOriginZ, Operation<Boolean> original
//    ) {
//        return original.call(instance, intOriginX, intOriginY, intOriginZ) || WatheClient.isTrainMoving();
//    }




    @WrapMethod(method = "isWithinFrustum")
    private static boolean wathe$isWithinFrustum(Viewport viewport, RenderSection section, Operation<Boolean> original) {
        if (WatheClient.isTrainMoving()) {
            float trainSpeed = WatheClient.getTrainSpeed();
            int chunkSize = 16;
            int tileWidth = 15 * chunkSize;
            int height = 116;
            int tileLength = 32 * chunkSize;
            int tileSize = tileLength * 3;
            float time = WatheClient.trainComponent.getTime() + MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true);

//            BlockPos blockPos = new BlockPos(
//                    section.getOriginX(),
//                    section.getOriginY(),
//                    section.getOriginZ()
//            );
//
//            boolean trainSection = ChunkSectionPos.getSectionCoord(blockPos.getY()) >= 4;
//            float v1 = (float) ((double) blockPos.getX());
//            float v2 = (float) ((double) blockPos.getY());
//            float v3 = (float) ((double) blockPos.getZ());
//            int zSection = blockPos.getZ() / chunkSize - ChunkSectionPos.getSectionCoord(camera.intZ);
//
//            float finalX = v1;
//            float finalY = v2;
//            float finalZ = v3;
//
//            if (zSection <= -8) {
//                finalX = ((v1 - tileLength + ((time) / 73.8f * trainSpeed)) % tileSize - tileSize / 2f);
//                finalY = (v2 + height);
//                finalZ = v3 + tileWidth;
//            } else if (zSection >= 8) {
//                finalX = ((v1 + tileLength + ((time) / 73.8f * trainSpeed)) % tileSize - tileSize / 2f);
//                finalY = (v2 + height);
//                finalZ = v3 - tileWidth;
//            } else if (!trainSection) {
//                finalX = ((v1 + ((time) / 73.8f * trainSpeed)) % tileSize - tileSize / 2f);
//                finalY = (v2 + height); // + zSection * 16;
//                finalZ = v3;
//            }
//
//            finalX = (blockPos.getX() - finalX) - camera.fracX;
//            finalY = (blockPos.getY() - finalY) - camera.fracY;
//            finalZ = (blockPos.getZ() - finalZ) - camera.fracZ;
            BlockPos blockPos = new BlockPos(
                    section.getOriginX() + LocalSectionIndex.unpackX(section.getSectionIndex()) * 16,
                    section.getOriginY() + LocalSectionIndex.unpackY(section.getSectionIndex()) * 16,
                    section.getOriginZ() + LocalSectionIndex.unpackZ(section.getSectionIndex()) * 16
            );

            boolean trainSection = ChunkSectionPos.getSectionCoord(section.getOriginY()) >= 4;

            float v1 = (float) ((double) blockPos.getX());
            float v2 = (float) ((double) blockPos.getY());
            float v3 = (float) ((double) blockPos.getZ());

//            int zSection = section.getChunkZ() / chunkSize;
            int zSection = blockPos.getZ() / chunkSize - ChunkSectionPos.getSectionCoord(viewport.getTransform().intZ);


            float finalX = v1;
            float finalY = v2;
            float finalZ = v3;

            if (zSection <= -8) {
                finalX = ((v1 - tileLength + ((time) / 73.8f * trainSpeed)) % tileSize - tileSize / 2f);
                finalY = (v2 + height);
                finalZ = v3 + tileWidth;
            } else if (zSection >= 8) {
                finalX = ((v1 + tileLength + ((time) / 73.8f * trainSpeed)) % tileSize - tileSize / 2f);
                finalY = (v2 + height);
                finalZ = v3 - tileWidth;
            } else if (!trainSection) {
                finalX = ((v1 + ((time) / 73.8f * trainSpeed)) % tileSize - tileSize / 2f);
                finalY = (v2 + height); // + zSection * 16;
                finalZ = v3;
            }

            finalX = (blockPos.getX() - finalX) - viewport.getTransform().fracX;
            finalY = (blockPos.getY() - finalY) - viewport.getTransform().fracY;
            finalZ = (blockPos.getZ() - finalZ) - viewport.getTransform().fracZ;

            return viewport.isBoxVisible(Math.round(finalX), Math.round(finalY), Math.round(finalZ)) || trainSection;




//
//
//
//            if (zSection <= 8) {
//                return viewport.isBoxVisible(
//                        Math.round((v1 - tileLength + ((time) / 73.8f * trainSpeed)) % tileSize - tileSize / 2f),
//                        Math.round(v2 + height),
//                        Math.round(v3 + tileWidth)
//                );
//            }
//            else if (zSection >= 8) {
//                return viewport.isBoxVisible(
//                        Math.round((v1 + tileLength + ((time) / 73.8f * trainSpeed)) % tileSize - tileSize / 2f),
//                        Math.round(v2 + height),
//                        Math.round(v3 - tileWidth)
//                );
//            }
//            else if (!trainSection) {
//                return viewport.isBoxVisible(
//                        Math.round((v1 + ((time) / 73.8f * trainSpeed)) % tileSize - tileSize / 2f),
//                        Math.round(v2 + height),
//                        Math.round(v3)
//                );
//            }
        }
        return original.call(viewport, section);
    }
}
