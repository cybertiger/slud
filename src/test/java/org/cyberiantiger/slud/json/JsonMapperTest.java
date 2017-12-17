package org.cyberiantiger.slud.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.BasicDeserializerFactory;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.std.EnumSetDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.cyberiantiger.slud.model.Item;
import org.cyberiantiger.slud.model.ItemType;
import org.cyberiantiger.slud.model.Limb;
import org.cyberiantiger.slud.model.LimbStatus;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class JsonMapperTest {
    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testLimbStatus() throws IOException {
        System.out.println((Object)
                mapper.readValue("{\"hp\":0,\"maxhp\":1,\"bandaged\":0,\"broken\":0,\"severed\":0}",
                new TypeReference<LimbStatus>() {}));
    }

    @Test
    public void testNullItem() throws IOException {
        System.out.println(mapper.readValue("0", Item.class));
    }

    @Test
    public void testObjectValue() throws IOException {
        System.out.println((Object) mapper.readValue("{\"test\":0}", new TypeReference<Map<String, Item>>() {}));
    }

    @Test
    public void testEnum() throws IOException {
        assertEquals(ItemType.NONE, mapper.readValue("0", ItemType.class));
    }

    @Test
    public void testEnumSet() throws IOException {
        // assertEquals(EnumSet.noneOf(Limb.class), mapper.readValue("0", new TypeReference<EnumSet<Limb>>() {}));
    }

    @Test
    public void testSet() throws IOException {
        // assertEquals(EnumSet.noneOf(Limb.class), mapper.readValue("0", new TypeReference<Set<Limb>>() {}));
    }

}
