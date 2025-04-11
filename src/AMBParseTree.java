import AMBParseTreeNodes.*;
import AMBTokenPKG.AMBTokens;
import AMBTokenPKG.KeywordsPKG.*;
import AMBTokenPKG.SymbolCollectionPKG.Labels;
import AMBTokenPKG.SymbolCollectionPKG.Numbers;
import AMBTokenPKG.SymbolsPKG.Colon;
import AMBTokenPKG.SymbolsPKG.HardClose;
import AMBTokenPKG.SymbolsPKG.HardOpen;
import AMBTokenPKG.SymbolsPKG.Semi;
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
