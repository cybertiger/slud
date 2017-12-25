package org.cyberiantiger.slud.ui.model;

import dagger.Lazy;
import lombok.Getter;

import javax.inject.Inject;
import java.util.List;

import static java.util.Collections.emptyList;

public class AvatarVital extends AbstractChangable<Avatar, AvatarVital> {
    @Getter
    private int value;
    @Getter
    private int max;

    @Inject
    public AvatarVital(Lazy<Avatar> parent) {
        super(parent);
    }

    public void setValue(int value) {
        this.value = value;
        setChanged();
    }

    public void setMax(int max) {
        this.max = max;
        setChanged();
    }

    @Override
    public void reset() {
        super.reset();
        this.value = 0;
        this.max = 0;
    }

    @Override
    public List<Changeable<AvatarVital, ?>> getChildren() {
        return emptyList();
    }
}
