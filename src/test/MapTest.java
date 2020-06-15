package test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapTest {
    public static void main(String[] args) {
        ConcurrentHashMap<Integer, Integer> chm = new ConcurrentHashMap<>();
        for (int i = 0; i < 1000; i = i+16) {
            chm.put(i, i);
        }


        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
    }
}
