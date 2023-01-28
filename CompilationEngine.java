import java.io.File;
import java.io.IOException;

/**
 * Compiles a complete class (from Jack to VM)
 * 
 * @throws IOException
 */
public class CompilationEngine {

    JackTokenizer tokenizer;
    SymbolTable classTable, subroutineTable;
    VMWriter vmWriter;
    String className, funcName;
    boolean isVarUsed, isVoid;
    int labelCount;

    /**
     * Constructs a new compilation engine that will compile the given source file
     * to the given output file
     * 
     * @param input  the file to tokenize
     * @param output the file to write the output to
     * @throws IOException if an error occurs while creating the BufferedReader
     */
    public CompilationEngine(File input, File output) throws IOException {
        this.vmWriter = new VMWriter(output);
        this.tokenizer = new JackTokenizer(input);
        this.classTable = new SymbolTable();
        this.subroutineTable = new SymbolTable();
        this.isVarUsed = false;
        this.className = "";
        this.funcName = "";
        this.labelCount = 0;
    }

    /**
     * Compiles a complete class
     * 
     * @throws IOException
     */
    public void compileClass() throws IOException {

        // write 'class'
        tokenizer.advance();

        // className
        tokenizer.advance();
        className = tokenizer.identifier();

        // {
        tokenizer.advance();

        // check if varDec exists
        tokenizer.advance();
        while (tokenizer.tokenType() == Type.KEYWORD
                && (tokenizer.keyword().equals("static") || tokenizer.keyword().equals("field"))) {
            compileClassVarDec();
            tokenizer.advance();
        }

        // check if subroutineDec exists
        while (tokenizer.tokenType() == Type.KEYWORD
                && (tokenizer.keyword().equals("function") || tokenizer.keyword().equals("constructor")
                        || tokenizer.keyword().equals("method"))) {
            compileSubroutine();
            tokenizer.advance();
        }

        // skip '}'
    }

    /**
     * compile the class variable declaration (field|static)
     * 
     * @throws IOException
     */
    public void compileClassVarDec() throws IOException {
        String name, type, kind;

        // write static|field
        kind = tokenizer.keyword().toUpperCase();

        // type
        tokenizer.advance();
        type = tokenizer.keyword();

        // varName
        tokenizer.advance();
        name = tokenizer.identifier();
        classTable.define(name, type, kind);

        // check if more variables exist
        tokenizer.advance();
        while (tokenizer.symbol() != ';') {
            // skip ','

            // write varName
            tokenizer.advance();
            name = tokenizer.identifier();
            classTable.define(name, type, kind);

            // get next token
            tokenizer.advance();
        }

        // current token - ';'

    }

    /**
     * compile the subroutine declaration (constructor|function|method)
     * 
     * @throws IOException
     */
    public void compileSubroutine() throws IOException {
        subroutineTable.reset();
        isVarUsed = false;

        // skip constructor | function | method
        if (tokenizer.keyword().equals("constructor")) {
            // allocate memory for constructor
            vmWriter.writePush("constant", classTable.varCount("FIELD"));
            vmWriter.writeCall("Memory.alloc", 1);
            vmWriter.writePop("pointer", 0);
            // method
        } else if (tokenizer.keyword().equals("method")) {
            // add "this" to symbol table
            subroutineTable.define("this", className, "ARG");

            // set THIS on RAM to the given object
            vmWriter.writePush("argument", 0);
            vmWriter.writePop("pointer", 0);
        }
        // skip void | type
        tokenizer.advance();

        // subroutineName
        tokenizer.advance();
        funcName = tokenizer.identifier();

        // skip '('
        tokenizer.advance();

        // parameterList
        tokenizer.advance();
        compileParameterList();

        // skip ')'

        // subroutineBody
        compileSubroutineBody();

    }

    /**
     * compile list of parameters
     * 
     * @throws IOException
     */
    public void compileParameterList() throws IOException {

        // check if parmeter exists
        if (tokenizer.tokenType() != Type.SYMBOL) {
            String type, name, kind = "ARG";

            // write parameter type
            type = tokenizer.keyword();

            // varName
            tokenizer.advance();
            name = tokenizer.keyword();
            subroutineTable.define(name, type, kind);

            // check if more parameters exist
            tokenizer.advance();
            while (tokenizer.tokenType() != Type.SYMBOL || tokenizer.symbol() != ')') {
                // skip ','

                // write type
                tokenizer.advance();
                type = tokenizer.keyword();

                // write varName
                tokenizer.advance();
                name = tokenizer.identifier();
                subroutineTable.define(name, type, kind);

                // get next token
                tokenizer.advance();
            }
        }
    }

