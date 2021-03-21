package com.almasb.spacefxgl.ui;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.view.KeyView;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static com.almasb.fxgl.dsl.FXGL.*;
import static javafx.scene.input.KeyCode.*;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class SpaceMainMenu extends FXGLMenu {

    public SpaceMainMenu() {
        super(MenuType.MAIN_MENU);

        var bg = texture("backgroundL1.jpg").subTexture(new Rectangle2D(0, 0, getAppWidth(), getAppHeight()));

        var title = getUIFactoryService().newText(getSettings().getTitle() + " v. " + getSettings().getVersion(), Color.BLUE, 32.0);
        centerTextBind(title, getAppWidth() / 2.0, 100);

        animationBuilder(this)
                .autoReverse(true)
                .repeatInfinitely()
                .animate(title.fillProperty())
                .from(Color.BLUE)
                .to(Color.WHITE)
                .buildAndPlay();

        var description = new Text("This is an FXGL clone of SpaceFX." +
                "\nSpaceFX was originally developed by Gerrit Grunwald." +
                "\nhttps://github.com/HanSolo/SpaceFX");

        description.setFont(Font.font(16));
        description.setFill(Color.WHITE);
        description.setTranslateX(315);
        description.setTranslateY(getAppHeight() - 50);

        GridPane pane = new GridPane();
        pane.setEffect(new DropShadow(10, 5, 5, Color.BLUE));
        pane.setHgap(25);
        pane.setVgap(10);
        pane.addRow(0, getUIFactoryService().newText("Movement"), new HBox(4, new KeyView(W), new KeyView(S), new KeyView(A), new KeyView(D)));
        pane.addRow(1, getUIFactoryService().newText("Shoot"), new KeyView(SPACE));
        pane.addRow(2, getUIFactoryService().newText("Shield"), new KeyView(E));
        pane.addRow(3, getUIFactoryService().newText("Rockets"), new KeyView(R));
        pane.addRow(4, getUIFactoryService().newText("Burst"), new KeyView(T));

        pane.addRow(7, getUIFactoryService().newText("Start game"), new KeyView(ENTER));
        pane.addRow(8, getUIFactoryService().newText("Exit game"), new KeyView(X));

        pane.setTranslateX(200);
        pane.setTranslateY(400);

        getContentRoot().getChildren().addAll(bg, title, description, pane);

        onKeyBuilder(getInput(), ENTER)
                .onActionBegin(() -> fireNewGame());

        onKeyBuilder(getInput(), X)
                .onActionBegin(() -> fireExit());
    }
}
