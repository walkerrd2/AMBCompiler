package AMBTokenPKG.SymbolCollectionPKG;
import AMBTokenPKG.AMBTokens;

public class CharacterString extends AMBTokens {

    private String val;

    public CharacterString(String val){
        this.val = val;
    }

    public String getVal(){
        return val;
    }

    public String toString(){
        return "\"" +val+ "\"";
    }

}
