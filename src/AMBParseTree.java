import AMBParseTreeNodes.*;
import AMBTokenPKG.AMBTokens;
import AMBTokenPKG.KeywordsPKG.*;
import AMBTokenPKG.SymbolCollectionPKG.CharacterString;
import AMBTokenPKG.SymbolCollectionPKG.Labels;
import AMBTokenPKG.SymbolCollectionPKG.Numbers;
import AMBTokenPKG.SymbolsPKG.*;
import Exceptions.ExpectedSymbol;

import java.util.ArrayList;

public class AMBParseTree {

    static int currentTok = 0;
    static ArrayList<AMBTokens> code;
    public static AMBNodes root = null;

    public static void generatePT(ArrayList<AMBTokens> code){
        AMBParseTree.code = code;
        currentTok = 0;
        root = program();
    }

    public static AMBNodes program(){
        AMBTokens cur = code.get(currentTok);
        AMBNodes node = new Program();
        Class<?> curClass = cur.getClass();
        if (curClass == START_PROGRAM.class){
            consume(node, START_PROGRAM.class);
            node.addChild(variableList());
        } else {
            System.err.println("On token number "+currentTok+" Invalid token "+cur);
            throw new ExpectedSymbol();
        }
        return node;
    }

    public static AMBNodes variableList(){
        AMBTokens cur = code.get(currentTok);
        AMBNodes node = new VariableList();
        Class<?> curClass = cur.getClass();
        if (curClass == INT.class || curClass == STRING.class || curClass == HardOpen.class){
            node.addChild(variable());
            node.addChild((variableList()));
        } else if (curClass == CODE.class){
            consume(node, CODE.class);
            node.addChild(sublist());
        } else {
            System.err.println("On token number "+currentTok+" Invalid token "+cur);
            throw new ExpectedSymbol();
        }
        return node;
    }

    public static AMBNodes variable(){
        AMBTokens cur = code.get(currentTok);
        AMBNodes node = new Variable();
        Class<?> curClass = cur.getClass();
        if (curClass == INT.class){
            consume(node, INT.class);
            consume(node, Labels.class);
            consume(node, Semi.class);
        } else if (curClass == STRING.class) {
            consume(node, STRING.class);
            consume(node, Labels.class);
            consume(node, Semi.class);
        } else if (curClass == HardOpen.class) {
            consume(node, HardOpen.class);
            node.addChild(arrayVariable());
        } else {
            System.err.println("On token number "+currentTok+" Invalid token "+cur);
            throw new ExpectedSymbol();
        }
        return node;
    }

    public static AMBNodes arrayVariable(){
        AMBTokens cur = code.get(currentTok);
        AMBNodes node = new ArrayVariable();
        Class<?> curClass = cur.getClass();
        if (curClass == INT.class) {
            consume(node, INT.class);
            consume(node, HardClose.class);
            consume(node, Labels.class);
            consume(node, HardOpen.class);
            consume(node, Numbers.class);
            consume(node, HardClose.class);
            consume(node, Semi.class);
        } else if (curClass == STRING.class){
            consume(node, STRING.class);
            consume(node, HardClose.class);
            consume(node, Labels.class);
            consume(node, HardOpen.class);
            consume(node, Numbers.class);
            consume(node, HardClose.class);
            consume(node, Semi.class);
        } else {
            System.err.println("On token number "+currentTok+" Invalid token "+cur);
            throw new ExpectedSymbol();
        }
        return node;
    }

    public static AMBNodes sublist(){
        AMBTokens cur = code.get(currentTok);
        AMBNodes node = new SubList();
        Class<?> curClass = cur.getClass();
        if (curClass == START_SUB.class){
            consume(node, START_SUB.class);
            consume(node, Labels.class);
            consume(node, Colon.class);
            node.addChild(codeList());
            node.addChild(sublist());
        } else if (curClass == END_PROGRAM.class){
            consume(node, END_PROGRAM.class);
        } else {
            System.err.println("On token number "+currentTok+" Invalid token "+cur);
            throw new ExpectedSymbol();
        }
        return node;
    }

    public static AMBNodes codeList(){
        AMBTokens cur = code.get(currentTok);
        AMBNodes node = new CodeList();
        Class<?> curClass = cur.getClass();
        if (curClass == PRINT.class || curClass == GOSUB.class || curClass == Labels.class || curClass == IF.class || curClass == WHILE.class){
            node.addChild(codeLine());
            node.addChild(codeList());
        } else if (curClass == END_SUB.class){
            consume(node, END_SUB.class);
        } else {
            System.err.println("On token number "+currentTok+" Invalid token "+cur);
            throw new ExpectedSymbol();
        }
        return node;
    }

