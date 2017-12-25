package org.cyberiantiger.slud.ui.model;

import dagger.Lazy;

import javax.inject.Inject;
import java.util.List;

import static java.util.Collections.emptyList;

public class AvatarVital extends AbstractChangable<Avatar, AvatarVital> {
    @Inject
    public AvatarVital(Lazy<Avatar> parent) {
        super(parent);
    }

    @Override
    public List<Changeable<AvatarVital, ?>> getChildren() {
        return emptyList();
    }
}
