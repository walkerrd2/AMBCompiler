import AMBTokenPKG.*;
import AMBTokenPKG.KeywordsPKG.*;
import AMBTokenPKG.SymbolCollectionPKG.Numbers;
import AMBTokenPKG.SymbolsPKG.*;
import AMBTokenPKG.SymbolCollectionPKG.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class will tokenize all the regular expressions. First
 * it will read the file, then turn the file into a giant String,
 * then turn the String into an array of Strings, split on spaces
 */
public class AMBTokenizer {

    public static ArrayList<AMBTokens> tokenize(String file){
        ArrayList<AMBTokens> tokens = new ArrayList<>();

        /* This will take in the file, read it, turn into big string,
         * then turn into an array of strings
         */
        try {
            // read the test.amb file
            StringBuilder rawData = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            // turn that file to large string
            while ((line = br.readLine()) != null) {
                rawData.append(line).append("\n");
            }
            br.close();

            // the array of strings
            String[] stringTok = rawData.toString().split("\\s|\\n");
            System.out.println(Arrays.toString(stringTok));

            /* This will tokenize the RE's and
             * add them to ArrayList. First: KEYWORDS; Second: SYMBOLS;
             * then tokenize the integer, characterString, and label grammars.
             * rawData will be read through the switch cases. It will be stored
             * as an Array String in stringTok (will use .split() to handle the
             * whitespace.
             */
            for (String str : stringTok) {
                AMBTokens tok = null;
                // First to tokenize: KEYWORDS
                switch (str) {
                    case "START_PROGRAM" -> tok = new START_PROGRAM();
                    case "END_PROGRAM." -> tok = new END_PROGRAM();
                    case "START_SUB" -> tok = new START_SUB();
                    case "END_SUB." -> tok = new END_SUB();
                    case "GOSUB" -> tok = new GOSUB();
                    case "CODE" -> tok = new CODE();
                    case "IF" -> tok = new IF();
                    case "THEN" -> tok = new THEN();
                    case "ELSE" -> tok = new ELSE();
                    case "END_IF" -> tok = new END_IF();
                    case "WHILE" -> tok = new WHILE();
                    case "DO" -> tok = new DO();
                    case "END_WHILE" -> tok = new END_WHILE();
                    case "INT" -> tok = new INT();
                    case "STRING" -> tok = new STRING();
                    case "PRINT" -> tok = new PRINT();
                    case "INPUT_INT" -> tok = new INPUT_INT();
                    case "INPUT_STRING" -> tok = new INPUT_STRING();

                    // Second: SYMBOLS
                    case "(" -> tok = new SoftOpen();
                    case ")" -> tok = new SoftClose();
                    case "[" -> tok = new HardOpen();
                    case "]" -> tok = new HardClose();
                    case ";" -> tok = new Semi();
                    case ":=" -> tok = new Assignment();
                    case ":" -> tok = new Colon();
                    case "*" -> tok = new MultOp(MultOp.Operand.mult);
                    case "/" -> tok = new MultOp(MultOp.Operand.divide);
                    case "+" -> tok = new AddOp(AddOp.Operand.add);
                    case "-" -> tok = new AddOp(AddOp.Operand.subtract);
                    case ">" -> tok = new CompOp(CompOp.Operand.greaterThan);
                    case "<" -> tok = new CompOp(CompOp.Operand.lessThan);
                    case "=>" -> tok = new CompOp(CompOp.Operand.greaterThanEq);
                    case "=<" -> tok = new CompOp(CompOp.Operand.lessThanEq);
                    case "=" -> tok = new CompOp(CompOp.Operand.equal);
                    case "!=" -> tok = new CompOp(CompOp.Operand.notEqual);
                    default -> {
                        if (str.matches("0|([1-9][0-9]*)|(-[1-9][0-9]*)")) { // Check if it's a number (0, naturalNumber, or negativeNumber)
                            tok = new Numbers(Integer.parseInt(str));
                        } else if (str.startsWith("\"") && str.endsWith("\"")) { // Check if it's a characterString (enclosed in quotes)
                            String content = str.substring(1,str.length() - 1); // Extract the string without quotes
                            tok = new CharacterString(content);
                        } else if (str.matches("[a-zA-Z][a-zA-Z0-9]*")) { // Check if it's a label (starts with letter, followed by characters)
                            tok = new Labels(str);
                        } else { // If none of these, token is invalid
                            System.err.println("Invalid token: " + str);
                        }
                    }
                };
                if(tok == null){
                    System.err.println("Tokenizing error. Bad token "+str);
                    System.exit(1);
                }
                tokens.add(tok);
            }
        } catch (IOException e){
            System.err.println("Error reading file: "+e.getMessage());
            System.exit(1);
        }
        return tokens;
    }
}
