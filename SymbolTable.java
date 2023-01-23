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

    public SymbolTable() {
        this.symbols = new HashMap<String, SymbolEntry>();
        this.fieldCount = 0;
        this.staticCount = 0;
        this.argCount = 0;
        this.localCount = 0;
    }

    public void define(String name, String type, String kind) {
        SymbolEntry entry = new SymbolEntry(type, kind, varCount(kind));
        symbols.put(name, entry);
    }

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

    public String kindOf(String name) {
        return (symbols.containsKey(name)) ? symbols.get(name).kind : "NONE";
    }

    public String typeOf(String name) {
        return symbols.get(name).type;
    }

    public int indexOf(String name) {
        return symbols.get(name).index;
    }

    public boolean contains(String name) {
        return symbols.containsKey(name);
    }

    public void reset() {
        symbols.clear();
        fieldCount = 0;
        staticCount = 0;
        argCount = 0;
        localCount = 0;
    }

}
