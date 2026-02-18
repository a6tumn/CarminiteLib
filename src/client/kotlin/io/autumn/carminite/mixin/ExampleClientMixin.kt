package io.autumn.carminite.mixin

import net.minecraft.client.Minecraft
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Suppress("NonJavaMixin")
@Mixin(Minecraft::class)
open class ExampleClientMixin {

    @Inject(method = ["run"], at = [At("HEAD")])
    private fun init(info: CallbackInfo) {
        // This code is injected into the start of Minecraft.run()V
    }
}

