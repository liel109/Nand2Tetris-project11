import java.io.File;

/**
 * Given a jack file or directory with jack files, compiles it to vm file or
 * directory of vm files
 *
 */
public class JackCompiler {
    public static void main(String[] args) {
        try {
            TypesMap.init();
            File source = new File(args[0]);
            File[] filesArray;
            if (source.isDirectory()) {
                filesArray = source.listFiles();
            } else {
                filesArray = new File[1];
                filesArray[0] = source;
            }

            for (File f : filesArray) {
                if (!f.getName().endsWith(".jack"))
                    continue;
                String fileName = f.getAbsolutePath();
                File output = new File(fileName.substring(0, fileName.length() - 4) + "vm");
                CompilationEngine cEng = new CompilationEngine(f, output);
                cEng.compileClass();
                cEng.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
