import AMBTokenPKG.AMBTokens;
import Exceptions.ExpectedSymbol;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // read in file and produce token list
        // take in Token List and produce Parse Tree
        // Take in Parse Tree and produce Python code
        try {
            ArrayList<AMBTokens> tokens = AMBTokenizer.tokenize("test.amb");
            AMBParseTree.generatePT(tokens);

            GeneratePython gp = new GeneratePython(AMBParseTree.root);
            String pyCode = gp.generateCode();
            System.out.println(pyCode);

            try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(Paths.get("output.py").toFile())))){
                pw.println(pyCode);
                System.out.println("Successfully wrote Python code to output.py");
            } catch (IOException e){
                System.err.println("Error writing to file: " + e.getMessage());
            }
        } catch (ExpectedSymbol e){
            System.err.println("Syntax error in AMB code: " + e.getMessage());
            System.err.println("Unable to generate Python code due to parsing errors.");
        } catch (Exception e) {
            System.err.println("Unexpected error generating Python code: " + e.getMessage());
            e.printStackTrace();
        }

    }
}