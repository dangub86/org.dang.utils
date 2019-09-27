package org.dang.utils;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MapUtilsTest {

    Map<String, Object> simpleMap;
    Map<String, Object> oneLevelDownMap;
    Map<String, Object> twoLevelsDownMap;
    Map<String, Object> multipleLevelsMap;

    @Before
    public void setUp() throws Exception {
        simpleMap = new LinkedHashMap<String, Object>(){{
            put("name", "Dan");
            put("surname", "Gub");
            put("email", "dan@example.com");
            put("mobile", "055-7896222");
            put("address", "122th Street");
            put("town", "Manhattan");
        }};

        oneLevelDownMap = new LinkedHashMap<String, Object>(){{
            put("user", simpleMap);
        }};

        twoLevelsDownMap = new LinkedHashMap<String, Object>(){{
            put("data", oneLevelDownMap);
        }};

        multipleLevelsMap = new LinkedHashMap<String, Object>(){{
            put("user", simpleMap);
            put("info", twoLevelsDownMap);
            put("contacts", new LinkedHashMap<String, Object>() {{
                put("mobile", "055-7896222");
                put("email", "dan@example.com");
            }});
            put("address", "122th Street");
            put("town", "Manhattan");
        }};
    }

    @Test
    public void givenOneKey_searchInSimpleMap() {
        String email = MapUtils.findValuesInMapByKeys(simpleMap, String.class, "email");
        assertEquals("dan@example.com", email);
    }

    @Test
    public void givenTwoKeys_searchInOneLevelMap() {
        String email = MapUtils.findValuesInMapByKeys(oneLevelDownMap, String.class, "user", "email");
        assertEquals("dan@example.com", email);
    }

    @Test
    public void givenThreeKeys_searchInTwoLevelsMap() {
        String email = MapUtils.findValuesInMapByKeys(twoLevelsDownMap, String.class,  "data", "email", "user");
        assertEquals("dan@example.com", email);
    }




}