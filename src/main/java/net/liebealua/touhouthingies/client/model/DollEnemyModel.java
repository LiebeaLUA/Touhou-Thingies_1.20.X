package net.liebealua.touhouthingies.client.model;// Made with Blockbench 4.9.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.liebealua.touhouthingies.TouhouThingies;
import net.liebealua.touhouthingies.client.animation.DollEnemyAnimation;
import net.liebealua.touhouthingies.entity.dollEnemy.DollEnemy;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;

public class DollEnemyModel<T extends DollEnemy> extends HierarchicalModel<T> implements ArmedModel, HeadedModel {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(TouhouThingies.MODID, "doll_enemy"), "main");
	public final ModelPart dollEnemy;
	public final ModelPart body;
	public final ModelPart body_upper;
	public final ModelPart body_lower;
	public final ModelPart head;
	public final ModelPart right_arm;
	public final ModelPart left_arm;
	public final ModelPart skirt;
	public final ModelPart right_leg;
	public final ModelPart left_leg;


	public DollEnemyModel(ModelPart root) {
		this.dollEnemy = root.getChild("dollEnemy");
		this.body = dollEnemy.getChild("body");
		this.body_upper = body.getChild("body_upper");
		this.body_lower = body.getChild("body_lower");
		this.head = body_upper.getChild("head");
		this.right_arm = body_upper.getChild("right_arm");
		this.left_arm = body_upper.getChild("left_arm");
		this.skirt = body_lower.getChild("skirt");
		this.right_leg = body_lower.getChild("right_leg");
		this.left_leg = body_lower.getChild("left_leg");
	}

