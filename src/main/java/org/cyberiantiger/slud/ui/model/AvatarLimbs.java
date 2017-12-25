package org.cyberiantiger.slud.ui.model;

import dagger.Lazy;

import javax.inject.Inject;
import java.util.List;

import static java.util.Collections.emptyList;

public class AvatarLimbs extends AbstractChangable<Avatar, AvatarLimbs> {
    @Inject
    public AvatarLimbs(Lazy<Avatar> parent) {
        super(parent);
    }

    @Override
    public List<Changeable<AvatarLimbs, ?>> getChildren() {
        return emptyList();
    }
}
