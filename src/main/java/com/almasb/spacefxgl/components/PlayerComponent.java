package com.almasb.spacefxgl.components;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.LocalTimer;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class PlayerComponent extends Component {

    private LocalTimer shootTimer = newLocalTimer();

    public void moveUp() {
        entity.translateY(-5);
    }

    public void moveDown() {
        entity.translateY(5);
    }

    public void moveLeft() {
        entity.translateX(-5);
    }

    public void moveRight() {
        entity.translateX(5);
    }

    public void shoot() {
        if (shootTimer.elapsed(Duration.seconds(0.1))) {
            spawn("torpedo", entity.getCenter());

            shootTimer.capture();
        }
    }

    public void shootBurst() {
        for (int angle = 0; angle < 360; angle += 36) {
            spawn("torpedo", new SpawnData(entity.getCenter()).put("dir", Vec2.fromAngle(angle).toPoint2D()));
        }
    }

    public void shootRocket() {
        if (shootTimer.elapsed(Duration.seconds(0.1))) {
            spawn("rocket", entity.getCenter());

            shootTimer.capture();
        }
    }

    public void activateShield() {

    }
}
