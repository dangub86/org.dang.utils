package org.dang.utils;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MapUtilsTest {

    private Map<String, Object> simpleMap;
    private Map<String, Object> oneLevelDownMap;
    private Map<String, Object> twoLevelsDownMap;
    private Map<String, Object> multipleLevelsMap;
    private Map<String, Object> multipleLevelsMapDuplicateKeys;
    private Map<String, Object> anotherSimpleMap;

    @Before
    public void setUp() throws Exception {
        simpleMap = new LinkedHashMap<String, Object>(){{
            put("name", "Dan");
            put("surname", "Gub");
            put("email", "simple@example.com");
            put("mobile", "055-7896222");
            put("address", "122th Street");
            put("town", "Manhattan");
        }};

        anotherSimpleMap = new LinkedHashMap<String, Object>(){{
            put("name", "Dan");
            put("surname", "Gub");
            put("email", "user@example.com");
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
            put("simpleUser", simpleMap);
            put("info", twoLevelsDownMap);
            put("contacts", new LinkedHashMap<String, Object>() {{
                put("mobile", "055-7896222");
                put("email", "dan@example.com");
            }});
            put("address", "122th Street");
            put("town", "Manhattan");
        }};

        multipleLevelsMapDuplicateKeys = new LinkedHashMap<String, Object>(){{
            put("user", anotherSimpleMap);
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

    @Test
    public void givenOneKey_searchRecursivelyInMap() {
        String email = MapUtils.findValueRecursivelyInMap(twoLevelsDownMap, "email", String.class);
        assertEquals("dan@example.com", email);
    }

    @Test
    public void givenFourKeys_searchInMultilevelsMap() {
        String email = MapUtils.findValuesInMapByKeys(multipleLevelsMap, String.class,  "info", "data", "user", "email");
        assertEquals("dan@example.com", email);
    }

    @Ignore
    @Test
    public void givenFourKeys_searchInMultilevelsMapWithDuplicatedKeys() {
        String email = MapUtils.findValuesInComplexMapByKeys(multipleLevelsMapDuplicateKeys, "email", String.class,  "info", "data", "user");
        assertEquals("simple@example.com", email);
    }




}