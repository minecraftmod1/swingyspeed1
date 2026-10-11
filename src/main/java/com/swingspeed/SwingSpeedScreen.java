package com.swingspeed;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class SwingSpeedScreen extends Screen {

	public SwingSpeedScreen() {
		super(Component.literal("Swing Speed"));
	}

	private static Component toggleText() {
		return Component.literal("Swing Speed Mod: " + (SwingConfig.enabled ? "ON" : "OFF"));
	}

	@Override
	protected void init() {
		int cx = this.width / 2;
		int y = this.height / 2 - 50;

		this.addRenderableWidget(Button.builder(toggleText(), btn -> {
			SwingConfig.enabled = !SwingConfig.enabled;
			SwingConfig.save();
			btn.setMessage(toggleText());
		}).bounds(cx - 100, y, 200, 20).build());

		SpeedSlider slider = new SpeedSlider(cx - 100, y + 28, 200, 20);
		this.addRenderableWidget(slider);

		float[] presets = {0.25f, 0.5f, 1.0f, 2.0f};
		for (int i = 0; i < presets.length; i++) {
			final float p = presets[i];
			this.addRenderableWidget(Button.builder(
					Component.literal(Math.round(p * 100) + "%"),
					btn -> slider.setSpeed(p)
			).bounds(cx - 100 + i * 51, y + 56, 47, 20).build());
		}

		this.addRenderableWidget(Button.builder(Component.literal("Done"), btn -> this.onClose())
				.bounds(cx - 100, y + 90, 200, 20).build());
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		super.render(graphics, mouseX, mouseY, delta);
		int cx = this.width / 2;
		int y = this.height / 2 - 50;

		String title = "Swing Speed";
		graphics.drawString(this.font, title, cx - this.font.width(title) / 2, y - 24, 0xFFFFFFFF, true);

		String hint = "Lower = slower hand movement (100% = normal)";
		graphics.drawString(this.font, hint, cx - this.font.width(hint) / 2, y - 12, 0xFFAAAAAA, true);
	}

	private static class SpeedSlider extends AbstractSliderButton {
		SpeedSlider(int x, int y, int w, int h) {
			super(x, y, w, h, Component.empty(), toValue(SwingConfig.speed));
			updateMessage();
		}

		private static double toValue(float speed) {
			return (speed - SwingConfig.MIN_SPEED) / (SwingConfig.MAX_SPEED - SwingConfig.MIN_SPEED);
		}

		@Override
		protected void updateMessage() {
			setMessage(Component.literal("Swing Speed: " + Math.round(SwingConfig.speed * 100) + "%"));
		}

		@Override
		protected void applyValue() {
			float s = SwingConfig.MIN_SPEED + (float) this.value * (SwingConfig.MAX_SPEED - SwingConfig.MIN_SPEED);
			s = Math.round(s * 20f) / 20f;
			SwingConfig.speed = SwingConfig.clamp(s);
			SwingConfig.save();
		}

		void setSpeed(float s) {
			SwingConfig.speed = SwingConfig.clamp(s);
			this.value = toValue(SwingConfig.speed);
			SwingConfig.save();
			updateMessage();
		}
	}
}
