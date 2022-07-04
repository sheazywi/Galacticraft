/*
 * Copyright (c) 2019-2022 Team Galacticraft
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.galacticraft.mod.client.model;

import dev.galacticraft.mod.Constant;
import dev.galacticraft.mod.api.block.entity.Walkway;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author <a href="https://github.com/TeamGalacticraft">TeamGalacticraft</a>
 */
@Environment(EnvType.CLIENT)
public class WalkwayBakedModel implements FabricBakedModel, BakedModel {
    private static WalkwayBakedModel instance = null;

    public static final Identifier WALKWAY_MARKER = new Identifier(Constant.MOD_ID, "autogenerated/walkway");
    public static final Identifier WALKWAY_TEX = new Identifier(Constant.MOD_ID, "block/walkway");
    public static final Identifier WALKWAY_PLATFORM = new Identifier(Constant.MOD_ID, "block/walkway");
    private final BakedModel platform;
    private final Mesh down;
    private final Mesh up;
    private final Mesh north;
    private final Mesh south;
    private final Mesh west;
    private final Mesh east;
    private final Sprite sprite;

    protected WalkwayBakedModel(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer) {
        this.platform = loader.getOrLoadModel(WALKWAY_PLATFORM).bake(loader, textureGetter, rotationContainer, WALKWAY_PLATFORM);
        this.sprite = textureGetter.apply(new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, WALKWAY_TEX));
        MeshBuilder meshBuilder = RendererAccess.INSTANCE.getRenderer().meshBuilder();
        QuadEmitter emitter = meshBuilder.getEmitter();
        emitter.square(Direction.DOWN, 0.4f, 0.4f, 0.6f, 0.6f, 0.0f).spriteColor(0, -1, -1, -1, -1).sprite(1, 0, 6, 6).sprite(2, 0, 9, 6).sprite(3, 0, 9, 9).sprite(0, 0, 6, 9).cullFace(Direction.DOWN).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        emitter.square(Direction.WEST, 0.6f, 0.4f, 0.4f, 0.0f, 0.4f).spriteColor(0, -1, -1, -1, -1).sprite(1, 0, 6, 4).sprite(2, 0, 9, 4).sprite(3, 0, 9, 11).sprite(0, 0, 6, 11).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        emitter.square(Direction.EAST, 0.6f, 0.4f, 0.4f, 0.0f, 0.4f).spriteColor(0, -1, -1, -1, -1).sprite(1, 0, 6, 4).sprite(2, 0, 9, 4).sprite(3, 0, 9, 11).sprite(0, 0, 6, 11).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        emitter.square(Direction.SOUTH, 0.4f, 0.0f, 0.6f, 0.4f, 0.4f).spriteColor(0, -1, -1, -1, -1).sprite(1, 0, 6, 4).sprite(2, 0, 9, 4).sprite(3, 0, 9, 11).sprite(0, 0, 6, 11).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        emitter.square(Direction.NORTH, 0.4f, 0.0f, 0.6f, 0.4f, 0.4f).spriteColor(0, -1, -1, -1, -1).sprite(1, 0, 6, 4).sprite(2, 0, 9, 4).sprite(3, 0, 9, 11).sprite(0, 0, 6, 11).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        this.down = meshBuilder.build();
        emitter.square(Direction.UP, 0.4f, 0.4f, 0.6f, 0.6f, 0.0f).spriteColor(0, -1, -1, -1, -1).sprite(0, 0, 6, 6).sprite(1, 0, 9, 6).sprite(2, 0, 9, 9).sprite(3, 0, 6, 9).cullFace(Direction.UP).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        emitter.square(Direction.EAST, 0.6f, 1.0f, 0.4f, 0.6f, 0.4f).spriteColor(0, -1, -1, -1, -1).sprite(1, 0, 6, 4).sprite(2, 0, 9, 4).sprite(3, 0, 9, 11).sprite(0, 0, 6, 11).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        emitter.square(Direction.WEST, 0.6f, 1.0f, 0.4f, 0.6f, 0.4f).spriteColor(0, -1, -1, -1, -1).sprite(1, 0, 6, 4).sprite(2, 0, 9, 4).sprite(3, 0, 9, 11).sprite(0, 0, 6, 11).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        emitter.square(Direction.NORTH, 0.4f, 0.6f, 0.6f, 1.0f, 0.4f).spriteColor(0, -1, -1, -1, -1).sprite(1, 0, 6, 4).sprite(2, 0, 9, 4).sprite(3, 0, 9, 11).sprite(0, 0, 6, 11).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        emitter.square(Direction.SOUTH, 0.4f, 0.6f, 0.6f, 1.0f, 0.4f).spriteColor(0, -1, -1, -1, -1).sprite(1, 0, 6, 4).sprite(2, 0, 9, 4).sprite(3, 0, 9, 11).sprite(0, 0, 6, 11).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        this.up = meshBuilder.build();
        emitter.square(Direction.NORTH, 0.4f, 0.4f, 0.6f, 0.6f, 0.0f).spriteColor(0, -1, -1, -1, -1).sprite(1, 0, 6, 6).sprite(2, 0, 9, 6).sprite(3, 0, 9, 9).sprite(0, 0, 6, 9).cullFace(Direction.NORTH).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        emitter.square(Direction.WEST, 0.0f, 0.4f, 0.4f, 0.6f, 0.4f).spriteColor(0, -1, -1, -1, -1).sprite(0, 0, 6, 4).sprite(1, 0, 9, 4).sprite(2, 0, 9, 11).sprite(3, 0, 6, 11).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        emitter.square(Direction.EAST, 0.6f, 0.4f, 1.0f, 0.6f, 0.4f).spriteColor(0, -1, -1, -1, -1).sprite(0, 0, 6, 4).sprite(1, 0, 9, 4).sprite(2, 0, 9, 11).sprite(3, 0, 6, 11).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        emitter.square(Direction.DOWN, 0.4f, 0.0f, 0.6f, 0.4f, 0.4f).spriteColor(0, -1, -1, -1, -1).sprite(1, 0, 6, 4).sprite(2, 0, 9, 4).sprite(3, 0, 9, 11).sprite(0, 0, 6, 11).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        emitter.square(Direction.UP, 0.4f, 0.6f, 0.6f, 1.0f, 0.4f).spriteColor(0, -1, -1, -1, -1).sprite(1, 0, 6, 4).sprite(2, 0, 9, 4).sprite(3, 0, 9, 11).sprite(0, 0, 6, 11).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        this.north = meshBuilder.build();
        emitter.square(Direction.SOUTH, 0.4f, 0.4f, 0.6f, 0.6f, 0.0f).spriteColor(0, -1, -1, -1, -1).sprite(0, 0, 6, 6).sprite(1, 0, 9, 6).sprite(2, 0, 9, 9).sprite(3, 0, 6, 9).cullFace(Direction.SOUTH).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        emitter.square(Direction.EAST, 0.0f, 0.4f, 0.4f, 0.6f, 0.4f).spriteColor(0, -1, -1, -1, -1).sprite(0, 0, 6, 4).sprite(1, 0, 9, 4).sprite(2, 0, 9, 11).sprite(3, 0, 6, 11).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        emitter.square(Direction.WEST, 0.6f, 0.4f, 1.0f, 0.6f, 0.4f).spriteColor(0, -1, -1, -1, -1).sprite(0, 0, 6, 4).sprite(1, 0, 9, 4).sprite(2, 0, 9, 11).sprite(3, 0, 6, 11).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        emitter.square(Direction.UP, 0.4f, 0.0f, 0.6f, 0.4f, 0.4f).spriteColor(0, -1, -1, -1, -1).sprite(1, 0, 6, 4).sprite(2, 0, 9, 4).sprite(3, 0, 9, 11).sprite(0, 0, 6, 11).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        emitter.square(Direction.DOWN, 0.4f, 0.6f, 0.6f, 1.0f, 0.4f).spriteColor(0, -1, -1, -1, -1).sprite(1, 0, 6, 4).sprite(2, 0, 9, 4).sprite(3, 0, 9, 11).sprite(0, 0, 6, 11).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        this.south = meshBuilder.build();
        emitter.square(Direction.WEST, 0.4f, 0.4f, 0.6f, 0.6f, 0.0f).spriteColor(0, -1, -1, -1, -1).sprite(1, 0, 6, 6).sprite(2, 0, 9, 6).sprite(3, 0, 9, 9).sprite(0, 0, 6, 9).cullFace(Direction.WEST).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        emitter.square(Direction.NORTH, 0.6f, 0.4f, 1.0f, 0.6f, 0.4f).spriteColor(0, -1, -1, -1, -1).sprite(0, 0, 6, 4).sprite(1, 0, 9, 4).sprite(2, 0, 9, 11).sprite(3, 0, 6, 11).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        emitter.square(Direction.SOUTH, 0.0f, 0.4f, 0.4f, 0.6f, 0.4f).spriteColor(0, -1, -1, -1, -1).sprite(0, 0, 6, 4).sprite(1, 0, 9, 4).sprite(2, 0, 9, 11).sprite(3, 0, 6, 11).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        emitter.square(Direction.UP, 0.0f, 0.4f, 0.4f, 0.6f, 0.4f).spriteColor(0, -1, -1, -1, -1).sprite(0, 0, 6, 4).sprite(1, 0, 9, 4).sprite(2, 0, 9, 11).sprite(3, 0, 6, 11).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        emitter.square(Direction.DOWN, 0.0f, 0.4f, 0.4f, 0.6f, 0.4f).spriteColor(0, -1, -1, -1, -1).sprite(0, 0, 6, 4).sprite(1, 0, 9, 4).sprite(2, 0, 9, 11).sprite(3, 0, 6, 11).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        this.west = meshBuilder.build();
        emitter.square(Direction.EAST, 0.4f, 0.4f, 0.6f, 0.6f, 0.0f).spriteColor(0, -1, -1, -1, -1).sprite(0, 0, 6, 6).sprite(1, 0, 9, 6).sprite(2, 0, 9, 9).sprite(3, 0, 6, 9).cullFace(Direction.EAST).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        emitter.square(Direction.SOUTH, 0.6f, 0.4f, 1.0f, 0.6f, 0.4f).spriteColor(0, -1, -1, -1, -1).sprite(0, 0, 6, 4).sprite(1, 0, 9, 4).sprite(2, 0, 9, 11).sprite(3, 0, 6, 11).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        emitter.square(Direction.NORTH, 0.0f, 0.4f, 0.4f, 0.6f, 0.4f).spriteColor(0, -1, -1, -1, -1).sprite(0, 0, 6, 4).sprite(1, 0, 9, 4).sprite(2, 0, 9, 11).sprite(3, 0, 6, 11).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        emitter.square(Direction.DOWN, 0.6f, 0.4f, 1.0f, 0.6f, 0.4f).spriteColor(0, -1, -1, -1, -1).sprite(0, 0, 6, 4).sprite(1, 0, 9, 4).sprite(2, 0, 9, 11).sprite(3, 0, 6, 11).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        emitter.square(Direction.UP, 0.6f, 0.4f, 1.0f, 0.6f, 0.4f).spriteColor(0, -1, -1, -1, -1).sprite(0, 0, 6, 4).sprite(1, 0, 9, 4).sprite(2, 0, 9, 11).sprite(3, 0, 6, 11).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        this.east = meshBuilder.build();
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        Walkway walkway = ((Walkway) blockView.getBlockEntity(pos));
        Consumer<Mesh> meshConsumer = context.meshConsumer();
        assert walkway != null;

