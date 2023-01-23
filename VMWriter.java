import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class VMWriter {

    BufferedWriter writer;

    /**
     * Constructs a new VMWriter that will write to the given output file
     * 
     * @param output the file to write to
     * @throws IOException if an error occurs while creating the BufferedWriter
     */
    public VMWriter(File output) throws IOException {
        this.writer = new BufferedWriter(new FileWriter(output));
    }

    /**
     * Writes a VM push command to the output file
     * 
     * @param segment the memory segment to push from
     * @param index   the index in the segment to push
     * @throws IOException if an error occurs while writing to the output file
     */
    public void writePush(String segment, int index) throws IOException {
        writer.write("push " + segment + " " + index + "\n");
    }

    /**
     * Writes a VM pop command to the output file
     * 
     * @param segment the memory segment to pop to
     * @param index   the index in the segment to pop to
     * @throws IOException if an error occurs while writing to the output file
     */
    public void writePop(String segment, int index) throws IOException {
        writer.write("pop " + segment + " " + index + "\n");
    }

    /**
     * Writes a VM arithmetic command to the output file
     * 
     * @param command the arithmetic command to write
     * @throws IOException if an error occurs while writing to the output file
     */
    public void writeArithmetic(String command) throws IOException {
        writer.write(command + "\n");
    }

    /**
     * Writes a VM label command to the output file
     * 
     * @param label the label to write
     * @throws IOException if an error occurs while writing to the output file
     */
    public void writeLabel(String label) throws IOException {
        writer.write("label " + label + "\n");
    }

    /**
     * Writes a VM goto command to the output file
     * 
     * @param label the label to go to
     * @throws IOException if an error occurs while writing to the output file
     */
    public void writeGoTo(String label) throws IOException {
        writer.write("goto " + label + "\n");
    }

    /**
     * Writes a VM if-goto command to the output file
     * 
     * @param label the label to go to if the top of the stack is not 0
     * @throws IOException if an error occurs while writing to the output file
     */
    public void writeIf(String label) throws IOException {
        writer.write("if-goto " + label + "\n");
    }

    /**
     * Writes a VM call command to the output file
     * 
     * @param name  the name of the function to call
     * @param nArgs the number of arguments passed to the function
     * @throws IOException if an error occurs while writing to the output file
     */
    public void writeCall(String name, int nArgs) throws IOException {
        writer.write("call " + name + " " + nArgs + "\n");
    }

    /**
     * Writes a VM function command to the output file
     * 
     * @param name  the name of the function
     * @param nVars the number of local variables in the function
     * @throws IOException if an error occurs while writing to the output file
     */
    public void writeFunction(String name, int nVars) throws IOException {
        writer.write("function " + name + " " + nVars + "\n");
    }

    /**
     * Writes a VM return command to the output file
     * 
     * @throws IOException if an error occurs while writing to the output file
     */
    public void writeReturn() throws IOException {
        writer.write("return\n");
    }

    /**
     * Closes the BufferedWriter object
     * 
     * @throws IOException if an error occurs while closing the BufferedWriter
     */
    public void close() throws IOException {
        writer.close();
    }

}
