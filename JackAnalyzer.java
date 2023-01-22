import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JackAnalyzer {
    public static void main(String[] args) {
        try{
            TypesMap.init();
            File source = new File(args[0]);
            File[] filesArray;
            if(source.isDirectory()){
                filesArray = source.listFiles();
            }
            else {
                filesArray = new File[1];
                filesArray[0] = source;
            }

            for(File f : filesArray){
                if(!f.getName().endsWith(".jack")) continue;
                String fileName = f.getAbsolutePath();
                File output = new File(fileName.substring(0,fileName.length()-4)+"xml");
                CompilationEngine cEng = new CompilationEngine(f,output);
                cEng.compileClass();
                cEng.close();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void testGetNextLine() throws IOException{
    }

    public static void testTokenizer() throws IOException{
        File source = new File("tokTest.txt");
        File output = new File("Tokentest.xml");
        BufferedWriter writer = new BufferedWriter(new FileWriter(output));
        JackTokenizer tokenizer = new JackTokenizer(source);
        writer.write("<tokens>\n");
        while(tokenizer.hasMoreTokens()){
            tokenizer.advance();
            String type = "";
            switch (tokenizer.tokenType()){
                case KEYWORD:
                    type = "keyword";
                    break;
                case SYMBOL:
                    type = "symbol";
                    break;
                case IDENTIFIER:
                    type = "identifier";
                    break;
                case INT_CONST:
                    type = "integerConstant";
                    break;
                case STRING_CONST:
                    type = "stringConstant";
                    break;
            }
            String content = tokenizer.currentToken;
            if(TypesMap.containsXmlOp(tokenizer.symbol())){
                content = TypesMap.getXmlOp(tokenizer.symbol());
            }
            writer.write("<" + type + "> " + content + " </" + type + ">\n");
        }
        writer.write("</tokens>");
        writer.close();
    }
}
