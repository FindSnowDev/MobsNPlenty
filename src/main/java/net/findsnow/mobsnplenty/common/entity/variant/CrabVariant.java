package net.findsnow.mobsnplenty.common.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum CrabVariant {
	DEFAULT(0),
	GREEN(1),
	BLUE(2);

	private static final CrabVariant[] VALUES = Arrays.stream(values()).sorted(
			Comparator.comparingInt(CrabVariant::getId)).toArray(CrabVariant[]::new);

	private final int id;

	CrabVariant(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static CrabVariant byId(int id) {
        return VALUES[id % VALUES.length];
    }
}