    /**
     * compile a subroutine body
     * 
     * @throws IOException
     */
    public void compileSubroutineBody() throws IOException {

        // skip '{'
        tokenizer.advance();

        // check if declarations of variables exist
        tokenizer.advance();
        while (tokenizer.tokenType() == Type.KEYWORD && tokenizer.keyword().equals("var")) {
            compileVarDec();
            tokenizer.advance();
        }
        isVarUsed = true;

        vmWriter.writeFunction(className + "." + funcName, subroutineTable.varCount("LOCAL"));
        compileStatements();

        // skip '}'

    }

    /**
     * compile the variable declaration of a subroutine
     * 
     * @throws IOException
     */
    public void compileVarDec() throws IOException {
        String name, type, kind = "VAR";

        // skip var

        // write type
        tokenizer.advance();
        type = tokenizer.identifier();

        // write varName
        tokenizer.advance();
        name = tokenizer.identifier();
        subroutineTable.define(name, type, kind);

        // check if more variables exist
        tokenizer.advance();
        while (tokenizer.symbol() != ';') {
            // skip ','

            // write varName
            tokenizer.advance();
            name = tokenizer.identifier();
            subroutineTable.define(name, type, kind);
            tokenizer.advance();

        }

        // skip ';'

    }

    /**
     * compile a statement (let|if|while|do|return)
     * 
     * @throws IOException
     */
    public void compileStatements() throws IOException {

        while (tokenizer.tokenType() == Type.KEYWORD && TypesMap.isStatement(tokenizer.keyword())) {
            switch (tokenizer.keyword()) {
                case "let":
                    compileLet();
                    break;
                case "if":
                    compileIf();
                    break;
                case "while":
                    compileWhile();
                    break;
                case "do":
                    compileDo();
                    break;
                case "return":
                    compileReturn();
                    break;
            }
        }

    }

    /**
     * compile a let statement
     * 
     * @throws IOException
     */
    public void compileLet() throws IOException {
        String segment = "";
        int index = 0;
        boolean isArray = false;

        // skip "let"
        tokenizer.advance();

        // store VarName
        if (subroutineTable.contains(tokenizer.identifier())) {
            segment = getSubroutineVarSegment(tokenizer.identifier());
            index = subroutineTable.indexOf(tokenizer.identifier());
        } else if (classTable.contains(tokenizer.identifier())) {
            segment = getClassVarSegment(tokenizer.identifier());
            index = classTable.indexOf(tokenizer.identifier());
        }

        // check if '[' exists
        tokenizer.advance();
        if (tokenizer.tokenType() == Type.SYMBOL && tokenizer.symbol() == '[') {
            isArray = true;
            // push base address of array
            vmWriter.writePush(segment, index);

            // skip '['

            // compute expression
            tokenizer.advance();
            compileExpression();

            // skip ']'

            // get entry adress of array
            vmWriter.writeArithmetic("add");

            // get next token (=)
            tokenizer.advance();
        }

        // skip '='
        tokenizer.advance();

        // compute expression
        compileExpression();

        // skip ';'

        // advance to next token
        tokenizer.advance();

        if (isArray) {
            vmWriter.writePop("temp", 0);
            vmWriter.writePop("pointer", 1);
            vmWriter.writePush("temp", 0);
            vmWriter.writePop("that", 0);
        } else {
            vmWriter.writePop(segment, index);
        }
    }

    /**
     * compile an if statement
     * 
     * @throws IOException
     */
    public void compileIf() throws IOException {
        // store the current label count and advance it by 2 (for the case of else
        // statment)
        int currentLabel = labelCount;
        labelCount += 2;

        // skip if

        // skip '('
        tokenizer.advance();

        // compute expression
        tokenizer.advance();
        compileExpression();

        // negate expression
        vmWriter.writeArithmetic("not");

        // if-goto
        vmWriter.writeIf("L" + currentLabel);

        // skip ')'

        // skip '{'
        tokenizer.advance();

        // write statements
        tokenizer.advance();
        compileStatements();

        // skip '}'

        // check if else exists
        tokenizer.advance();
        if (tokenizer.tokenType() == Type.KEYWORD && tokenizer.keyword().equals("else")) {
            vmWriter.writeGoTo("L" + (currentLabel + 1));
            vmWriter.writeLabel("L" + currentLabel++);
            // skip else

            // skip '{'
            tokenizer.advance();

            // write statements
            tokenizer.advance();
            compileStatements();

            // skip '}'

            // read next token
            tokenizer.advance();
        }
        vmWriter.writeLabel("L" + currentLabel);
    }

