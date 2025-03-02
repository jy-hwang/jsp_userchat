package util;

public class StringUtils {

  public static boolean isEmpty(String param) {

    return param == null || param.trim().isEmpty() || param.equalsIgnoreCase("null");

  }
}
