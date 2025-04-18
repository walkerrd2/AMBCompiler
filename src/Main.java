import AMBTokenPKG.AMBTokens;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // read in file and produce token list
        // take in Token List and produce Parse Tree
        // Take in Parse Tree and produce Python code
        ArrayList<AMBTokens> tokens = AMBTokenizer.tokenize("test.amb");
        AMBParseTree.generatePT(tokens);
//        String pyCode = AMBParseTree.root.genPython();
//        System.out.println(pyCode);
//
//        try (PrintWriter print = new PrintWriter(new BufferedWriter(new FileWriter(Paths.get())))){
//            print.println(pyCode);
//        } catch (IOException e){
//            System.err.println("Error with file: "+e.getMessage());
//        }
    }
}