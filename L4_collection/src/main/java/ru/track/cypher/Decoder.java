package ru.track.cypher;

import java.util.*;

import org.jetbrains.annotations.NotNull;

public class Decoder {

    // Расстояние между A-Z -> a-z
    public static final int SYMBOL_DIST = 32;

    private Map<Character, Character> cypher;

    /**
     * Конструктор строит гистограммы открытого домена и зашифрованного домена
     * Сортирует буквы в соответствие с их частотой и создает обратный шифр Map<Character, Character>
     *
     * @param domain - текст по кторому строим гистограмму языка
     */
    public Decoder(@NotNull String domain, @NotNull String encryptedDomain) {
        Map<Character, Integer> domainHist = createHist(domain);
        Map<Character, Integer> encryptedDomainHist = createHist(encryptedDomain);

        cypher = new LinkedHashMap<>();

        Iterator<Map.Entry<Character, Integer>> domainIterator = domainHist.entrySet().iterator();
        Iterator<Map.Entry<Character, Integer>> encriptedIterator = encryptedDomainHist.entrySet().iterator();
        while (domainIterator.hasNext() && encriptedIterator.hasNext()) {
            cypher.put(encriptedIterator.next().getKey(), domainIterator.next().getKey());
        }
    }

    public Map<Character, Character> getCypher() {
        return cypher;
    }

    /**
     * Применяет построенный шифр для расшифровки текста
     *
     * @param encoded зашифрованный текст
     * @return расшифровка
     */
    @NotNull
    public String decode(@NotNull String encoded) {
        StringBuilder stringBuilder = new StringBuilder();
        for (char ch: encoded.toCharArray()) {
            if (isLetter(ch)) {
                stringBuilder.append(cypher.get(ch));
            } else {
                stringBuilder.append(ch);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Считывает входной текст посимвольно, буквы сохраняет в мапу.
     * Большие буквы приводит к маленьким
     *
     *
     * @param text - входной текст
     * @return - мапа с частотой вхождения каждой буквы (Ключ - буква в нижнем регистре)
     * Мапа отсортирована по частоте. При итерировании на первой позиции наиболее частая буква
     */
    @NotNull
    static Map<Character, Integer> createHist(@NotNull String text) {
        Map<Character, Integer> histMap = new HashMap<>();
        for (Character ch: text.toCharArray()) {
            if (isLetter(ch)) {
                Character lowChar = Character.toLowerCase(ch);
                Integer count = histMap.get(lowChar);
                if (count == null) {
                    histMap.put(lowChar, 1);
                } else {
                    histMap.put(lowChar, count + 1);
                }
            }
        }

        List<Map.Entry<Character, Integer>> entries = new ArrayList<>(histMap.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<Character, Integer>>() {
            @Override
            public int compare(Map.Entry<Character, Integer> characterIntegerEntry, Map.Entry<Character, Integer> t1) {
                return t1.getValue() - characterIntegerEntry.getValue();
            }
        });

        Map<Character, Integer> sortedHistMap = new LinkedHashMap<>();
        for (Map.Entry<Character, Integer> entry: entries) {
            sortedHistMap.put(entry.getKey(), entry.getValue());
        }

        return sortedHistMap;
    }

    static boolean isLetter(char ch) {
        if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
            return true;
        } else {
            return false;
        }
    }


}
