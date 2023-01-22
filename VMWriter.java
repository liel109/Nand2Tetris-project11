import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class VMWriter {
    
    BufferedWriter writer;

    public VMWriter(File output) throws IOException {
        this.writer = new BufferedWriter(new FileWriter(output));
    }

    public void writePush(String segment, int index) throws IOException {
        writer.write("push " + segment + " " + index+"\n");
    }

    public void writePop(String segment, int index) throws IOException {
        writer.write("pop " + segment + " " + index+"\n");
    }

    public void writeArithmetic(String command) throws IOException{
        writer.write(command + "\n");
    }

    public void writeLabel(String label) throws IOException{
        writer.write("lable " + label + "\n");
    }

    public void writeGoTo(String label) throws IOException{
        writer.write("goto " + label + "\n");
    }

    public void writeIf(String label) throws IOException{
        writer.write("if-goto " + label + "\n");
    }

    public void writeCall(String name, int nArgs) throws IOException{
        writer.write("call " + name + " " + nArgs + "\n");
    }

    public void writeFunction(String name, int nVars) throws IOException{
        writer.write("function " + name + " " + nVars + "\n");
    }

    public void writeReturn() throws IOException{
        writer.write("return\n");
    }

    public void close() throws IOException{
        writer.close();
    }

}
