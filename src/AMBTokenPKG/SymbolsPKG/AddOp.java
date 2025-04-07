package AMBTokenPKG.SymbolsPKG;

public class AddOp extends Symbols {

    public enum Operand {add, subtract};
    private Operand op;

    public AddOp(Operand op) {
        this.op = op;
    }

    public Operand getOp() {
        return op;
    }



}
