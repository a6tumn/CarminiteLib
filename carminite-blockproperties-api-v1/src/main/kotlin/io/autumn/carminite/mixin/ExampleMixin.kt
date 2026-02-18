package io.autumn.carminite.mixin

import net.minecraft.server.MinecraftServer
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Suppress("NonJavaMixin")
@Mixin(MinecraftServer::class)
open class ExampleMixin {

    @Inject(method = ["loadLevel"], at = [At("HEAD")])
    private fun init(info: CallbackInfo) {
        // This code is injected into the start of MinecraftServer.loadLevel()V
    }
}