//	public static LayerDefinition createBodyLayer() {
//		MeshDefinition meshdefinition = new MeshDefinition();
//		PartDefinition partdefinition = meshdefinition.getRoot();
//
//		PartDefinition dollEnemy = partdefinition.addOrReplaceChild("dollEnemy", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
//
//		PartDefinition body = dollEnemy.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));
//
//		PartDefinition body_upper = body.addOrReplaceChild("body_upper", CubeListBuilder.create().texOffs(24, 25).addBox(-2.0F, -6.0F, -1.0F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, 0.0F));
//
//		PartDefinition chest_r1 = body_upper.addOrReplaceChild("chest_r1", CubeListBuilder.create().texOffs(28, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 0.5F, -0.4363F, 0.0F, 0.0F));
//
//		PartDefinition head = body_upper.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 20).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
//				.texOffs(22, 11).addBox(-4.0F, -8.0F, 1.0F, 8.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 0.0F));
//
//		PartDefinition right_arm = body_upper.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(2.0F, -6.0F, 0.0F));
//
//		PartDefinition right_arm_r1 = right_arm.addOrReplaceChild("right_arm_r1", CubeListBuilder.create().texOffs(24, 33).addBox(0.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1309F));
//
//		PartDefinition left_arm = body_upper.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(-2.0F, -6.0F, 0.0F));
//
//		PartDefinition left_arm_r1 = left_arm.addOrReplaceChild("left_arm_r1", CubeListBuilder.create().texOffs(16, 32).addBox(-2.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1309F));
//
//		PartDefinition body_lower = body.addOrReplaceChild("body_lower", CubeListBuilder.create(), PartPose.offset(0.0F, 18.0F, 0.0F));
//
//		PartDefinition skirt = body_lower.addOrReplaceChild("skirt", CubeListBuilder.create().texOffs(18, 20).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
//				.texOffs(0, 11).addBox(-4.0F, 1.0F, -3.0F, 8.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
//				.texOffs(0, 0).addBox(-5.0F, 4.0F, -4.0F, 10.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 0.0F));
//
//		PartDefinition right_leg = body_lower.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(8, 32).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -9.0F, 0.0F));
//
//		PartDefinition left_leg = body_lower.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 32).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -9.0F, 0.0F));
//
//		return LayerDefinition.create(meshdefinition, 64, 64);
//	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition dollEnemy = partdefinition.addOrReplaceChild("dollEnemy", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = dollEnemy.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition body_upper = body.addOrReplaceChild("body_upper", CubeListBuilder.create().texOffs(24, 25).addBox(-2.0F, -6.0F, -1.0F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, 0.0F));

		PartDefinition back_ribbon_r1 = body_upper.addOrReplaceChild("back_ribbon_r1", CubeListBuilder.create().texOffs(40, 0).addBox(-3.0F, -2.0F, 0.0F, 6.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 2.0F, 0.2182F, 0.0F, 0.0F));

		PartDefinition chest_r1 = body_upper.addOrReplaceChild("chest_r1", CubeListBuilder.create().texOffs(28, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.0F, -4.0F, 0.5F, -0.4363F, 0.0F, 0.0F));

		PartDefinition head = body_upper.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 20).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(22, 11).addBox(-4.0F, -8.0F, 2.0F, 8.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(36, 20).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 10.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -6.0F, 0.0F));

		PartDefinition right_arm = body_upper.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(2.0F, -6.0F, 0.0F));

		PartDefinition right_arm_r1 = right_arm.addOrReplaceChild("right_arm_r1", CubeListBuilder.create().texOffs(8, 32).addBox(0.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1309F));

		PartDefinition left_arm = body_upper.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(-2.0F, -6.0F, 0.0F));

		PartDefinition left_arm_r1 = left_arm.addOrReplaceChild("left_arm_r1", CubeListBuilder.create().texOffs(8, 32).addBox(-2.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1309F));

		PartDefinition body_lower = body.addOrReplaceChild("body_lower", CubeListBuilder.create(), PartPose.offset(0.0F, 18.0F, 0.0F));

		PartDefinition skirt = body_lower.addOrReplaceChild("skirt", CubeListBuilder.create().texOffs(18, 20).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 11).addBox(-4.0F, 1.0F, -3.0F, 8.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-5.0F, 4.0F, -4.0F, 10.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 0.0F));

		PartDefinition right_leg = body_lower.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 32).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -9.0F, 0.0F));

		PartDefinition left_leg = body_lower.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 32).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -9.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.dollEnemy.getAllParts().forEach(ModelPart::resetPose);

		head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
		head.xRot = headPitch * Mth.DEG_TO_RAD;

		right_arm.xRot = Mth.cos(limbSwing * 0.6662f) * 1.2f * limbSwingAmount;
		left_arm.xRot = Mth.cos(limbSwing * 0.6662f + Mth.PI) * 1.2f * limbSwingAmount;
		right_leg.xRot = Mth.cos(limbSwing * 0.6662f + Mth.PI) * 0.8f * limbSwingAmount;
		left_leg.xRot = Mth.cos(limbSwing * 0.6662f) * 0.8f * limbSwingAmount;

		animate(entity.idleAnimationState, DollEnemyAnimation.idle, ageInTicks);
		if (entity.getPrimed())
			this.head.resetPose();
		animate(entity.deathAnimationState, DollEnemyAnimation.death, ageInTicks);
		animate(entity.shakeAnimationState, DollEnemyAnimation.shake, ageInTicks);

		setupAttackAnimation(entity, ageInTicks);
	}





	protected void setupAttackAnimation(T pLivingEntity, float pAgeInTicks) {
		if (!(this.attackTime <= 0.0F)) {
			HumanoidArm humanoidarm = this.getAttackArm(pLivingEntity);
			ModelPart modelpart = this.getArm(humanoidarm);
			float f = this.attackTime;
			this.body.yRot = Mth.sin(Mth.sqrt(f) * 6.2831855F) * 0.2F;
			ModelPart var10000;
			if (humanoidarm == HumanoidArm.LEFT) {
				var10000 = this.body;
				var10000.yRot *= -1.0F;
			}

			this.right_arm.z = Mth.sin(this.body.yRot) * 5.0F;
			this.right_arm.x = -Mth.cos(this.body.yRot) * 5.0F;
			this.left_arm.z = -Mth.sin(this.body.yRot) * 5.0F;
			this.left_arm.x = Mth.cos(this.body.yRot) * 5.0F;
			var10000 = this.right_arm;
			var10000.yRot += this.body.yRot;
			var10000 = this.left_arm;
			var10000.yRot += this.body.yRot;
			var10000 = this.left_arm;
			var10000.xRot += this.body.yRot;
			f = 1.0F - this.attackTime;
			f *= f;
			f *= f;
			f = 1.0F - f;
			float f1 = Mth.sin(f * 3.1415927F);
			float f2 = Mth.sin(this.attackTime * 3.1415927F) * -(this.head.xRot - 0.7F) * 0.75F;
			modelpart.xRot -= f1 * 1.2F + f2;
			modelpart.yRot += this.body.yRot * 2.0F;
			modelpart.zRot += Mth.sin(this.attackTime * 3.1415927F) * -0.4F;
		}
	}

	private HumanoidArm getAttackArm(T pEntity) {
		HumanoidArm humanoidarm = pEntity.getMainArm();
		return pEntity.swingingArm == InteractionHand.MAIN_HAND ? humanoidarm : humanoidarm.getOpposite();
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.dollEnemy;
	}

	@Override
	public void translateToHand(HumanoidArm humanoidArm, PoseStack poseStack) {
		this.getArm(humanoidArm).translateAndRotate(poseStack);
	}

	@Override
	public ModelPart getHead() {
		return this.head;
	}

	private ModelPart getArm(HumanoidArm pArm) {
		return pArm == HumanoidArm.LEFT ? this.left_arm : this.right_arm;
	}
}