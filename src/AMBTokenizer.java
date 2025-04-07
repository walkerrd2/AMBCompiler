import AMBTokenPKG.*;
import AMBTokenPKG.KeywordsPKG.*;
import AMBTokenPKG.SymbolsPKG.*;

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
        This is the String that we want to tokenize
         */
        String rawData = "( 4 * 2 )";

        /* This will tokenize the RE's and
         * add them to ArrayList. First: KEYWORDS; Second: SYMBOLS;
         * then tokenize the integer, characterString, and label grammars.
         * rawData will be read through the switch cases. It will be stored
         * as an Array String in stringTok (will use .split() to handle the
         * whitespace.
         */
        String[] stringTok = rawData.split("\s|\n");
        System.out.println(Arrays.toString(stringTok));
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

        }
        return tokens;
    }


}
