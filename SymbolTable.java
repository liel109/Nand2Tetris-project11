import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

    Map<String, SymbolEntry> symbols;
    int fieldCount;
    int staticCount;
    int argCount;
    int localCount;

    /**
     * Anonymous Class SymbolEntry
     * Represents a single entry in the symbol table by it's type, kind, and index.
     */
    class SymbolEntry {
        String type;
        String kind;
        int index;

        /**
         * Constructor Method
         * Creates a new instance of a SymbolEntry
         * 
         * @param type  - the type of the corresponding identifier
         * @param kind  - the kind of the corresponding identifier
         * @param index - the index of the corresponding identifier as determined by the
         *              SymbolTable
         */
        SymbolEntry(String type, String kind, int index) {
            this.type = type;
            this.kind = kind;
            this.index = index;
        }
    }

    /**
     * Constructor a new SymbolTable
     * 
     */
    public SymbolTable() {
        this.symbols = new HashMap<String, SymbolEntry>();
        this.fieldCount = 0;
        this.staticCount = 0;
        this.argCount = 0;
        this.localCount = 0;
    }

    /**
     * puts new object in to the SymbolTable.
     * the object consist of key:name, value:SymbolEntry
     * 
     * @param name
     * @param type
     * @param kind
     */
    public void define(String name, String type, String kind) {
        SymbolEntry entry = new SymbolEntry(type, kind, varCount(kind));
        symbols.put(name, entry);
    }

    /**
     * returns the number of variables of the given kind already defined
     * in the Symboltable and increamenting the number
     * 
     * @param kind the kind of variable (STATIC, FIELD, ARG, LOCAL)
     * @return the number of variables of the given kind already defined
     */
    public int varCount(String kind) {
        switch (kind) {
            case "STATIC":
                return staticCount++;
            case "FIELD":
                return fieldCount++;
            case "ARG":
                return argCount++;
            default:
                return localCount++;
        }
    }

    /**
     * returns the kind of the named identifier in the Symboltable
     * 
     * @param name the name of the object
     * @return the kind of the object if it is defined, NONE otherwise
     */
    public String kindOf(String name) {
        return (symbols.containsKey(name)) ? symbols.get(name).kind : "NONE";
    }

    /**
     * returns the type of the named identifier in the Symboltable
     * 
     * @param name the name of the object
     * @return the type of the object
     */
    public String typeOf(String name) {
        return symbols.get(name).type;
    }

    /**
     * returns the index assigned to the named identifier
     * 
     * @param name the name of the object
     * @return the index of the object
     */
    public int indexOf(String name) {
        return symbols.get(name).index;
    }

    /**
     * returns true if the named identifier is in the Symboltable
     * 
     * @param name the name of the object
     * @return true if the object is in the Symboltable, false otherwise
     */
    public boolean contains(String name) {
        return symbols.containsKey(name);
    }

    /**
     * resets the Symboltable
     */
    public void reset() {
        symbols.clear();
        fieldCount = 0;
        staticCount = 0;
        argCount = 0;
        localCount = 0;
    }

}
