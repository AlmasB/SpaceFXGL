package com.almasb.spacefxgl.components;

import com.almasb.fxgl.entity.component.Component;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class SelfRotateComponent extends Component {

    private double rotateSpeed;

    public SelfRotateComponent(double rotateSpeed) {
        this.rotateSpeed = rotateSpeed;
    }

    @Override
    public void onUpdate(double tpf) {
        entity.rotateBy(rotateSpeed * tpf);
    }
}
