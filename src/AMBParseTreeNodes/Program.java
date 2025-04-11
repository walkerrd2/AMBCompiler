package AMBParseTreeNodes;

public class Program extends AMBNodes {
    @Override
    public String genPython() {
        return children.get(1).genPython();
    }
}
