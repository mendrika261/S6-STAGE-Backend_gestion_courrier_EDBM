package mg.edbm.mail.utils;

public class NumberUtils {
    public static Long octetToMo(Long octet) {
        return octet / (1024 * 1024);
    }
}
