package org.cyberiantiger.slud.json;

import com.fasterxml.jackson.databind.util.StdConverter;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class IntBooleanConverter extends StdConverter<Integer, Boolean> {

    @Override
    public Boolean convert(Integer integer) {
        return integer == 0 ? FALSE : TRUE;
    }
}
