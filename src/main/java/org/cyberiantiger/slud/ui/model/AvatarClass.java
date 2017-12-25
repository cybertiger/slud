package org.cyberiantiger.slud.ui.model;

import dagger.Lazy;

import javax.inject.Inject;
import java.util.List;

import static java.util.Collections.emptyList;

public class AvatarClass extends AbstractChangable<Avatar, AvatarClass> {

    @Inject
    protected AvatarClass(Lazy<Avatar> parent) {
        super(parent);
    }

    @Override
    public List<Changeable<AvatarClass, ?>> getChildren() {
        return emptyList();
    }
}
