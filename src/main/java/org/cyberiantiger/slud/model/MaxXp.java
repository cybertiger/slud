package org.cyberiantiger.slud.model;

import lombok.Value;

@Value
public class MaxXp {
    /**
     * Minimum for current level.
     */
    long min;
    /**
     * Maximum for current level.
     */
    long max;
}