    public static AMBNodes codeLine(){
        AMBTokens cur = code.get(currentTok);
        AMBNodes node = new CodeLine();
        Class<?> curClass = cur.getClass();
        if (curClass == Labels.class){
            node.addChild(lineLabel());
        } else if (curClass == IF.class){
            node.addChild(condition());
        } else if (curClass == WHILE.class){
            node.addChild(loop());
        } else if (curClass == PRINT.class){
            consume(node, PRINT.class);
            consume(node, SoftOpen.class);
            node.addChild(expression());
            consume(node, SoftClose.class);
            consume(node, Semi.class);
        } else if (curClass == GOSUB.class){
            consume(node, GOSUB.class);
            consume(node, Labels.class);
            consume(node, Semi.class);
        } else {
            System.err.println("On token number "+currentTok+" Invalid token "+cur);
            throw new ExpectedSymbol();
        }
        return node;
    }

    public static AMBNodes lineLabel(){
        AMBTokens cur = code.get(currentTok);
        AMBNodes node = new LineLabel();
        Class<?> curClass = cur.getClass();
        if (curClass == Labels.class){
            consume(node, Labels.class);
            node.addChild(assignment());
        } else {
            System.err.println("On token number "+currentTok+" Invalid token "+cur);
            throw new ExpectedSymbol();
        }
        return node;
    }

    public static AMBNodes assignment(){
        AMBTokens cur = code.get(currentTok);
        AMBNodes node = new AssignmentVar();
        Class<?> curClass = cur.getClass();
        if (curClass == Assignment.class){
            consume(node, Assignment.class);
            node.addChild(expressionOrInput());
            consume(node, Semi.class);
        } else if (curClass == HardOpen.class){
            consume(node, HardOpen.class);
            consume(node, Numbers.class);
            consume(node, HardClose.class);
            consume(node, Assignment.class);
            node.addChild(expressionOrInput());
            consume(node, Semi.class);
        } else {
            System.err.println("On token number "+currentTok+" Invalid token "+cur);
            throw new ExpectedSymbol();
        }
        return node;
    }

    public static AMBNodes condition(){
        AMBTokens cur = code.get(currentTok);
        AMBNodes node = new Condition();
        Class<?> curClass = cur.getClass();
        if (curClass ==  IF.class){
            consume(node, IF.class);
            node.addChild(expression());
            consume(node, CompOp.class);
            node.addChild(expression());
            consume(node, THEN.class);
            node.addChild(thenCodeList());
        } else {
            System.err.println("On token number "+currentTok+" Invalid token "+cur);
            throw new ExpectedSymbol();
        }
        return node;
    }

    public static AMBNodes thenCodeList(){
        AMBTokens cur = code.get(currentTok);
        AMBNodes node = new ThenCodeList();
        Class<?> curClass = cur.getClass();
        if (curClass == PRINT.class || curClass == GOSUB.class || curClass == Labels.class || curClass == IF.class || curClass == WHILE.class){
            node.addChild(codeLine());
            node.addChild(thenCodeList());
        } else if (curClass == ELSE.class){
            consume(node, ELSE.class);
            node.addChild(elseCodeList());
        } else if (curClass == END_IF.class){
            consume(node, END_IF.class);
        } else {
            System.err.println("On token number "+currentTok+" Invalid token "+cur);
            throw new ExpectedSymbol();
        }
        return node;
    }

    public static AMBNodes elseCodeList(){
        AMBTokens cur = code.get(currentTok);
        AMBNodes node = new ElseCodeList();
        Class<?> curClass = cur.getClass();
        if (curClass == PRINT.class || curClass == GOSUB.class || curClass == Labels.class || curClass == IF.class || curClass == WHILE.class){
            node.addChild(codeLine());
            node.addChild(thenCodeList());
        } else if (curClass == END_IF.class){
            consume(node, END_IF.class);
        } else {
            System.err.println("On token number "+currentTok+" Invalid token "+cur);
            throw new ExpectedSymbol();
        }
        return node;
    }

    public static AMBNodes loop(){
        AMBTokens cur = code.get(currentTok);
        AMBNodes node = new Loop();
        Class<?> curClass = cur.getClass();
        if(curClass == WHILE.class){
            consume(node, WHILE.class);
            node.addChild(expression());
            consume(node, CompOp.class);
            node.addChild(expression());
            consume(node, DO.class);
            node.addChild(whileCodeList());
        } else {
            System.err.println("On token number "+currentTok+" Invalid token "+cur);
            throw new ExpectedSymbol();
        }
        return node;
    }

    public static AMBNodes whileCodeList(){
        AMBTokens cur = code.get(currentTok);
        AMBNodes node = new WhileCodeList();
        Class<?> curClass = cur.getClass();
        if (curClass == PRINT.class || curClass == GOSUB.class || curClass == Labels.class || curClass == IF.class || curClass == WHILE.class){
            node.addChild(codeLine());
            node.addChild(whileCodeList());
        } else if (curClass == END_WHILE.class){
            consume(node, END_WHILE.class);
        } else {
            System.err.println("On token number "+currentTok+" Invalid token "+cur);
            throw new ExpectedSymbol();
        }
        return node;
    }

