package net.findsnow.mobsnplenty.common.entity.animationstate;

import net.minecraft.util.StringRepresentable;

public enum SharkAnimationState implements StringRepresentable {
	IDLE("idle", 0),
	SWIMMING("swimming", 40),
	CRAWLING("crawling", 40),
	BITE("bite", 10);

	private final String name;
	private final int duration;

	SharkAnimationState(String name, int duration) {
		this.name = name;
		this.duration = duration;
	}

	public static SharkAnimationState fromString(String name) {
		for (SharkAnimationState state : values()) {
			if (state.getSerializedName().equals(name)) {
				return state;
			}
		}
		return IDLE;
	}

	@Override
	public String getSerializedName() {
		return this.name;
	}

	public int getDuration() {
		return this.duration;
	}
}