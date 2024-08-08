package mg.edbm.mail.utils;

import org.springframework.util.StringUtils;

public class StringCustomUtils {
    public static String titleCase(String str, String delimiter) {
        if(str == null || delimiter == null) return str;
        final String[] words = str.split(delimiter);
        final StringBuilder sb = new StringBuilder();
        for(String word : words) {
            sb.append(StringUtils.capitalize(word)).append(delimiter);
        }
        return sb.toString().trim();
    }

    public static String titleCase(String str) {
        return titleCase(str, " ");
    }
}
