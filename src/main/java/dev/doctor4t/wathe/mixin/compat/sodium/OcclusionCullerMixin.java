package dev.doctor4t.wathe.mixin.compat.sodium;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.doctor4t.wathe.client.WatheClient;
import net.caffeinemc.mods.sodium.client.render.chunk.occlusion.OcclusionCuller;
import net.caffeinemc.mods.sodium.client.render.viewport.Viewport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(OcclusionCuller.class)
public class OcclusionCullerMixin {

    @WrapOperation(
            method = "isWithinFrustum",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/caffeinemc/mods/sodium/client/render/viewport/Viewport;isBoxVisible(III)Z"
            ),
            remap = false
    )
    private static boolean wrapIsBoxVisible(
            Viewport instance, int intOriginX, int intOriginY, int intOriginZ, Operation<Boolean> original
    ) {
        return original.call(instance, intOriginX, intOriginY, intOriginZ) || WatheClient.isTrainMoving();
    }
}
