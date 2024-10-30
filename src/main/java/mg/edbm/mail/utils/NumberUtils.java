package mg.edbm.mail.utils;

public class NumberUtils {
    public static Long octetToMo(Long octet) {
        return octet / (1024 * 1024);
    }

    public static String octetToHumanReadable(Long octet) {
        if (octet < 1024)
            return octet + " o";
        else if (octet < 1024 * 1024)
            return octet / 1024 + " Ko";
        else if (octet < 1024 * 1024 * 1024)
            return octet / (1024 * 1024) + " Mo";
        return octet / (1024 * 1024 * 1024) + " Go";
    }
}
