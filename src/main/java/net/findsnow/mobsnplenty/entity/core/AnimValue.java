package net.findsnow.mobsnplenty.entity.core;

import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AnimValue {
	private float currentValue;
	private float targetValue;
	private float  previousValue;

	public AnimValue(float startingValue, float endValue) {
		this.currentValue = startingValue;
		this.targetValue = startingValue;
		this.previousValue = endValue;
	}

	public void setTarget(float newTarget) {
		if (this.targetValue != newTarget) {
			this.targetValue = newTarget;
		}
	}

	public void tick(float speed) {
		if (Math.abs(this.currentValue - this.targetValue) > 0.001f) {
			this.previousValue = this.currentValue;
			this.currentValue = Mth.lerp(speed * 0.1f, this.currentValue, this.targetValue);
		}
	}

	public float getValue(float partialTick) {
		return Mth.lerp(partialTick, this.previousValue, this.currentValue);
	}
}
