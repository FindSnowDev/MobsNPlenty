package net.findsnow.mobsnplenty.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;

public class FlyParticle extends TextureSheetParticle {
	private final SpriteSet sprites;
	private static final int minOffTime = 20 * 2;
	private static final int maxOffTime = 20 * 4;
	private static final int minOnTime = 10;
	private static final int maxOnTime = 20;

	private final SimplexNoise simplexNoise;
	private int ageOffset = 0;
	private int ticksUntilNextSwitch = 40;
	private boolean isOn = false;
	private boolean isSpeedingUp = false;
	private int speedUpDuration = 0;

	public FlyParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
		super(level, x, y, z, xSpeed, ySpeed, zSpeed);
		this.sprites = sprites;
		this.gravity = 0;
		this.xd = 0;
		this.yd = 0;
		this.zd = 0;
		this.alpha = 0;
		this.lifetime = 200;
		this.quadSize = 0.07F;
		this.simplexNoise = new SimplexNoise(random);
		this.setSpriteFromAge(sprites);
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	public float getQuadSize(float scaleFactor) {
		float ageFraction = ((float) this.age + scaleFactor) / (float) this.lifetime;
		return this.quadSize * (1.0f - ageFraction * ageFraction * 0.5f);
	}

	@Override
	public void tick() {
		super.tick();

		if (this.random.nextFloat() < 1f) {
			if (this.onGround) {
				this.ageOffset += 5;
			}
		}
		if (--this.ticksUntilNextSwitch <= 0) {
			if (this.isOn) {
				this.isOn = false;
				this.ticksUntilNextSwitch = this.random.nextIntBetweenInclusive(minOffTime, maxOffTime);
			} else {
				this.isOn = true;
				this.ticksUntilNextSwitch = this.random.nextIntBetweenInclusive(minOnTime, maxOnTime);
			}
		}

		this.alpha = this.isOn && (this.lifetime - this.age) > 3 ? Math.min(1, this.alpha + 0.33f) : Math.max(0, this.alpha - 0.33f);

		if (this.random.nextInt(100) == 0 && !this.isSpeedingUp) {
			this.isSpeedingUp = true;
			this.speedUpDuration = this.random.nextInt(20) + 10;
		}

		float speedFactor = this.isSpeedingUp ? 0.1f : 0.01f;
		float noiseFactor = 0.02f;
		this.xd = this.simplexNoise.getValue(this.age * noiseFactor, this.age * noiseFactor) * speedFactor;
		this.yd = this.simplexNoise.getValue((this.age + this.ageOffset) * noiseFactor - 50f, (this.age + this.ageOffset) * noiseFactor + 100f) * speedFactor * 0.1f;
		this.zd = this.simplexNoise.getValue(this.age * noiseFactor + 100f, this.age * noiseFactor - 50f) * speedFactor;

		speedFactor = this.isSpeedingUp ? 0.2f : 0.05f;
		noiseFactor = 0.033f;
		this.xd += this.simplexNoise.getValue(this.age * noiseFactor, this.age * noiseFactor) * speedFactor * 0.3f;
		this.yd += this.simplexNoise.getValue((this.age + this.ageOffset) * noiseFactor - 50f, (this.age + this.ageOffset) * noiseFactor + 100f) * speedFactor;
		this.zd += this.simplexNoise.getValue(this.age * noiseFactor + 100f, this.age * noiseFactor - 50f) * speedFactor * 0.2f;

		if (this.isSpeedingUp) {
			if (--this.speedUpDuration <= 0) {
				this.isSpeedingUp = false;
			}
		}
		this.setSpriteFromAge(this.sprites);
	}
}
