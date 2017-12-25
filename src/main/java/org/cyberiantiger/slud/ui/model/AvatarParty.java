package org.cyberiantiger.slud.ui.model;

import dagger.Lazy;

import javax.inject.Inject;
import java.util.List;

import static java.util.Collections.emptyList;

public class AvatarParty extends AbstractChangable<Avatar, AvatarParty> {
    @Inject
    public AvatarParty(Lazy<Avatar> parent) {
        super(parent);
    }

    @Override
    public List<Changeable<AvatarParty, ?>> getChildren() {
        return emptyList();
    }
}
