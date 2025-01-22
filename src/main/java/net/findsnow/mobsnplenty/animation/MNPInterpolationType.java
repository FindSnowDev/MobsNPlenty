package net.findsnow.mobsnplenty.animation;

import static net.minecraft.util.Mth.lerp;

public enum MNPInterpolationType {
	LINEAR {
		@Override
		public double interpolate(double start, double end, double delta) {
			return start + (end - start) * delta;
		}
	},
	CATMULLROM {
		@Override
		public double interpolate(double start, double end, double delta) {
			// Simplified Catmull-Rom interpolation
			double t = delta;
			double t2 = t * t;
			double t3 = t2 * t;
			return start + (end - start) * (-2 * t3 + 3 * t2);
		}
	},
	BEZIER {
		@Override
		public double interpolate(double start, double end, double delta) {
			// Cubic Bezier interpolation
			double t = delta;
			double u = 1 - t;
			return start * u * u * u + end * t * t * t;
		}
	};

	public abstract double interpolate(double start, double end, double delta);
}