    /**
     * compile a while statement
     * 
     * @throws IOException
     */
    public void compileWhile() throws IOException {
        // store current label count and advance it by 2
        int currentLabel = labelCount;
        labelCount += 2;

        // write label
        vmWriter.writeLabel("L" + currentLabel);

        // skip while

        // skip '('
        tokenizer.advance();

        // write expression
        tokenizer.advance();
        compileExpression();

        // negate expression
        vmWriter.writeArithmetic("not");

        // if-goto
        vmWriter.writeIf("L" + (currentLabel + 1));

        // skip ')'

        // skip '{'
        tokenizer.advance();

        // write statements
        tokenizer.advance();
        compileStatements();

        // skip '}'

        // Go to start of the loop
        vmWriter.writeGoTo("L" + currentLabel++);

        // condition not fulfiled (end loop)
        vmWriter.writeLabel("L" + currentLabel);

        // get the next token
        tokenizer.advance();
    }

    /**
     * compile a do statement
     * 
     * @throws IOException
     */
    public void compileDo() throws IOException {
        String name = "";
        // skip do

        // write subroutine call
        // store subroutineName | className | varName
        tokenizer.advance();
        name = tokenizer.identifier();

        // read next '.' | '('
        tokenizer.advance();
        compileSubroutineCall(name);

        // skip ';'
        tokenizer.advance();

        // get the next token
        tokenizer.advance();

        // get rid of the returned value
        vmWriter.writePop("temp", 0);

    }

    /**
     * compile a return statement
     * 
     * @throws IOException
     */
    public void compileReturn() throws IOException {

        // skip return

        // check if expression exists
        tokenizer.advance();
        if (tokenizer.tokenType() != Type.SYMBOL || tokenizer.symbol() != ';') {
            // write expression
            compileExpression();
        } else {
            vmWriter.writePush("constant", 0);
        }

        // skip ';'

        // get next token
        tokenizer.advance();

        vmWriter.writeReturn();

    }

    /**
     * compile an expression
     * 
     * @throws IOException
     */
    public void compileExpression() throws IOException {
        char op = 0;

        // write term
        compileTerm();

        // check if operation exists
        while (tokenizer.tokenType() == Type.SYMBOL && TypesMap.containsOperation(tokenizer.symbol())) {
            // write operation
            op = tokenizer.symbol();

            // write term
            tokenizer.advance();
            compileTerm();

            switch (op) {
                case '+':
                    vmWriter.writeArithmetic("add");
                    break;
                case '-':
                    vmWriter.writeArithmetic("sub");
                    break;
                case '&':
                    vmWriter.writeArithmetic("and");
                    break;
                case '|':
                    vmWriter.writeArithmetic("or");
                    break;
                case '<':
                    vmWriter.writeArithmetic("lt");
                    break;
                case '>':
                    vmWriter.writeArithmetic("gt");
                    break;
                case '=':
                    vmWriter.writeArithmetic("eq");
                    break;
                case '*':
                    vmWriter.writeCall("Math.multiply", 2);
                    break;
                case '/':
                    vmWriter.writeCall("Math.divide", 2);
                    break;
            }
        }
    }

