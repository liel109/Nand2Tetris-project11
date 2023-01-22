import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TypesMap {
    public static Map<String,String> types = new HashMap<String,String>();
    public static Map<String, String> xmlOps =new HashMap<String,String>();

    public static List<String> statements = Arrays.asList(new String[]{"let","if","while","do","return"}); 
    public static List<String> ops = Arrays.asList(new String[]{"+","-","*","/","&","|","<",">","="}); 
    public static void init(){
        //keywords
        types.put("class", "keyword"); types.put("constructor", "keyword"); types.put("function", "keyword"); types.put("method", "keyword");
        types.put("field", "keyword"); types.put("static", "keyword"); types.put("var", "keyword"); types.put("int", "keyword");
        types.put("char", "keyword"); types.put("boolean", "keyword"); types.put("void", "keyword"); types.put("true", "keyword"); types.put("false", "keyword");
        types.put("null", "keyword"); types.put("this", "keyword"); types.put("let", "keyword"); types.put("do", "keyword"); types.put("if", "keyword");
        types.put("else", "keyword"); types.put("while", "keyword"); types.put("return", "keyword");
        
        //symbols
        types.put("{", "symbol"); types.put("}", "symbol"); types.put("(", "symbol"); types.put(")", "symbol"); types.put("[", "symbol"); types.put("]", "symbol");
        types.put(".", "symbol"); types.put(",", "symbol"); types.put(";", "symbol"); types.put("+", "symbol"); types.put("-", "symbol");
        types.put("*", "symbol"); types.put("/", "symbol"); types.put("&", "symbol"); types.put("|", "symbol"); types.put("<", "symbol");
        types.put(">", "symbol"); types.put("=", "symbol"); types.put("~", "symbol");

        xmlOps.put("<", "&lt;"); xmlOps.put(">", "&gt;"); xmlOps.put("\"", "&quot;"); xmlOps.put("&", "&amp;");
    }
    public static String get(String key){
        return types.get(key);
    }

    public static boolean contains(String key){
        return types.containsKey(key);
    }

    public static boolean containsOperation(char key){
        return ops.contains(key+"");
    }

    public static boolean containsXmlOp(char key){
        return xmlOps.containsKey(key+"");
    }

    public static String getXmlOp(char key){
        return xmlOps.get(key+"");
    }

    public static boolean isStatement(String statement){
        return statements.contains(statement);
    }
}
