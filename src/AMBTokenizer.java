import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class AMBTokenizer {

    public static ArrayList<AMBTokens> tokenize(String file){
        ArrayList<AMBTokens> tokens = new ArrayList<>();
        // read file
        // Turn file into giant String
        String rawData = "( 4 * 2 )";

        // Turn String into array of Strings, split on spaces
        // SUBSTANTIAL SIMPLIFICATION
        //( 4 * 2 ) need spaces to make valid, will make life easier
        String[] stringTok = rawData.trim().split("\\s");
        System.out.println(Arrays.toString(stringTok));

        for (String str : stringTok) {
            AMBTokens tok = null;
            if(str.equals("START_PROGRAM")){
                tok = new START_PROGRAM();
            } else if (str.equals("(")){
                tok = new softOpen();
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