    public static AMBNodes expressionOrInput(){
        AMBTokens cur = code.get(currentTok);
        AMBNodes node = new ExpressionOrInput();
        Class<?> curClass = cur.getClass();
        if(curClass == SoftOpen.class || curClass == Numbers.class || curClass == CharacterString.class || curClass == Labels.class){
            node.addChild(expression());
        } else if (curClass == INPUT_INT.class){
            consume(node, INPUT_INT.class);
            consume(node, Semi.class);
        } else if (curClass == INPUT_STRING.class){
            consume(node, INPUT_STRING.class);
            consume(node, Semi.class);
        } else {
            System.err.println("On token number "+currentTok+" Invalid token "+cur);
            throw new ExpectedSymbol();
        }
        return node;
    }

    public static AMBNodes expression(){
        AMBTokens cur = code.get(currentTok);
        AMBNodes node = new Expression();
        Class<?> curClass = cur.getClass();
        if(curClass == SoftOpen.class || curClass == Numbers.class || curClass == CharacterString.class || curClass == Labels.class){
            node.addChild(term());
            node.addChild(termTail());
        } else {
            System.err.println("On token number "+currentTok+" Invalid token "+cur);
            throw new ExpectedSymbol();
        }
        return node;
    }

    public static AMBNodes termTail(){
        AMBTokens cur = code.get(currentTok);
        AMBNodes node = new TermTail();
        Class<?> curClass = cur.getClass();
        if(curClass == AddOp.class){
            consume(node, AddOp.class);
            node.addChild(term());
            node.addChild(termTail());
        } else if (curClass == CompOp.class || curClass == DO.class || curClass == SoftClose.class || curClass == Semi.class || curClass == THEN.class){
            return node;
        } else {
            System.err.println("On token number "+currentTok+" Invalid token "+cur);
            throw new ExpectedSymbol();
        }
        return node;
    }

    public static AMBNodes term(){
        AMBTokens cur = code.get(currentTok);
        AMBNodes node = new Term();
        Class<?> curClass = cur.getClass();
        if(curClass == SoftOpen.class || curClass == Numbers.class || curClass == CharacterString.class || curClass == Labels.class){
            node.addChild(factor());
            node.addChild(factorTail());
        } else {
            System.err.println("On token number "+currentTok+" Invalid token: "+cur);
            throw new ExpectedSymbol();
        }
        return node;
    }

    public static AMBNodes factorTail(){
        AMBTokens cur = code.get(currentTok);
        AMBNodes node = new FactorTail();
        Class<?> curClass = cur.getClass();
        if(curClass == MultOp.class){
            consume(node, MultOp.class);
            node.addChild(factor());
            node.addChild(factorTail());
        } else if (curClass == AddOp.class || curClass == CompOp.class || curClass == DO.class || curClass == SoftClose.class || curClass == Semi.class || curClass == THEN.class){
            return node;
        } else {
            System.err.println("On token number "+currentTok+" Invalid token: "+cur);
            throw new ExpectedSymbol();
        }
        return node;
    }

    public static AMBNodes factor(){
        AMBTokens cur = code.get(currentTok);
        AMBNodes node = new Factor();
        Class<?> curClass = cur.getClass();
        if(curClass == SoftOpen.class){
            consume(node, SoftOpen.class);
            node.addChild(expression());
            consume(node, SoftClose.class);
        } else if (curClass == Numbers.class){
            consume(node, Numbers.class);
        } else if (curClass == CharacterString.class){
            consume(node, CharacterString.class);
        } else if (curClass == Labels.class){
            consume(node, Labels.class);
            node.addChild(possibleArray());
        } else {
            System.err.println("On token number "+currentTok+" Invalid token: "+cur);
            throw new ExpectedSymbol();
        }
        return node;
    }

    public static AMBNodes possibleArray(){
        AMBTokens cur = code.get(currentTok);
        AMBNodes node = new PossibleArray();
        Class<?> curClass = cur.getClass();
        if(curClass == HardOpen.class){
            consume(node, HardOpen.class);
            node.addChild(arrayNumberOrLabel());
        } else if(curClass == MultOp.class || curClass == AddOp.class || curClass == CompOp.class || curClass == DO.class || curClass == SoftClose.class || curClass == Semi.class || curClass == THEN.class){
            return node;
        } else {
            System.err.println("On token number "+currentTok+" Invalid token: "+cur);
            throw new ExpectedSymbol();
        }
        return node;
    }

    public static AMBNodes arrayNumberOrLabel(){
        AMBTokens cur = code.get(currentTok);
        AMBNodes node = new ArrayNumberOrLabel();
        Class<?> curClass = cur.getClass();
        if (curClass == Numbers.class){
            consume(node, Numbers.class);
            consume(node, HardClose.class);
        } else if(curClass == Labels.class){
            consume(node, Labels.class);
            consume(node, HardClose.class);
        } else {
            System.err.println("On token number "+currentTok+" Invalid token: "+cur);
            throw new ExpectedSymbol();
        }
        return node;
    }


    public static boolean consume(AMBNodes node, Class<?> cc){
        AMBTokens cur = code.get(currentTok);
        if(cur.getClass() == cc){
            currentTok++;
            node.addChild(cur);
            return true;
        } else {
            System.err.println("On token number "+currentTok+" Invalid token "+cur);
            throw new ExpectedSymbol();
        }
    }

}
