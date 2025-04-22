package net.findsnow.mobsnplenty.client.model;

import net.findsnow.mobsnplenty.client.animations.CrabAnimations;
import net.findsnow.mobsnplenty.common.entity.Crab;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

public class CrabModel extends HierarchicalModel<Crab> {
	private final ModelPart bone;
	private final ModelPart head;

	public CrabModel(ModelPart root) {
		this.bone = root.getChild("bone");
		this.head = bone.getChild("crab").getChild("main").getChild("head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, -0.0436F, 0.0F));

		PartDefinition crab = bone.addOrReplaceChild("crab", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition main = crab.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head = main.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1745F, 0.0F, 0.0F));

		PartDefinition eye_left = head.addOrReplaceChild("eye_left", CubeListBuilder.create().texOffs(4, 24).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offset(3.0F, -6.0F, -3.0F));

		PartDefinition eye_right = head.addOrReplaceChild("eye_right", CubeListBuilder.create().texOffs(0, 24).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offset(-3.0F, -6.0F, -3.0F));

		PartDefinition mandibles = head.addOrReplaceChild("mandibles", CubeListBuilder.create().texOffs(22, 15).addBox(-2.0F, -1.5F, -1.5F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.2F))
				.texOffs(32, 0).addBox(-2.0F, -1.5F, -1.5F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, -4.5F, -1.5F));

		PartDefinition body = head.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -2.0F, -3.0F, 10.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

		PartDefinition claw_left = main.addOrReplaceChild("claw_left", CubeListBuilder.create(), PartPose.offsetAndRotation(7.5F, -1.5F, -2.0F, -0.2896F, -0.7327F, 0.1712F));

		PartDefinition upper_claw = claw_left.addOrReplaceChild("upper_claw", CubeListBuilder.create().texOffs(0, 10).addBox(-8.0F, -4.0F, -1.5F, 8.0F, 4.0F, 3.0F, new CubeDeformation(0.1F)), PartPose.offset(0.5F, 0.5F, 0.0F));

		PartDefinition lower_claw = claw_left.addOrReplaceChild("lower_claw", CubeListBuilder.create().texOffs(22, 10).addBox(-8.0F, -2.0F, -1.5F, 8.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.5F, 0.0F));

		PartDefinition claw_right = main.addOrReplaceChild("claw_right", CubeListBuilder.create(), PartPose.offsetAndRotation(-7.5F, -1.5F, -2.0F, -0.2896F, 0.7327F, -0.1712F));

