package org.cyberiantiger.slud.ui.model;


import dagger.Lazy;

import javax.inject.Inject;
import java.util.List;

import static java.util.Collections.emptyList;

public class AvatarExperience extends AbstractChangable<Avatar, AvatarExperience> {
    @Inject
    public AvatarExperience(Lazy<Avatar> parent) {
        super(parent);
    }

    @Override
    public List<Changeable<AvatarExperience, ?>> getChildren() {
        return emptyList();
    }
}
