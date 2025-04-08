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

    public String toString(){
        return switch(op){
            case add -> "+";
            case subtract -> "-";
            default -> null;
        };
    }



}
