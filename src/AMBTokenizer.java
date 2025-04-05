import AMBTokenPKG.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class will tokenize all the regular expressions
 */

public class AMBTokenizer {

    public static ArrayList<AMBTokens> tokenize(String file){
        ArrayList<AMBTokens> tokens = new ArrayList<>();
        // read file
        // Turn file into giant String
        // Turn String into array of Strings, split on spaces
        // SUBSTANTIAL SIMPLIFICATION
        //( 4 * 2 ) need spaces to make valid, will make life easier

        /*
        Test to put rawData in array and print to remove white space.
        Called in the Main
         */
        String rawData = "( 4 * 2 )";
        String[] stringTok = rawData.trim().split("\\s");
        System.out.println(Arrays.toString(stringTok));

        /* This will be the loop that will tokenize the RE's and
         * add them to ArrayList. First: KEYWORDS; Second: SYMBOLS;
         * then tokenize the integer, characterString, and label grammars
         */
        for (String str : stringTok) {
            AMBTokens tok = null;
            // First to tokenize: KEYWORDS
            switch (str) {
                case "START_PROGRAM" -> tok = new START_PROGRAM();
                case "END_PROGRAM" -> tok = new END_PROGRAM();
                case "START_SUB" -> tok = new START_SUB();
                case "END_SUB" -> tok = new END_SUB();
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
                case "INPUT" -> tok = new INPUT();


                // Second: SYMBOLS
                case "(" -> tok = new SoftOpen();
                case ")" -> tok = new SoftClose();
                case "[" -> tok = new HardOpen();
                case "]" -> tok = new HardClose();
                case "\"" -> tok = new Quote();
                case ";" -> tok = new Semi();
                case ":=" -> tok = new Assignment();
                case ":" -> tok = new Colon();
                case "*" -> tok = new MultOp(MultOp.Operand.mult);
                case "/" -> tok = new MultOp(MultOp.Operand.divide);
                case "+" -> tok = new AddOp(AddOp.Operand.add);
                case "-" -> tok = new AddOp(AddOp.Operand.subtract);
                case "<" -> tok = new CompOp(CompOp.Operand.greaterThan);
                case ">" -> tok = new CompOp(CompOp.Operand.lessThan);
                case "=<" -> tok = new CompOp(CompOp.Operand.greaterThanEq);
                case "=>" -> tok = new CompOp(CompOp.Operand.lessThanEq);
                case "=" -> tok = new CompOp(CompOp.Operand.equal);
                case "!=" -> tok = new CompOp(CompOp.Operand.notEqual);
                default -> {
                }
                //begin DFA implementation of symbol collection
                // go character by character in str (the current string token)
                // determine if it's going to be a:
                // number
                // characterString
                // label
                // or failure ex. 32ab, 3*2 (if proper spacing isn't used)
            }
            if(tok == null){
                System.err.println("Tokenizing error. Bad token "+str);
                System.exit(1);
            }
            tokens.add(tok);



//            if(str.equals("START_PROGRAM")) {
//                tok = new START_PROGRAM();
//            } else if (str.equals("END_PROGRAM")) {
//                tok = new END_PROGRAM();
//            } else if (str.equals("START_SUB")) {
//                tok = new START_SUB();
//            } else if (str.equals("END_SUB")) {
//                tok = new END_SUB();
//            } else if (str.equals("GOSUB")) {
//                tok = new GOSUB();
//            } else if (str.equals("CODE")) {
//                tok = new CODE();
//            } else if (str.equals("IF")) {
//                tok = new IF();
//            } else if (str.equals("THEN")) {
//                tok = new THEN();
//            } else if (str.equals("ELSE")) {
//                tok = new ELSE();
//            } else if (str.equals("END_IF")) {
//                tok = new END_IF();
//            } else if (str.equals("WHILE")) {
//                tok = new WHILE();
//            } else if (str.equals("DO")) {
//                tok = new DO();
//            } else if (str.equals("END_WHILE")) {
//                tok = new END_WHILE();
//            } else if (str.equals("INT")) {
//                tok = new INT();
//            } else if (str.equals("STRING")) {
//                tok = new STRING();
//            } else if (str.equals("PRINT")) {
//                tok = new PRINT();
//            } else if (str.equals("INPUT")) {
//                tok = new INPUT();
//
//                // Second: SYMBOLS
//            } else if (str.equals("(")) {
//                tok = new SoftOpen();
//            } else if (str.equals(")")) {
//                tok = new SoftClose();
//            } else if (str.equals("[")) {
//                tok = new HardOpen();
//            } else if (str.equals("]")) {
//                tok = new HardClose();
//            } else if (str.equals("\"")) {
//                tok = new Quote();
//            } else if (str.equals(";")) {
//                tok = new Semi();
//            } else if (str.equals(":=")) {
//                tok = new Assignment();
//            } else if (str.equals(":")) {
//                tok = new Colon();
//            } else if (str.equals("*")) {
//                tok = new MultOp(MultOp.Operand.mult);
//            } else if (str.equals("/")) {
//                tok = new MultOp(MultOp.Operand.divide);
//            } else if (str.equals("+")) {
//                tok = new AddOp(AddOp.Operand.add);
//            } else if (str.equals("-")) {
//                tok = new AddOp(AddOp.Operand.subtract);
//            } else if (str.equals("<")) {
//                tok = new CompOp(CompOp.Operand.greaterThan);
//            } else if (str.equals(">")) {
//                tok = new CompOp(CompOp.Operand.lessThan);
//            } else if (str.equals("=<")) {
//                tok = new CompOp(CompOp.Operand.greaterThanEq);
//            } else if (str.equals("=>")) {
//                tok = new CompOp(CompOp.Operand.lessThanEq);
//            } else if (str.equals("=")) {
//                tok = new CompOp(CompOp.Operand.equal);
//            } else if (str.equals("!=")) {
//                tok = new CompOp(CompOp.Operand.notEqual);
//            } else {
//                //begin DFA implementation of symbol collection
//                // go character by character in str (the current string token)
//                // determine if it's going to be a:
//                // number
//                // characterString
//                // label
//                // or failure ex. 32ab, 3*2 (if proper spacing isn't used)
//            }
//            if(tok == null){
//                System.err.println("Tokenizing error. Bad token "+str);
//                System.exit(1);
//            }
//            tokens.add(tok);
        }
        return tokens;
    }


}
