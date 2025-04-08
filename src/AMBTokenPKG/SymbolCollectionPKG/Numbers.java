package AMBTokenPKG.SymbolCollectionPKG;
import AMBTokenPKG.AMBTokens;

public class Numbers extends AMBTokens {
    private int val;

    public Numbers(int val){
        this.val = val;
    }

    public int getVal(){
        return val;
    }

    public String toString(){
        return Integer.toString(val);
    }

}
