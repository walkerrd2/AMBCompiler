package AMBTokenPKG.SymbolCollectionPKG;

import AMBTokenPKG.AMBTokens;

public class Labels extends AMBTokens {
    private String lab;

    public Labels(String lab){
        this.lab = lab;
    }

    public String getLab(){
        return lab;
    }

    public String toString(){
        return lab;
    }
}
