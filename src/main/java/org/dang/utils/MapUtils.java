package org.dang.utils;

import java.util.*;
import java.util.stream.Collectors;

public class MapUtils {

    public static <T> T findValuesInMapByKeys(Map<String, Object> map, Class<?> returnType, String... strings) {
        List<String> stringList = new ArrayList<>(Arrays.asList(strings));

        if (map.containsKey(stringList.get(0)) && !(map.get(stringList.get(0)) instanceof Map)) {
            return (T) returnType.cast(map.get(stringList.get(0)));
        }

        Map.Entry<String, Object> entryMap =  map.entrySet().stream()
                .filter( entry -> stringList.stream()
                           .anyMatch( str -> entry.getKey().equals(str)) )
                .findFirst().orElseThrow( () -> new NoSuchElementException("Keys " + stringList + " you are searching for are not first-level keys in the map: " + map));

        stringList.remove(entryMap.getKey());
        strings = stringList.toArray(new String[stringList.size()]);

        return findValuesInMapByKeys((Map<String, Object>) entryMap.getValue(), returnType, strings);
    }

    public static <T> T findValueRecursivelyInMap(Map<String, Object> map, String key, Class<?> returnType) {
        if (map.containsKey(key) && !(map.get(key) instanceof Map)) {
            return (T) returnType.cast(map.get(key));
        }

        List<? extends Map<String, Object>> objList = (List<? extends Map<String, Object>>) map.entrySet()
                .stream()
                .map(entry -> (Map<String, Object>) entry.getValue())
                .filter(e -> Objects.nonNull(e) && (e instanceof Map))
                .collect(Collectors.toList());

        final List<? extends  Map<String, Object>> objListFinal = objList.stream()
                .skip(1)
                .collect(Collectors.toList());

        return (T) objList.stream()
                .map(newMap -> findValueRecursivelyInMap(newMap, key, returnType, objListFinal))
                .findFirst().get();


    }

    private static <T> T findValueRecursivelyInMap(Map<String, Object> map, String key, Class<?> returnType, List<? extends Map<String, Object>> objList) {
        if (map.containsKey(key) && !(map.get(key) instanceof Map)) {
            return (T) returnType.cast(map.get(key));
        }

        final List<? extends  Map<String, Object>> objListFinal = objList.stream()
                .skip(1)
                .collect(Collectors.toList());

        return (T) objList.stream()
                .map(newMap -> findValueRecursivelyInMap(newMap, key, returnType, objListFinal))
                .findFirst().get();
    }


}
