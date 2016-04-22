package main;

import java.util.List;
import java.util.Map;

public interface InflectionCallback {
    void call(String category, String originalWord, List<String> nativeList, List<String> foreignList);
}
