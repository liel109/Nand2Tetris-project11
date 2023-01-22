import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class JackTokenizer{

    File f;
    BufferedReader reader;
    public String currentToken;
    String currentLine;
    Type currentType;
    boolean multilineComment;

    public JackTokenizer(File source) throws IOException{
        this.f = source;
        this.reader = new BufferedReader(new FileReader(source));
        this.currentToken = "";
        this.currentLine = "";
        this.multilineComment = false;
    }

    public boolean hasMoreTokens() throws IOException{
        return (reader.ready() || !currentLine.equals(""));
    }
    
    void getNextLine() throws IOException{
        currentLine = reader.readLine();
        if(currentLine.indexOf("*/") != -1){
            currentLine = currentLine.substring(currentLine.indexOf("*/") + 2);
            multilineComment = false;
        }
        else if(multilineComment){
            currentLine = "";
        }
        else if(currentLine.indexOf("//") != -1){
            currentLine = currentLine.substring(0, currentLine.indexOf("//"));
        }
        else if(currentLine.indexOf("/**") != -1){
            currentLine = currentLine.substring(0, currentLine.indexOf("/**"));
            multilineComment = true;
        }
        currentLine = currentLine.trim();
    }

    public void advance() throws IOException {
        while(currentLine.length() == 0){
            getNextLine();
        }

        int counter = 1;
        StringBuilder token = new StringBuilder();
        char c = currentLine.charAt(0);
        char terminate = ' ';

        if(TypesMap.contains(c+"")){
            currentToken = c + "";
            currentType = Type.SYMBOL;
            currentLine = currentLine.substring(counter);
            currentLine = currentLine.trim();
            return;
        }
        else if(c == '"'){
            terminate = '"';
            c= currentLine.charAt(counter++);
        }
    
        while(c != terminate && counter < currentLine.length() && ! TypesMap.contains(c + "")) {
            token.append(c);
            c = currentLine.charAt(counter++);
        }

        currentToken = token.toString();
        
        if(terminate == '"'){
            currentType = Type.STRING_CONST;
            currentLine = currentLine.substring(counter).trim();
        }
        else{
            char first = token.charAt(0);
            if(first >= 48 && first <= 57){
                currentType = Type.INT_CONST;
            }
            else if(TypesMap.contains(currentToken)){
                currentType = Type.KEYWORD;
            }
            else currentType = Type.IDENTIFIER;
            
            currentLine = currentLine.substring(counter-1).trim();
        }
    }

    public Type tokenType(){
        return currentType;
    }

    public String keyword(){
        return currentToken;
    }
    
    public char symbol(){
        return currentToken.charAt(0);
    }

    public String identifier(){
        return currentToken;
    }

    public int intVal(){
        return Integer.parseInt(currentToken);
    }

    public String stringVal(){
        return currentToken;
    }

    public void close() throws IOException{
        reader.close();
    }
}