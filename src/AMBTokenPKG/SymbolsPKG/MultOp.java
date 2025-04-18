package AMBTokenPKG.SymbolsPKG;

public class MultOp extends Symbols {

    public enum Operand{mult, divide};
    private Operand op;

    public MultOp(Operand op) {
        this.op = op;
    }

    public Operand getOp() {
        return op;
    }

    public String toString(){
        return switch(op){
            case mult -> "*";
            case divide -> "/";
            default -> null;
        };
    }


}
