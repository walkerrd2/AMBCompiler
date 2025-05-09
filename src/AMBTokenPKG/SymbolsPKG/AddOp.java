package AMBTokenPKG.SymbolsPKG;

public class AddOp extends Symbols {

    public enum Operand {add, subtract;

        @Override
        public String toString(){
            return switch(this){
                case add -> "+";
                case subtract -> "-";
                default -> null;
            };
        }
    }
    private Operand op;

    public AddOp(Operand op) {
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
