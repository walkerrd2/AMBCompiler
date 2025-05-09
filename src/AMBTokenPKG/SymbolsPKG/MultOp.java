package AMBTokenPKG.SymbolsPKG;

public class MultOp extends Symbols {

    public enum Operand{mult, divide;

        @Override
        public String toString(){
            return switch(this){
                case mult -> "*";
                case divide -> "/";
                default -> null;
            };
        }
    }

    private Operand op;

    public MultOp(Operand op) {
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
