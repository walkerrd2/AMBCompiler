package AMBTokenPKG;

public class CompOp extends Symbols {

    public enum Operand{greaterThan, lessThan, greaterThanEq, lessThanEq, equal, notEqual};
    private Operand op;

    public CompOp(Operand op) {
        this.op = op;
    }

    public Operand getOp() {
        return op;
    }


}