		PartDefinition upper_claw2 = claw_right.addOrReplaceChild("upper_claw2", CubeListBuilder.create().texOffs(0, 10).mirror().addBox(0.0F, -4.0F, -1.5F, 8.0F, 4.0F, 3.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offset(-0.5F, 0.5F, 0.0F));

		PartDefinition lower_claw2 = claw_right.addOrReplaceChild("lower_claw2", CubeListBuilder.create().texOffs(22, 10).mirror().addBox(0.0F, -2.0F, -1.5F, 8.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-0.5F, 0.5F, 0.0F));

		PartDefinition legs = crab.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left = legs.addOrReplaceChild("left", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition leg_1 = left.addOrReplaceChild("leg_1", CubeListBuilder.create(), PartPose.offsetAndRotation(4.5F, -1.0F, -2.0F, -0.8982F, -0.9275F, -0.2555F));

		PartDefinition l1 = leg_1.addOrReplaceChild("l1", CubeListBuilder.create().texOffs(24, 21).addBox(-0.4016F, -5.8054F, -0.1172F, 1.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.0984F, 4.8054F, 0.1172F));

		PartDefinition leg_2 = left.addOrReplaceChild("leg_2", CubeListBuilder.create(), PartPose.offsetAndRotation(5.5F, -1.0F, -0.25F, -1.5708F, -1.3963F, 0.4363F));

		PartDefinition l2 = leg_2.addOrReplaceChild("l2", CubeListBuilder.create().texOffs(24, 21).addBox(-0.5F, -6.0F, 0.0F, 1.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 0.0F));

		PartDefinition leg_3 = left.addOrReplaceChild("leg_3", CubeListBuilder.create(), PartPose.offsetAndRotation(5.5F, -1.0F, 1.5F, 1.5708F, -1.3526F, -2.7053F));

		PartDefinition l3 = leg_3.addOrReplaceChild("l3", CubeListBuilder.create().texOffs(24, 21).addBox(-0.5F, -6.0F, 0.0F, 1.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 0.0F));

		PartDefinition leg_4 = left.addOrReplaceChild("leg_4", CubeListBuilder.create(), PartPose.offsetAndRotation(4.5F, -1.0F, 3.0F, 2.1296F, -0.7863F, 3.0055F));

		PartDefinition l4 = leg_4.addOrReplaceChild("l4", CubeListBuilder.create().texOffs(24, 21).addBox(-0.5F, -6.0F, 0.0F, 1.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 0.0F));

		PartDefinition right = legs.addOrReplaceChild("right", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition leg_5 = right.addOrReplaceChild("leg_5", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.5F, -1.0F, -2.0F, -0.8982F, 0.9275F, 0.2555F));

		PartDefinition l5 = leg_5.addOrReplaceChild("l5", CubeListBuilder.create().texOffs(24, 21).addBox(-0.3484F, -5.5554F, -0.1172F, 1.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.1516F, 4.5554F, 0.1172F));

		PartDefinition leg_6 = right.addOrReplaceChild("leg_6", CubeListBuilder.create(), PartPose.offsetAndRotation(-5.5F, -1.0F, -0.25F, -1.5708F, 1.3963F, -0.4363F));

		PartDefinition l6 = leg_6.addOrReplaceChild("l6", CubeListBuilder.create().texOffs(24, 21).addBox(-0.5734F, -5.5838F, -0.0937F, 1.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0734F, 4.5838F, 0.0937F));

		PartDefinition leg_7 = right.addOrReplaceChild("leg_7", CubeListBuilder.create(), PartPose.offsetAndRotation(-5.5F, -1.0F, 1.5F, 1.5708F, 1.3526F, 2.7053F));

		PartDefinition l7 = leg_7.addOrReplaceChild("l7", CubeListBuilder.create().texOffs(24, 21).addBox(-0.4085F, -5.5874F, -0.0937F, 1.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.0915F, 4.5874F, 0.0937F));

		PartDefinition leg_8 = right.addOrReplaceChild("leg_8", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.5F, -1.0F, 3.0F, 2.1296F, 0.7863F, -3.0055F));

		PartDefinition l8 = leg_8.addOrReplaceChild("l8", CubeListBuilder.create().texOffs(24, 21).addBox(-0.5959F, -5.5562F, -0.109F, 1.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0959F, 4.5562F, 0.109F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	private void applyHeadRotation(float headYaw, float headPitch) {
		headYaw = Mth.clamp(headYaw, -22f, 25f);
		headPitch = Mth.clamp(headPitch, -30f, 30f);
		this.head.yRot = headYaw * ((float) Math.PI / 180f);
		this.head.xRot = headPitch * ((float) Math.PI / 180f);
	}

	@Override
	public ModelPart root() {
		return bone;
	}

	@Override
	public void setupAnim(Crab entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(netHeadYaw, headPitch);

		if (entity.isClimbing()) {
			Direction climbDirection = entity.getClimbDirection();
			switch (climbDirection) {
				case UP:
					this.bone.xRot = Mth.HALF_PI;
					break;
				case DOWN:
					this.bone.xRot = -Mth.HALF_PI;
					break;
				case NORTH:
				case WEST:
					this.bone.zRot = -Mth.HALF_PI;
					break;
				case SOUTH:
				case EAST:
					this.bone.zRot = Mth.HALF_PI;
					break;
			}
		} else {
			this.bone.yRot = Mth.HALF_PI;
			this.bone.xRot = 0;
			this.bone.zRot = 0;
		}

		this.animateWalk(CrabAnimations.CRAB_WALK, limbSwing, limbSwingAmount, 9.0F, 100.0F);
		this.animate(entity.idleAnimationState, CrabAnimations.CRAB_IDLE, ageInTicks, 1F);
		this.animate(entity.waveAnimationState, CrabAnimations.CRAB_WAVE, ageInTicks, 1F);
		this.animate(entity.pinchAnimationState, CrabAnimations.CRAB_PINCH, ageInTicks, 1F);
	}
}
