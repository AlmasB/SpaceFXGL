package com.almasb.spacefxgl;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.components.*;
import com.almasb.fxgl.dsl.effects.SlowTimeEffect;
import com.almasb.fxgl.dsl.views.SelfScrollingBackgroundView;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.TimeComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.spacefxgl.components.PlayerComponent;
import com.almasb.spacefxgl.components.SelfRotateComponent;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.shape.CubicCurve;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.spacefxgl.EntityType.*;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class SpaceFactory implements EntityFactory {

    // TODO: check why vertical line
    @Spawns("background")
    public Entity newBackground(SpawnData data) {
        return entityBuilder(data)
                .view(new SelfScrollingBackgroundView(image("backgroundL1.jpg"), 700, 900, Orientation.VERTICAL, -25.0))
                .view(new SelfScrollingBackgroundView(image("backgroundL2.jpg"), 700, 900, Orientation.VERTICAL, -25.0))
                .view(new SelfScrollingBackgroundView(image("backgroundL3.jpg"), 700, 900, Orientation.VERTICAL, -25.0))
                .build();
    }

    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        return entityBuilder(data)
                .type(PLAYER)
                .viewWithBBox(texture("spaceship.png", 1280 / 28.0, 1280 / 28.0))
                .collidable()
                .with(new KeepOnScreenComponent().bothAxes())
                .with(new PlayerComponent())
                .build();
    }

    @Spawns("asteroid")
    public Entity newAsteroid(SpawnData data) {
        var num = FXGLMath.random(1, 11);

        var e = entityBuilder(data)
                .type(ASTEROID)
                .viewWithBBox(texture("asteroid" + num + ".png", 500 / 8.0, 500 / 8.0))
                .collidable()
                .with(new ProjectileComponent(FXGLMath.randomPoint2D(), random(50, 150)).allowRotation(false))
                .with(new SelfRotateComponent(random(5.5, 25.5)))
                .build();

        runOnce(() -> e.addComponent(new OffscreenCleanComponent()), Duration.seconds(2));

        return e;
    }

    @Spawns("enemy")
    public Entity newEnemy(SpawnData data) {
        var texture = texture("enemy1L1.png", 1024 / 20.0, 1024 / 20.0);
        texture.setRotate(90);

        var e = entityBuilder(data)
                .type(ENEMY)
                .viewWithBBox(texture)
                .collidable()
                .with(new AutoRotationComponent().withSmoothing())
                .build();

        animationBuilder()
                .repeatInfinitely()
                .autoReverse(true)
                .duration(Duration.seconds(5))
                .interpolator(Interpolators.SMOOTH.EASE_IN_OUT())
                .translate(e)
                .alongPath(new CubicCurve(
                        data.getX(), data.getY(),
                        random(0, getAppWidth() / 2), getAppHeight() / 3.0,
                        random(0, getAppWidth()), random(getAppHeight() / 3.0, getAppHeight() / 3.0 * 2),
                        random(0, getAppWidth() - 50), getAppHeight() - 50
                ))
                .buildAndPlay();

        return e;
    }

    @Spawns("torpedo")
    public Entity newTorpedo(SpawnData data) {
        var t = texture("torpedo.png");
        t.setRotate(90);

        Point2D dir = data.hasKey("dir") ? data.get("dir") : new Point2D(0, -1);

        return entityBuilder(data)
                .type(TORPEDO)
                .at(data.getX() - 17 / 2.0, data.getY() - 20 / 2.0)
                .viewWithBBox(t)
                .collidable()
                .with(new ProjectileComponent(dir, 750))
                .with(new OffscreenCleanComponent())
                .build();
    }

    @Spawns("rocket")
    public Entity newRocket(SpawnData data) {
        var w = 131 / 10.0;
        var h = 388 / 10.0;

        var effect = new EffectComponent();

        var e = entityBuilder(data)
                .type(TORPEDO)
                .at(data.getX() - w / 2.0, data.getY() - h / 2.0)
                .viewWithBBox(texture("rocket.png", w, h))
                .collidable()
                .with(new TimeComponent())
                .with(effect)
                .with(new ProjectileComponent(new Point2D(0, -1), 1750).allowRotation(false))
                .with(new OffscreenCleanComponent())
                .build();

        effect.startEffect(new SlowTimeEffect(0.05, Duration.seconds(1)));

        return e;
    }

    @Spawns("torpedoHit")
    public Entity newTorpedoHit(SpawnData data) {
        var num = FXGLMath.random(1, 4);

        var w = 400 / 1.0;
        var h = 160 / 1.0;

        var animChannel = new AnimationChannel(image("torpedoHitL" + num + ".png", w, h), 5, (int) (w / 5), (int) (h / 2), Duration.seconds(0.5), 0, 9);

        return entityBuilder(data)
                .at(data.getX() - 40, data.getY() - 40)
                .view(new AnimatedTexture(animChannel).play())
                .with(new ExpireCleanComponent(Duration.seconds(0.6)))
                .build();
    }

    @Spawns("explosion")
    public Entity newExplosion(SpawnData data) {
        var w = 2048 / 4.0;
        var h = 1792 / 4.0;

        var animChannel = new AnimationChannel(image("explosionL1.png", w, h), 8, (int) (w / 8), (int) (h / 7), Duration.seconds(1.0), 0, 55);

        return entityBuilder(data)
                .view(new AnimatedTexture(animChannel).play())
                .with(new ExpireCleanComponent(Duration.seconds(1.1)))
                .build();
    }
}
