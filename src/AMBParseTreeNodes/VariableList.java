package AMBParseTreeNodes;

public class VariableList extends AMBNodes {

    @Override
    public String genPython() {
        if(children.get(0).getClass() == Variable.class){
            String str = "";
            str = children.get(0).genPython();
            str += children.get(1).genPython(); //variable list
            return str;
        } else {
            return children.get(1).genPython();
        }
    }
}
