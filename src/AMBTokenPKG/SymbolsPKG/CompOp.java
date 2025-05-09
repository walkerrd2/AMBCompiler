package AMBTokenPKG.SymbolsPKG;

public class CompOp extends Symbols {

    public enum Operand{
        greaterThan, lessThan, greaterThanEq, lessThanEq, equal, notEqual;

        @Override
        // make toString()
        public String toString(){
            return switch(this){
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


    private Operand op;

    public CompOp(Operand op) {
        this.op = op;
    }

    public Operand getOp() {
        return op;
    }

    @Override
    public String toString(){
        return op.toString();
    }
}
