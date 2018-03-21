package ru.track.cypher;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Вспомогательные методы шифрования/дешифрования
 */
public class CypherUtil {

    public static final String SYMBOLS = "abcdefghijklmnopqrstuvwxyz";

    /**
     * Генерирует таблицу подстановки - то есть каждой буква алфавита ставится в соответствие другая буква
     * Не должно быть пересечений (a -> x, b -> x). Маппинг уникальный
     *
     * @return таблицу подстановки шифра
     */
    @NotNull
    public static Map<Character, Character> generateCypher() {
        List<Character> shuffledSymbols = new ArrayList();
        Collections.addAll(shuffledSymbols, ArrayUtils.toObject(SYMBOLS.toCharArray()));
        List<Character> symbols = new ArrayList(shuffledSymbols);
        Collections.shuffle(shuffledSymbols);
        Map<Character, Character> cypher = new HashMap<>();
        for (int i = 0; i < SYMBOLS.length(); i++) {
            cypher.put(symbols.get(i), shuffledSymbols.get(i));
        }
        return cypher;
    }

}
