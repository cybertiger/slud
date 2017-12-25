package org.cyberiantiger.slud.ui.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Party {
    String name; /* TODO, not implemented in protocol */
    Map<String, PartyMemberStatus> members = new HashMap<>();
}
