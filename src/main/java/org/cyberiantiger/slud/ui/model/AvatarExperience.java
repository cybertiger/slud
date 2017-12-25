package org.cyberiantiger.slud.ui.model;


import dagger.Lazy;
import lombok.Getter;

import javax.inject.Inject;
import java.util.List;

import static java.util.Collections.emptyList;

public class AvatarExperience extends AbstractChangable<Avatar, AvatarExperience> {
    @Getter
    private long value;
    @Getter
    private long min;
    @Getter
    private long max;

    @Inject
    public AvatarExperience(Lazy<Avatar> parent) {
        super(parent);
    }

    public void setValue(long value) {
        this.value = value;
        setChanged();
    }

    public void setMin(long min) {
        this.min = min;
        setChanged();
    }

    public void setMax(long max) {
        this.max = max;
        setChanged();
    }

    @Override
    public void reset() {
        super.reset();
        this.value = 0;
        this.min = 0;
        this.max = 0;
    }

    @Override
    public List<Changeable<AvatarExperience, ?>> getChildren() {
        return emptyList();
    }
}
