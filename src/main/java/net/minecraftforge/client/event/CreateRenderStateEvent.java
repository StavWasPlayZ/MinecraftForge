package net.minecraftforge.client.event;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.ApiStatus;

/**
 * Fired when an entity's {@link EntityRenderState render state} is created.
 * See the two subclasses to listen for before and after creation.
 *
 * <p>Fired on the {@link net.minecraftforge.common.MinecraftForge#EVENT_BUS Forge event bus}.</p>
 * @see CreateRenderStateEvent.Pre
 * @see CreateRenderStateEvent.Post
 */
public abstract class CreateRenderStateEvent extends Event {
    private final EntityRenderer<?, ?> renderer;
    private final Entity entity;
    private final EntityRenderState state;
    private final float packedLight;

    @ApiStatus.Internal
    protected CreateRenderStateEvent(EntityRenderer<?, ?> renderer, Entity entity, EntityRenderState state, float packedLight) {
        this.renderer = renderer;
        this.entity = entity;
        this.state = state;
        this.packedLight = packedLight;
    }

    /**
     * @return The entity's renderer
     */
    public EntityRenderer<?, ?> getRenderer() {
        return renderer;
    }

    /**
     * @return The entity whose state is being created
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * @return The entity's render state
     */
    public EntityRenderState getState() {
        return state;
    }

    /**
     * @return The amount of packed (sky and block) light for rendering
     *
     * @see LightTexture
     */
    public float getPackedLight() {
        return packedLight;
    }

    /**
     * An event fired when an entity's render state is to be created.
     * <p>Fired on the {@link net.minecraftforge.common.MinecraftForge#EVENT_BUS Forge event bus}.</p>
     * @see CreateRenderStateEvent.Pre
     * @see CreateRenderStateEvent.Post
     */
    public static class Pre extends CreateRenderStateEvent {
        @ApiStatus.Internal
        public Pre(EntityRenderer<?, ?> renderer, Entity entity, EntityRenderState state, float packedLight) {
            super(renderer, entity, state, packedLight);
        }
    }
    /**
     * An event fired when an entity's render state was created.
     * <p>You may modify existing render state fields here.<p/>
     * <p>Fired on the {@link net.minecraftforge.common.MinecraftForge#EVENT_BUS Forge event bus}.</p>
     * @see CreateRenderStateEvent.Pre
     * @see CreateRenderStateEvent.Post
     */
    public static class Post extends CreateRenderStateEvent {
        @ApiStatus.Internal
        public Post(EntityRenderer<?, ?> renderer, Entity entity, EntityRenderState state, float packedLight) {
            super(renderer, entity, state, packedLight);
        }
    }
}
