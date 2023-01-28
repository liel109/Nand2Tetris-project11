/**
 * A class consisting operations on Strings.
 * 
 */
public abstract class StringOps {

    /**
     * Returns a String with its leading and trailing white spaces removed.
     * 
     * @param String s The string to repeat
     * @param num    The number of times to repeat the string
     * @return A string with num times the string s
     */
    public static String repeat(String s, int num) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < num; i++) {
            result.append(s);
        }
        return result.toString();
    }

    /**
     * Returns a String with its leading and trailing white spaces removed.
     * 
     * @param String
     * @return A string which its leading and trailing white space removed.
     */
    public static String strip(String s) {
        s = stripLeading(s);
        return stripTrailing(s);
    }

    /**
     * Returns a String with its leading white spaces removed.
     * 
     * @param String
     * @return A string which its leading white space removed.
     */
    public static String stripLeading(String s) {
        StringBuilder result = new StringBuilder();
        boolean hasStarted = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c != ' ') {
                hasStarted = true;
            }
            if (hasStarted) {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * Returns a String with its leading white spaces removed.
     * 
     * @param String
     * @return A string which its trailing white space removed.
     */
    public static String stripTrailing(String s) {
        StringBuilder result = new StringBuilder();
        boolean hasStarted = false;
        for (int i = s.length() - 1; i >= 0; i--) {
            char c = s.charAt(i);
            if (c != ' ') {
                hasStarted = true;
            }
            if (hasStarted) {
                result.append(c);
            }
        }
        result.reverse();
        return result.toString();
    }

    /**
     * Determines if a given string can be parsed as an integer.
     *
     * @param s the string to be checked.
     * @return true if the string can be parsed as an integer, false otherwise.
     */
    public static boolean isNum(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
