package AMBTokenPKG.SymbolsPKG;

public class CompOp extends Symbols {

    public enum Operand{greaterThan, lessThan, greaterThanEq, lessThanEq, equal, notEqual};
    private Operand op;

    public CompOp(Operand op) {
        this.op = op;
    }

    public Operand getOp() {
        return op;
    }

    // make toString()
    public String toString(){
        return switch(op){
            case greaterThan -> ">";
            case lessThan -> "<";
            case greaterThanEq -> ">=";
            case lessThanEq -> "<=";
            case equal -> "=";
            case notEqual -> "!=";
            default -> null;
        };
    }


}
