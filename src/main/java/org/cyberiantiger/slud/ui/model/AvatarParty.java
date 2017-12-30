package org.cyberiantiger.slud.ui.model;

import dagger.Lazy;
import org.cyberiantiger.slud.model.PartyMember;
import org.cyberiantiger.slud.model.PartyVitals;

import javax.inject.Inject;
import java.util.*;

import static java.util.Collections.emptyList;

public class AvatarParty extends AbstractChangable<Avatar, AvatarParty> {
    // Use treemap to sort alphabetical.
    private Map<String, AvatarPartyMember> members = new TreeMap<>();

    @Inject
    public AvatarParty(Lazy<Avatar> parent) {
        super(parent);
    }

    @Override
    public Collection<AvatarPartyMember> getChildren() {
        return members.values();
    }

    public void handleGmcpPartyMembers(Map<String, PartyMember> party) {
        boolean changed = false;
        for (Map.Entry<String, PartyMember> e : party.entrySet()) {
            String name = e.getKey();
            PartyMember update = e.getValue();
            AvatarPartyMember modelMember = members.get(name);
            if (modelMember == null) {
                changed = true;
                modelMember = new AvatarPartyMember(() -> this, name);
                members.put(name, modelMember);
            }
            modelMember.updateDetails(update);
        }
        // Remove party members.
        changed |= members.keySet().retainAll(party.keySet());
        if (changed) {
            setChanged();
        }
    }

    public void handleGmcpPartyVitals(Map<String, PartyVitals> party) {
        party.forEach((k, v) -> Optional.ofNullable(members.get(k)).ifPresent(member -> member.updateVitals(v)));
    }
}
