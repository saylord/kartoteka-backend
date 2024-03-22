package kz.logitex.kartoteka.util;

import lombok.extern.slf4j.Slf4j;

import java.text.Normalizer;
import java.util.Locale;

@Slf4j
public class StringModifier {
    // Method for normalizing and converting a string to lowercase
    public static String normalizeAndLowerCase(String input) {
        if (input == null) {
            return null;
        }

        // Normalize the string using NFC form
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFC);

        // Convert to lowercase with the default locale
        return normalized.toLowerCase(Locale.getDefault());
    }
}
