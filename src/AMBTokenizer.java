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
//        for (String token : stringTok) {
//            System.out.println(token);
//        }

        return tokens;
    }


}
