package org.cyberiantiger.slud.ui.model;

import dagger.Lazy;

import javax.inject.Inject;
import java.util.List;

import static java.util.Collections.emptyList;

public class AvatarStats extends AbstractChangable<Avatar, AvatarSkills> {
    @Inject
    public AvatarStats(Lazy<Avatar> parent) {
        super(parent);
    }

    @Override
    public List<Changeable<AvatarSkills, ?>> getChildren() {
        return emptyList();
    }
}
