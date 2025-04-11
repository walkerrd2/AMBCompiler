package AMBParseTreeNodes;

import java.util.ArrayList;

public class AMBNodes {
    ArrayList<AMBNodes> children;

    public AMBNodes(){
        children = new ArrayList<>();
    }

    public void addChild(AMBNodes node){
        children.add(node);
    }

    public ArrayList<AMBNodes> getChildren(){
        return children;
    }

}
