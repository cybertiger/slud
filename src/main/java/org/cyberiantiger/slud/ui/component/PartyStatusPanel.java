package org.cyberiantiger.slud.ui.component;

import lombok.Getter;
import org.cyberiantiger.slud.model.PartyMember;
import org.cyberiantiger.slud.ui.ImageCache;
import org.cyberiantiger.slud.ui.model.AvatarParty;
import org.cyberiantiger.slud.ui.model.AvatarPartyMember;
import org.cyberiantiger.slud.ui.model.ChangeListener;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Iterator;

public class PartyStatusPanel extends JPanel {
    /**
     *  Max party size excluding yourself.
     */
    private static final int MAX_PARTY_SIZE = 9;
    private final PartyMemberPanel[] partyMembers;
    @Getter
    private final ChangeListener<AvatarParty> partyListener = new ChangeListener<AvatarParty>() {
        @Override
        public void stateChanged(AvatarParty state) {
            Iterator<AvatarPartyMember> children = state.getChildren().iterator();

            for (int i = 0; i < MAX_PARTY_SIZE; i++) {
                if (children.hasNext()) {
                    partyMemberListeners[i].attach(children.next());
                } else {
                    partyMemberListeners[i].detach();
                }
            }
        }
    };
    private final PartyMemberListener[] partyMemberListeners;

    public PartyStatusPanel(ImageCache cache) {
        setLayout(new GridLayout(MAX_PARTY_SIZE, 1));
        setOpaque(false);
        partyMembers = new PartyMemberPanel[MAX_PARTY_SIZE];
        partyMemberListeners = new PartyMemberListener[MAX_PARTY_SIZE];
        for (int i = 0; i < MAX_PARTY_SIZE; i++) {
            partyMembers[i] = new PartyMemberPanel(cache);
            partyMemberListeners[i] = new PartyMemberListener(partyMembers[i]);
            add(partyMembers[i]);
        }
    }

    private static class PartyMemberListener implements ChangeListener<AvatarPartyMember> {
        private final PartyMemberPanel panel;
        private AvatarPartyMember source = null;

        public PartyMemberListener(PartyMemberPanel panel) {
            this.panel = panel;
        }

        public void attach(AvatarPartyMember member) {
            if (source != null) {
                source.removeChangeListener(this);
            }
            member.addChangeListener(this);
            source = member;
            panel.setDrawChildren(true);
            stateChanged(source);
        }

        public void detach() {
            if (source != null) {
                source.removeChangeListener(this);
            }
            panel.setDrawChildren(false);
        }

        @Override
        public void stateChanged(AvatarPartyMember state) {
            panel.getHp().setValue(state.getHp());
            panel.getMp().setValue(state.getMp());
            panel.getSp().setValue(state.getSp());
            panel.getIcon().setMember(
                    state.getRace(), state.getGender(), state.getPrimaryClass(), state.getSecondaryClass());
        }
    }
}
