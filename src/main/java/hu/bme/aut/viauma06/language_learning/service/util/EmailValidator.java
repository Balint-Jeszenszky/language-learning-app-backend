package hu.bme.aut.viauma06.language_learning.service.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {

    public static boolean validateEmail(String email) {
        Pattern emailPattern = Pattern.compile("^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");
        Matcher emailMatcher = emailPattern.matcher(email);

        return emailMatcher.matches();
    }
}
