package net.findsnow.mobsnplenty.client.model;

import net.findsnow.mobsnplenty.client.animations.SharkAnimations;
import net.findsnow.mobsnplenty.common.entity.Shark;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class SharkModel extends HierarchicalModel<Shark> {
	private final ModelPart bone;
	private final ModelPart head;
	private final ModelPart left_fin;
	private final ModelPart right_fin;

	public SharkModel(ModelPart root) {
		this.bone = root.getChild("bone");
		this.head = bone.getChild("shark").getChild("main").getChild("head");
		this.left_fin = bone.getChild("shark").getChild("main").getChild("body").getChild("upper_body").getChild("arms").getChild("left_arm");
		this.right_fin = bone.getChild("shark").getChild("main").getChild("body").getChild("upper_body").getChild("arms").getChild("right_arm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 13.0F, 6.0F));

		PartDefinition shark = bone.addOrReplaceChild("shark", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition main = shark.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head = main.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 2.0F, -13.0F));

		PartDefinition upper_jaw = head.addOrReplaceChild("upper_jaw", CubeListBuilder.create().texOffs(0, 45).addBox(-7.0F, -11.0F, -17.0F, 14.0F, 11.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, -1.0F));

		PartDefinition cube_r1 = upper_jaw.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 77).addBox(-6.0F, -2.0F, -6.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.0F, 2.0F, -5.0F, 0.0F, 0.0F, -3.1416F));

		PartDefinition lower_jaw = head.addOrReplaceChild("lower_jaw", CubeListBuilder.create().texOffs(70, 45).addBox(-7.0F, 0.0F, -12.0F, 14.0F, 5.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(90, 0).addBox(-6.0F, -2.0F, -11.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, -1.0F));

		PartDefinition body = main.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, -14.0F));

		PartDefinition upper_body = body.addOrReplaceChild("upper_body", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -9.0F, 0.25F, 18.0F, 18.0F, 27.0F, new CubeDeformation(0.0F))
				.texOffs(50, 93).addBox(-1.0F, -17.0F, 11.25F, 2.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -0.25F));

		PartDefinition arms = upper_body.addOrReplaceChild("arms", CubeListBuilder.create(), PartPose.offset(8.0F, 9.0F, 8.25F));

		PartDefinition left_arm = arms.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(90, 14).addBox(0.0F, -0.5F, -5.5F, 2.0F, 15.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -4.5F, 3.5F));

		PartDefinition right_arm = arms.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(24, 93).addBox(-2.0F, -0.5F, -5.5F, 2.0F, 15.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(-17.0F, -4.5F, 3.5F));

		PartDefinition lower_body = body.addOrReplaceChild("lower_body", CubeListBuilder.create().texOffs(70, 66).addBox(-6.0F, -7.0F, 0.0F, 12.0F, 14.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 27.0F));

		PartDefinition tail = lower_body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 93).addBox(-1.0F, -13.5F, -5.0F, 2.0F, 27.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 15.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(Shark shark, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		if (shark.isInWaterOrBubble()) {
			this.animateWalk(SharkAnimations.swim, limbSwing, limbSwingAmount, 5.0F, 100.0F);
			this.animate(shark.biteAnimationState, SharkAnimations.bite, ageInTicks, 1.0F);
			this.bone.xRot += bone.xRot * (float) (Math.PI / 180);
			this.bone.yRot += bone.yRot * (float) (Math.PI / 180);
			this.head.xRot += (head.xRot * (float) (Math.PI / 180) / 2);
			this.head.yRot += (head.yRot * (float) (Math.PI / 180) / 2);
			this.right_fin.yRot += (right_fin.yRot * (float) (Math.PI / 180) / 2);
			this.right_fin.xRot += (right_fin.xRot * (float) (Math.PI / 180) / 2);
			this.left_fin.xRot += (left_fin.xRot * (float) (Math.PI / 180) / 2);
			this.left_fin.yRot += (left_fin.yRot * (float) (Math.PI / 180) / 2);
		} else {
			this.animateWalk(SharkAnimations.crawl, limbSwing, limbSwingAmount, 3.0F, 100.0F);
			this.animate(shark.idleAnimationState, SharkAnimations.land_idle, ageInTicks, 1.0F);
			this.bone.xRot = 0;
			this.bone.yRot = 0;
			this.right_fin.yRot = 0;
			this.left_fin.yRot = 0;
			this.head.xRot += (head.xRot * (float) (Math.PI / 180));
			this.head.yRot += (head.yRot * (float) (Math.PI / 180));
		}
	}

	@Override
	public ModelPart root() {
		return bone;
	}
}
