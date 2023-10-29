package dk.w4.carcrudsprintbootdemo.util;

import org.springframework.stereotype.Component;

@Component
public class VINValidator {
    // This code was provided by https://introcs.cs.princeton.edu/java/31datatype/VIN.java.html

    int[] values = { 1, 2, 3, 4, 5, 6, 7, 8, 0, 1, 2, 3, 4, 5, 0, 7, 0, 9, 2, 3, 4, 5, 6, 7, 8, 9 };
    int[] weights = { 8, 7, 6, 5, 4, 3, 2, 10, 0, 9, 8, 7, 6, 5, 4, 3, 2 };

    public boolean isValidVIN(String vin) {
        String s = vin.replaceAll("-", "");
        s = s.toUpperCase();
        if (s.length() != 17)
            throw new RuntimeException("VIN number must be 17 characters");

        int sum = 0;
        for (int i = 0; i < 17; i++) {
            char c = s.charAt(i);
            int value;
            int weight = weights[i];

            // letter
            if (c >= 'A' && c <= 'Z') {
                value = values[c - 'A'];
                if (value == 0)
                    throw new RuntimeException("Illegal character: " + c);
            }

            // number
            else if (c >= '0' && c <= '9') value = c - '0';

            // illegal character
            else throw new RuntimeException("Illegal character: " + c);

            sum = sum + weight * value;

        }

        // check digit
        sum = sum % 11;
        char check = s.charAt(8);
        if (check != 'X' && (check < '0' || check > '9'))
            throw new RuntimeException("Illegal check digit: " + check);
        if (sum == 10 && check == 'X') return true;
        else if (sum == check - '0') return true;
        else return false;
    }
}
