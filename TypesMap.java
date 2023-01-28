import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * A class that holds all the types, operations and statements used in the Jack
 * language.
 * 
 */
public class TypesMap {
    public static Map<String, String> types = new HashMap<String, String>();
    public static Map<String, String> xmlOps = new HashMap<String, String>();

    public static List<String> statements = Arrays.asList(new String[] { "let", "if", "while", "do", "return" });
    static String s[] = { "+", "-", "*", "/", "&", "|", "<", ">", "=" };
    public static List<String> ops = Arrays.asList(s);

    /**
     * Initializes the types Map with all keywords and symbols used in the Jack
     * language.
     */
    public static void init() {
        // keywords
        types.put("class", "keyword");
        types.put("constructor", "keyword");
        types.put("function", "keyword");
        types.put("method", "keyword");
        types.put("field", "keyword");
        types.put("static", "keyword");
        types.put("var", "keyword");
        types.put("int", "keyword");
        types.put("char", "keyword");
        types.put("boolean", "keyword");
        types.put("void", "keyword");
        types.put("true", "keyword");
        types.put("false", "keyword");
        types.put("null", "keyword");
        types.put("this", "keyword");
        types.put("let", "keyword");
        types.put("do", "keyword");
        types.put("if", "keyword");
        types.put("else", "keyword");
        types.put("while", "keyword");
        types.put("return", "keyword");

        // symbols
        types.put("{", "symbol");
        types.put("}", "symbol");
        types.put("(", "symbol");
        types.put(")", "symbol");
        types.put("[", "symbol");
        types.put("]", "symbol");
        types.put(".", "symbol");
        types.put(",", "symbol");
        types.put(";", "symbol");
        types.put("+", "symbol");
        types.put("-", "symbol");
        types.put("*", "symbol");
        types.put("/", "symbol");
        types.put("&", "symbol");
        types.put("|", "symbol");
        types.put("<", "symbol");
        types.put(">", "symbol");
        types.put("=", "symbol");
        types.put("~", "symbol");
        // xml ops
        xmlOps.put("<", "&lt;");
        xmlOps.put(">", "&gt;");
        xmlOps.put("\"", "&quot;");
        xmlOps.put("&", "&amp;");
    }

    /**
     * Returns the type of the given key.
     * 
     * @param key the keyword or symbol to check
     * @return the type of the given key
     */
    public static String get(String key) {
        return types.get(key);
    }

    /**
     * Determines if the given key is present in the types map.
     * 
     * @param key The key to check for in the map.
     * @return True if the key is present in the map, false otherwise.
     */
    public static boolean contains(String key) {
        return types.containsKey(key);
    }

    /**
     * returns a boolean indicating whether a given character is present in the list
     * of operators.
     * 
     * @param key the character to check for in the list of operators.
     * @return a boolean indicating whether the character is present in the list of
     *         operators.
     */
    public static boolean containsOperation(char key) {
        return ops.contains(key + "");
    }

    /**
     * returns a boolean indicating whether a given character is present in the list
     * of xml operations.
     * 
     * @param key the character to check for in the list of xml operations.
     * @return a boolean indicating whether the character is present in the list of
     *         xml operations.
     */
    public static boolean containsXmlOp(char key) {
        return xmlOps.containsKey(key + "");
    }

    /**
     * returns the corresponding XML operation for a given character.
     * 
     * @param key the character to get the corresponding XML operation for.
     * @return the corresponding XML operation for the given character.
     */
    public static String getXmlOp(char key) {
        return xmlOps.get(key + "");
    }

    /**
     * returns a boolean indicating whether a given string is a statement.
     * 
     * @param statement the string to check for being a statement.
     * @return a boolean indicating whether the given string is a statement.
     */
    public static boolean isStatement(String statement) {
        return statements.contains(statement);
    }
}
