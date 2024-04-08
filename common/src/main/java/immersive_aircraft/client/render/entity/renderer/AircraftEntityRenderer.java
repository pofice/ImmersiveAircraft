package immersive_aircraft.client.render.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import immersive_aircraft.client.render.entity.renderer.utils.ModelPartRenderHandler;
import immersive_aircraft.entity.AircraftEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.joml.Vector3f;

public abstract class AircraftEntityRenderer<T extends AircraftEntity> extends InventoryVehicleRenderer<T> {
    public AircraftEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    // Because this is used in plugins, changing to generic T is no longer possible
    protected abstract ModelPartRenderHandler<T> getModel(AircraftEntity entity);

    @SuppressWarnings("RedundantMethodOverride")
    @Override
    protected Vector3f getPivot(AircraftEntity entity) {
        return new Vector3f(0.0f, 0.0f, 0.0f);
    }

    public void renderLocal(T entity, float yaw, float tickDelta, PoseStack matrixStack, PoseStack.Pose peek, MultiBufferSource vertexConsumerProvider, int light) {
        // Wind effect
        Vector3f effect = entity.onGround() ? new Vector3f(0.0f, 0.0f, 0.0f) : entity.getWindEffect();
        matrixStack.mulPose(Axis.YP.rotationDegrees(-yaw));
        matrixStack.mulPose(Axis.XP.rotationDegrees(entity.getViewXRot(tickDelta) + effect.z));
        matrixStack.mulPose(Axis.ZP.rotationDegrees(entity.getRoll(tickDelta) + effect.x));

        super.renderLocal(entity, yaw, tickDelta, matrixStack, peek, vertexConsumerProvider, light);

        //Render trails
        entity.getTrails().forEach(t -> TrailRenderer.render(t, vertexConsumerProvider, peek));
    }
}

