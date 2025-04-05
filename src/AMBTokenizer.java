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
            if(str.equals("START_PROGRAM")) {
                tok = new START_PROGRAM();
            } else if(){

            } else if (str.equals("(")) {
                tok = new softOpen();
            } else if () {

            } else {
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
