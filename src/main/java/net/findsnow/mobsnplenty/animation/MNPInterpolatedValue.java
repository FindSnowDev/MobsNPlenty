package net.findsnow.mobsnplenty.animation;

public class MNPInterpolatedValue {
	private float currentValue;
	private float targetValue;
	private float previousValue;
	private final MNPInterpolationType interpolationType;

	public MNPInterpolatedValue(MNPInterpolationType type, float initialValue, float targetValue) {
		this.interpolationType = type;
		this.currentValue = initialValue;
		this.previousValue = initialValue;
		this.targetValue = targetValue;
	}

	public void tick(float speed) {
		this.previousValue = this.currentValue;
		this.currentValue = lerp(this.currentValue, this.targetValue, speed);
	}

	public float getValue(float partialTicks) {
		if (Math.abs(previousValue - currentValue) < 0.001f) {
			return currentValue;
		}
		return (float) interpolationType.interpolate(previousValue, currentValue, partialTicks);
	}

	public void setTargetValue(float value) {
		this.targetValue = value;
	}

	private static float lerp(float start, float end, float delta) {
		return start + (end - start) * delta;
	}
}
