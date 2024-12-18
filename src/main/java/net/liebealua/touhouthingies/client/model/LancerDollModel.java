package net.liebealua.touhouthingies.client.model;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.liebealua.touhouthingies.TouhouThingies;
import net.liebealua.touhouthingies.client.animation.LancerDollAnimation;
import net.liebealua.touhouthingies.entity.dolls.LancerDoll;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;

public class LancerDollModel<T extends LancerDoll> extends HierarchicalModel<T> implements ArmedModel, HeadedModel {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(TouhouThingies.MODID, "lancer_doll"), "main");
    public final ModelPart model;
    public final ModelPart body;
    public final ModelPart body_upper;
    public final ModelPart body_lower;
    public final ModelPart neck_axis;
    public final ModelPart head;
    public final ModelPart right_arm;
    public final ModelPart left_arm;
    public final ModelPart skirt;
    public final ModelPart right_leg;
    public final ModelPart left_leg;


    public LancerDollModel(ModelPart root) {
        this.model = root.getChild("model");
        this.body = model.getChild("body");
        this.body_upper = body.getChild("body_upper");
        this.body_lower = body.getChild("body_lower");
        this.neck_axis = body_upper.getChild("neck_axis");
        this.head = neck_axis.getChild("head");
        this.right_arm = body_upper.getChild("right_arm");
        this.left_arm = body_upper.getChild("left_arm");
        this.skirt = body_lower.getChild("skirt");
        this.right_leg = body_lower.getChild("right_leg");
        this.left_leg = body_lower.getChild("left_leg");
    }


    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition model = partdefinition.addOrReplaceChild("model", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = model.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition body_upper = body.addOrReplaceChild("body_upper", CubeListBuilder.create(), PartPose.offset(0.0F, 9.0F, 0.0F));

        PartDefinition back_ribbon_r1 = body_upper.addOrReplaceChild("back_ribbon_r1", CubeListBuilder.create().texOffs(40, 0).addBox(-3.0F, -2.0F, 0.0F, 6.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 2.0F, 0.2182F, 0.0F, 0.0F));

        PartDefinition neck_axis = body_upper.addOrReplaceChild("neck_axis", CubeListBuilder.create().texOffs(24, 25).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 0.0F));

        PartDefinition chest_r1 = neck_axis.addOrReplaceChild("chest_r1", CubeListBuilder.create().texOffs(28, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.5F, -0.4363F, 0.0F, 0.0F));

        PartDefinition head = neck_axis.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 20).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(22, 11).addBox(-4.0F, -8.0F, 2.0F, 8.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(36, 20).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 10.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_arm = body_upper.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(2.0F, -6.0F, 0.0F));

        PartDefinition right_arm_r1 = right_arm.addOrReplaceChild("right_arm_r1", CubeListBuilder.create().texOffs(8, 32).addBox(0.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1309F));

        PartDefinition left_arm = body_upper.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(-2.0F, -6.0F, 0.0F));

        PartDefinition left_arm_r1 = left_arm.addOrReplaceChild("left_arm_r1", CubeListBuilder.create().texOffs(8, 32).addBox(-2.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1309F));

        PartDefinition lance = left_arm.addOrReplaceChild("lance", CubeListBuilder.create().texOffs(0, 56).addBox(-0.5F, -0.5F, -3.75F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(18, 58).addBox(-2.5F, -2.5F, -4.75F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(30, 58).addBox(-2.0F, -2.0F, -6.75F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(21, 51).addBox(-1.5F, -1.5F, -9.75F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(6, 54).addBox(-1.0F, -1.0F, -13.75F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(14, 52).addBox(-0.5F, -0.5F, -18.75F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 5.5F, -1.25F));

        PartDefinition body_lower = body.addOrReplaceChild("body_lower", CubeListBuilder.create(), PartPose.offset(0.0F, 9.0F, 0.0F));

        PartDefinition skirt = body_lower.addOrReplaceChild("skirt", CubeListBuilder.create().texOffs(18, 20).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 11).addBox(-4.0F, 1.0F, -3.0F, 8.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-5.0F, 4.0F, -4.0F, 10.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

        PartDefinition right_leg = body_lower.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 32).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 0.0F, 0.0F));

        PartDefinition left_leg = body_lower.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 32).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.getAllParts().forEach(ModelPart::resetPose);


        if (!entity.chargeAnimationState.isStarted() && !entity.attackAnimationState.isStarted()) {
            head.xRot = headPitch * Mth.DEG_TO_RAD;
            head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
        }

//        right_arm.xRot = Mth.cos(limbSwing * 0.6662f) * 1.2f * limbSwingAmount;
//        left_arm.xRot = Mth.cos(limbSwing * 0.6662f + Mth.PI) * 1.2f * limbSwingAmount;
//        right_leg.xRot = Mth.cos(limbSwing * 0.6662f + Mth.PI) * 0.8f * limbSwingAmount;
//        left_leg.xRot = Mth.cos(limbSwing * 0.6662f) * 0.8f * limbSwingAmount;

        animate(entity.idleAnimationState, LancerDollAnimation.idle, ageInTicks);
        animate(entity.chargeAnimationState, LancerDollAnimation.charge, ageInTicks);
        animate(entity.attackAnimationState, LancerDollAnimation.attack, ageInTicks);
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
        return this.model;
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