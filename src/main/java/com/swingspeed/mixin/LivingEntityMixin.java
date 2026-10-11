package com.swingspeed.mixin;

import com.swingspeed.SwingConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	@Inject(method = "getCurrentSwingDuration", at = @At("RETURN"), cancellable = true)
	private void swingspeed$scaleSwing(CallbackInfoReturnable<Integer> cir) {
		if (!SwingConfig.enabled) return;
		Minecraft mc = Minecraft.getInstance();
		if (mc == null || (Object) this != mc.player) return;

		int base = cir.getReturnValue();
		int scaled = Math.max(1, Math.round(base / SwingConfig.speed));
		cir.setReturnValue(scaled);
	}
}
