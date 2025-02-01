package com.javarush.kuzin.caesarcipher;

import java.util.HashMap;
import java.util.Map;

public class CeasarCipher {

    // Метод для шифрования
    public static String encrypt(String text, int shift, String language) {
        StringBuilder result = new StringBuilder();
        String alphabet = getAlphabet(language);

        for (char character : text.toCharArray()) {
            if (Character.isLetter(character)) {
                char base = getBase(character, language);
                int alphabetLength = alphabet.length();
                int charPosition = alphabet.indexOf(Character.toLowerCase(character));
                int newPosition = (charPosition + shift) % alphabetLength;
                if (newPosition < 0) {
                    newPosition += alphabetLength;
                }
                char newChar = alphabet.charAt(newPosition);
                result.append(Character.isLowerCase(character) ? newChar : Character.toUpperCase(newChar));
            } else {
                result.append(character);
            }
        }

        return result.toString();
    }

    // Метод для дешифрования
    public static String decrypt(String text, int shift, String language) {
        return encrypt(text, -shift, language);
    }

    // Метод для частотного анализа и автоматической расшифровки
    public static String frequencyAnalysisDecrypt(String text, String language) {
        String alphabet = getAlphabet(language);
        Map<Character, Integer> frequencyMap = getFrequencyMap(text, alphabet);
        char mostFrequentChar = getMostFrequentChar(frequencyMap);
        char mostFrequentCharInLanguage = getMostFrequentCharInLanguage(language);

        int shift = (alphabet.indexOf(mostFrequentChar) - alphabet.indexOf(mostFrequentCharInLanguage) + alphabet.length()) % alphabet.length();
        return decrypt(text, shift, language);
    }

    // Получение алфавита
    private static String getAlphabet(String language) {
        if ("ru".equals(language)) {
            return "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
        } else {
            return "abcdefghijklmnopqrstuvwxyz";
        }
    }

    // Получение базового символа
    private static char getBase(char character, String language) {
        if ("ru".equals(language)) {
            return Character.isLowerCase(character) ? 'а' : 'А';
        } else {
            return Character.isLowerCase(character) ? 'a' : 'A';
        }
    }

    // Создание карты частот символов
    private static Map<Character, Integer> getFrequencyMap(String text, String alphabet) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char character : text.toLowerCase().toCharArray()) {
            if (alphabet.indexOf(character) != -1) {
                frequencyMap.put(character, frequencyMap.getOrDefault(character, 0) + 1);
            }
        }
        return frequencyMap;
    }

    // Получение самого частого символа
    private static char getMostFrequentChar(Map<Character, Integer> frequencyMap) {
        return frequencyMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow();
    }

    // Получение самого частого символа в языке
    private static char getMostFrequentCharInLanguage(String language) {
        if ("ru".equals(language)) {
            return 'о'; // Самый частый символ в русском языке
        } else {
            return 'e'; // Самый частый символ в английском языке
        }
    }
}