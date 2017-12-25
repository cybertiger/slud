package org.cyberiantiger.slud.ui.model;

import dagger.Lazy;

import javax.inject.Inject;
import java.util.List;

import static java.util.Collections.emptyList;

public class AvatarSkills extends AbstractChangable<Avatar,AvatarSkills> {
    @Inject
    public AvatarSkills(Lazy<Avatar> parent) {
        super(parent);
    }

    @Override
    public List<Changeable<AvatarSkills, ?>> getChildren() {
        return emptyList();
    }
}
