import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class JackTokenizer {

    File f;
    BufferedReader reader;
    public String currentToken;
    String currentLine;
    Type currentType;
    boolean multilineComment, isString;

    /**
     * Constructs a new JackTokenizer that will tokenize the given source file
     * 
     * @param source the file to tokenize
     * @throws IOException if an error occurs while creating the BufferedReader
     */
    public JackTokenizer(File source) throws IOException {
        this.f = source;
        this.reader = new BufferedReader(new FileReader(source));
        this.currentToken = "";
        this.currentLine = "";
        this.multilineComment = false;
    }

    /**
     * Returns true if there are more tokens in the source file
     * 
     * @return true if there are more tokens, false otherwise
     * @throws IOException if an error occurs while reading from the source file
     */
    public boolean hasMoreTokens() throws IOException {
        return (reader.ready() || !currentLine.equals(""));
    }

    /**
     * Gets the next line from the source file, and processes it to remove comments
     * and whitespaces
     * 
     * @throws IOException if an error occurs while reading from the source file
     */
    void getNextLine() throws IOException {
        currentLine = reader.readLine();
        if (currentLine.indexOf("*/") != -1) {
            currentLine = currentLine.substring(currentLine.indexOf("*/") + 2);
            multilineComment = false;
        } else if (multilineComment) {
            currentLine = "";
        } else if (currentLine.indexOf("//") != -1) {
            currentLine = currentLine.substring(0, currentLine.indexOf("//"));
        } else if (currentLine.indexOf("/**") != -1) {
            currentLine = currentLine.substring(0, currentLine.indexOf("/**"));
            multilineComment = true;
        }
        currentLine = currentLine.trim();
    }

    /**
     * Advances the tokenizer to the next token in the source file
     * 
     * @throws IOException if an error occurs while reading from the source file
     */
    public void advance() throws IOException {
        isString = false;
        while (currentLine.length() == 0) {
            getNextLine();
        }

        int counter = 1;
        StringBuilder token = new StringBuilder();
        char c = currentLine.charAt(0);
        char terminate = ' ';

        if (TypesMap.contains(c + "")) {
            currentToken = c + "";
            currentType = Type.SYMBOL;
            currentLine = currentLine.substring(counter);
            currentLine = currentLine.trim();
            return;
        } else if (c == '"') {
            isString = true;
            terminate = '"';
            c = currentLine.charAt(counter++);
        }

        while (c != terminate && counter < currentLine.length()) {
            if (terminate == ' ' && TypesMap.contains(c + ""))
                break;
            token.append(c);
            c = currentLine.charAt(counter++);
        }

        currentToken = token.toString();

        if (terminate == '"') {
            currentType = Type.STRING_CONST;
            currentLine = currentLine.substring(counter).trim();
        } else {
            char first = token.charAt(0);
            if (first >= 48 && first <= 57) {
                currentType = Type.INT_CONST;
            } else if (TypesMap.contains(currentToken)) {
                currentType = Type.KEYWORD;
            } else
                currentType = Type.IDENTIFIER;

            currentLine = currentLine.substring(counter - 1).trim();
        }
    }

    /**
     * Returns the type of the current token
     * 
     * @return the type of the current token
     */
    public Type tokenType() {
        return currentType;
    }

    /**
     * Returns the keyword of the current token
     * 
     * @return the keyword of the current token
     */
    public String keyword() {
        return currentToken;
    }

    /**
     * Returns the symbol of the current token
     * 
     * @return the symbol of the current token
     */
    public char symbol() {
        return currentToken.charAt(0);
    }

    /**
     * Returns the identifier of the current token
     * 
     * @return the identifier of the current token
     */
    public String identifier() {
        return currentToken;
    }

    /**
     * Returns the integer value of the current token
     * 
     * @return the integer value of the current token
     */
    public int intVal() {
        return Integer.parseInt(currentToken);
    }

    /**
     * Returns the string value of the current token
     * 
     * @return the string value of the current token
     */
    public String stringVal() {
        return currentToken;
    }

    /**
     * Closes the tokenizer's source file
     * 
     * @throws IOException if an error occurs while closing the file
     */
    public void close() throws IOException {
        reader.close();
    }

    /**
     * Returns whether the current token is a string
     * 
     * @return whether the current token is a string
     */
    public boolean isString() {
        return isString;
    }
}