    /**
     * compile a term
     * 
     * @throws IOException
     */
    public void compileTerm() throws IOException {
        String segment = "";
        int index = 0;

        // write intConst | stringConst | keyConst | varName | subroutineName | '(' |
        // unaryOp
        if (tokenizer.tokenType() == Type.KEYWORD) {
            boolean neg = false;
            // case keyConst
            switch (tokenizer.keyword()) {
                case "true":
                    segment = "constant";
                    index = 1;
                    neg = true;
                    break;
                case "false":
                case "null":
                    segment = "constant";
                    index = 0;
                    break;
                case "this":
                    segment = "pointer";
                    index = 0;
                    break;
            }
            vmWriter.writePush(segment, index);
            if (neg)
                vmWriter.writeArithmetic("neg");

            // get next token
            tokenizer.advance();
        } else if (tokenizer.tokenType() == Type.SYMBOL) {
            // case '(' | unaryOp

            if (tokenizer.symbol() == '(') {
                // write expression
                tokenizer.advance();
                compileExpression();

                // skip ')'

                // get next token
                tokenizer.advance();
            } else {
                char symbol = tokenizer.symbol();

                // write term
                tokenizer.advance();
                compileTerm();

                if (symbol == '-') {
                    vmWriter.writeArithmetic("neg");
                } else
                    vmWriter.writeArithmetic("not");
            }
        } else {
            String term = tokenizer.identifier();
            if (StringOps.isNum(term)) {
                // case intConst
                vmWriter.writePush("constant", Integer.parseInt(term));

                // get next token
                tokenizer.advance();
            } else if (tokenizer.isString()) {
                // case stringConst
                vmWriter.writePush("constant", term.length());
                vmWriter.writeCall("String.new", 1);
                for (int i = 0; i < term.length(); i++) {
                    vmWriter.writePush("constant", term.charAt(i));
                    vmWriter.writeCall("String.appendChar", 2);
                }
                // get next token
                tokenizer.advance();

            } else {
                // check if term does not end (next token is '[' | '(' | '.' )
                tokenizer.advance();
                if (tokenizer.tokenType() == Type.SYMBOL) {
                    if (tokenizer.symbol() == '(' || tokenizer.symbol() == '.') {
                        // write subroutine call
                        compileSubroutineCall(term);

                        // get next token
                        tokenizer.advance();
                    } else {
                        // case varName
                        if (subroutineTable.contains(term)) {
                            segment = getSubroutineVarSegment(term);
                            index = subroutineTable.indexOf(term);
                        } else if (classTable.contains(term)) {
                            segment = getClassVarSegment(term);
                            index = classTable.indexOf(term);
                        }
                        vmWriter.writePush(segment, index);

                        if (tokenizer.symbol() == '[') {
                            // skip '['

                            // write expression
                            tokenizer.advance();
                            compileExpression();

                            // skip ']'

                            // get the array address in the ram
                            vmWriter.writeArithmetic("add");

                            // get the content in the address and store in temp 0
                            vmWriter.writePop("pointer", 1);
                            vmWriter.writePush("that", 0);

                            // get next token
                            tokenizer.advance();
                        }
                    }
                }
            }
        }
    }

    /**
     * compile a subroutine call
     * 
     * @param name the name of the object
     * @throws IOException
     */
    private void compileSubroutineCall(String name) throws IOException {

        String funcCall = "";
        int varCount = 0;

        // check if there is a '.'
        if (tokenizer.tokenType() == Type.SYMBOL && tokenizer.symbol() == '.') {
            // skip '.'

            // check if name is variable
            if (subroutineTable.contains(name)) {
                funcCall = subroutineTable.typeOf(name) + ".";
                varCount++;

                // push the current object
                vmWriter.writePush(getSubroutineVarSegment(name), subroutineTable.indexOf(name));
            } else if (classTable.contains(name)) {
                funcCall = classTable.typeOf(name) + ".";
                varCount++;

                // push the current object
                vmWriter.writePush(getClassVarSegment(name), classTable.indexOf(name));
            } else
                funcCall = name + ".";

            // read subroutineName
            tokenizer.advance();
            funcCall += tokenizer.identifier();

            // get next token
            tokenizer.advance();
        } else { // case mehod call inside class
            // skip '('

            funcCall = className + "." + name;
            varCount++;

            // push the current object
            vmWriter.writePush("pointer", 0);
        }

        // write expression list
        tokenizer.advance();
        varCount += compileExpressionList();

        // skip ')'

        vmWriter.writeCall(funcCall, varCount);
    }

    /**
     * compile a list of expressions
     * 
     * @return the number of expressions
     * @throws IOException
     */
    public int compileExpressionList() throws IOException {
        int counter = 0;

        // check if expression exists
        while (tokenizer.tokenType() != Type.SYMBOL || tokenizer.symbol() != ')') {
            if (tokenizer.tokenType() == Type.SYMBOL && tokenizer.symbol() == ',') {

                // skip ','

                // get next token
                tokenizer.advance();
            } else {
                counter++;
                // write expression
                compileExpression();
            }
        }
        return counter;
    }

    /**
     * reuturn the segment of the class object
     * 
     * @param name the name of the object
     * @return this if it's a class field and static otherwise
     */
    private String getClassVarSegment(String name) {
        if (classTable.kindOf(name) == "FIELD") {
            return "this";
        }
        return "static";
    }

    /**
     * reuturn the segment of the sabroutine object
     * 
     * @param name the name of the object
     * @return argument if it's a class field and local otherwise
     */
    private String getSubroutineVarSegment(String name) {
        if (subroutineTable.kindOf(name) == "ARG") {
            return "argument";
        }
        return "local";
    }

    /**
     * close the compilation engine
     * 
     * @throws IOException
     */
    public void close() throws IOException {
        vmWriter.close();
        tokenizer.close();
    }

}