        int x = 0;
        int y = 0;
        switch (walkway.getDirection()) {
            case DOWN -> x = 180;
            case NORTH -> x = 270;
            case SOUTH -> x = 90;
            case EAST -> {
                x = 90;
                y = 90;
            }
            case WEST -> {
                x = 90;
                y = 270;
            }
        }

        Transform.INSTANCE.setQuaternions(Vec3f.POSITIVE_X.getDegreesQuaternion(x), Vec3f.POSITIVE_Y.getDegreesQuaternion(y));
        context.pushTransform(Transform.INSTANCE);
        context.fallbackConsumer().accept(this.platform);
        context.popTransform();

        if (walkway.getConnections()[0]) {
            meshConsumer.accept(this.down);
        }
        if (walkway.getConnections()[1]) {
            meshConsumer.accept(this.up);
        }
        if (walkway.getConnections()[2]) {
            meshConsumer.accept(this.north);
        }
        if (walkway.getConnections()[3]) {
            meshConsumer.accept(this.south);
        }
        if (walkway.getConnections()[4]) {
            meshConsumer.accept(this.west);
        }
        if (walkway.getConnections()[5]) {
            meshConsumer.accept(this.east);
        }
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        QuadEmitter emitter = context.getEmitter();
        for (Direction direction : Constant.Misc.DIRECTIONS) {
            emitter.square(direction, 0.4f, 0.4f, 0.6f, 0.6f, 0.4f).spriteBake(0, this.sprite, MutableQuadView.BAKE_NORMALIZED & MutableQuadView.BAKE_LOCK_UV).emit();
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean hasDepth() {
        return true;
    }

    @Override
    public boolean isSideLit() {
        return true;
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    @Override
    public Sprite getParticleSprite() {
        return this.sprite;
    }

    @Override
    public ModelTransformation getTransformation() {
        return ModelTransformation.NONE;
    }

    @Override
    public ModelOverrideList getOverrides() {
        return ModelOverrideList.EMPTY;
    }

    public static WalkwayBakedModel getInstance(ModelLoader loader, Function<SpriteIdentifier, Sprite> spriteFunction, ModelBakeSettings rotationContainer) {
        if (instance == null) {
            return instance = new WalkwayBakedModel(loader, spriteFunction, rotationContainer);
        }
        return instance;
    }

    public static void invalidate() {
        instance = null;
    }

    public enum Transform implements RenderContext.QuadTransform {
        INSTANCE;

        private Quaternion quaternionX;
        private Quaternion quaternionY;
        private final Vec3f vec = new Vec3f();

        public void setQuaternions(Quaternion quaternionX, Quaternion quaternionY) {
            this.quaternionX = quaternionX;
            this.quaternionY = quaternionY;
        }

        @Override
        public boolean transform(MutableQuadView quad) {
            for (int i = 0; i < 4; i++) {
                quad.copyPos(i, vec);
                vec.set(vec.getX() - 0.5f, vec.getY() - 0.5f, vec.getZ() - 0.5f);
                vec.rotate(quaternionX);
                vec.rotate(quaternionY);
                vec.set(vec.getX() + 0.5f, vec.getY() + 0.5f, vec.getZ() + 0.5f);
                quad.pos(i, vec);
            }
            return true;
        }
    }
}
