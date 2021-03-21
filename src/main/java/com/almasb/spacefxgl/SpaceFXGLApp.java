package com.almasb.spacefxgl;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.SimpleGameMenu;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.spacefxgl.components.PlayerComponent;
import com.almasb.spacefxgl.ui.SpaceMainMenu;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.spacefxgl.EntityType.*;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class SpaceFXGLApp extends GameApplication {

    private PlayerComponent playerComp;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(700);
        settings.setHeight(900);
        settings.setTitle("Space FXGL");
        settings.setVersion("0.1alpha");
        settings.setFontUI("spaceboy.ttf");
        settings.setAppIcon("icon.png");
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(true);

        settings.setSceneFactory(new SceneFactory() {
            @Override
            public FXGLMenu newMainMenu() {
                return new SpaceMainMenu();
            }

            @Override
            public FXGLMenu newGameMenu() {
                return new SimpleGameMenu();
            }
        });
    }

    @Override
    protected void initInput() {
        onKey(KeyCode.W, () -> playerComp.moveUp());
        onKey(KeyCode.S, () -> playerComp.moveDown());
        onKey(KeyCode.A, () -> playerComp.moveLeft());
        onKey(KeyCode.D, () -> playerComp.moveRight());

        onKeyDown(KeyCode.E, () -> playerComp.activateShield());
        onKeyDown(KeyCode.R, () -> playerComp.shootRocket());
        onKeyDown(KeyCode.T, () -> playerComp.shootBurst());
        onKey(KeyCode.SPACE, () -> playerComp.shoot());
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("score", 0);
        vars.put("lives", 4);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new SpaceFactory());

        spawn("background");

        var player = spawn("player", getAppCenter().add(0, 250));
        playerComp = player.getComponent(PlayerComponent.class);

        run(() -> {
            spawn("asteroid", random(0, getAppWidth()), random(-200, -50));
        }, Duration.seconds(0.1));

        run(() -> {

            for (int i = 0; i < 4; i++) {
                spawnFadeIn("enemy", new SpawnData(random(0, getAppWidth() - 50), random(0, 300)), Duration.seconds(0.25));
            }

        }, Duration.seconds(3));
    }

    @Override
    protected void initPhysics() {
        onCollisionBegin(TORPEDO, ASTEROID, (t, a) -> {

            inc("score", +100);

            //spawn("explosion", a.getPosition());
            spawn("torpedoHit", t.getCenter());

            t.removeFromWorld();
            a.removeFromWorld();
        });

        onCollisionBegin(TORPEDO, ENEMY, (t, e) -> {

            inc("score", +200);

            spawn("explosion", e.getPosition());
            spawn("torpedoHit", t.getCenter());

            t.removeFromWorld();
            e.removeFromWorld();
        });
    }

    @Override
    protected void initUI() {
        var text = getUIFactoryService().newText("", Color.LIGHTBLUE, 26.0);
        addUINode(text);
        centerTextBind(text, getAppWidth() / 2.0, 50.0);
        text.textProperty().bind(getip("score").asString());

        var livesBox = new HBox(2);

        for (int i = 0; i < geti("lives"); i++) {
            var icon = texture("spaceship.png", 16, 16);

            livesBox.getChildren().add(icon);
        }

        addUINode(livesBox, 25, 25);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
