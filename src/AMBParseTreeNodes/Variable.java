package AMBParseTreeNodes;

import AMBTokenPKG.KeywordsPKG.INT;
import AMBTokenPKG.KeywordsPKG.STRING;
import AMBTokenPKG.SymbolCollectionPKG.Labels;

public class Variable extends AMBNodes {

    @Override
    public String genPython(){
        if (children.get(0).getClass() == INT.class){
            Labels lab = (Labels)(children.get(1));
            return lab.getLab()+" = 0\n";
        } else if (children.get(0).getClass() == STRING.class) {
            Labels lab = (Labels)(children.get(1));
            return lab.getLab()+" = \"\"\n";
        } else {
            return getChildren().get(1).genPython();
        }
    }
}
