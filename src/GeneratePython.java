import AMBParseTreeNodes.*;
import AMBTokenPKG.AMBTokens;
import AMBTokenPKG.KeywordsPKG.*;
import AMBTokenPKG.SymbolCollectionPKG.CharacterString;
import AMBTokenPKG.SymbolCollectionPKG.Labels;
import AMBTokenPKG.SymbolCollectionPKG.Numbers;
import AMBTokenPKG.SymbolsPKG.*;
import java.util.HashMap;
import java.util.Map;

public class GeneratePython {

    private AMBNodes root;
    private StringBuilder pythonCode;
    private int indentLevel;
    private Map<String, String> variableTypes;
    private Map<String, Integer> arrayDimensions;
    private Map<String, String> subroutines;

    public GeneratePython(AMBNodes root) {
        this.root = root;
        this.pythonCode = new StringBuilder();
        this.indentLevel = 0;
        this.variableTypes = new HashMap<>();
        this.arrayDimensions = new HashMap<>();
        this.subroutines = new HashMap<>();
    }

    /*
     * Generate Python code from the parse tree.
     */
    public String generateCode() {
        if (root instanceof Program) {
            processProgram((Program) root);
        }
        return pythonCode.toString();
    }

    /*
     * Processes the Program node and its children.
     */
    private void processProgram(Program program) {
        // Process variable declarations first
        for (int i = 0; i < program.getChildren().size(); i++) {
            if (program.getChildren().get(i) instanceof VariableList) {
                processVariableList((VariableList) program.getChildren().get(i));
            }
        }

        // Add a newline after variable declarations
        pythonCode.append("\n");

        // Process main function call at the end
        boolean hasMain = false;
        for (String subroutineName : subroutines.keySet()) {
            if (subroutineName.equals("main")) {
                hasMain = true;
                break;
            }
        }

        if (hasMain) {
            pythonCode.append("main()\n");
        }
    }

    /*
     * Processes a VariableList node and its children.
     */
    private void processVariableList(VariableList variableList) {
        for (int i = 0; i < variableList.getChildren().size(); i++) {
            if (variableList.getChildren().get(i) instanceof Variable) {
                processVariable((Variable) variableList.getChildren().get(i));
            } else if (variableList.getChildren().get(i) instanceof SubList) {
                processSubList((SubList) variableList.getChildren().get(i));
            } else if (variableList.getChildren().get(i) instanceof AMBTokens) {
                // Skip AMB tokens like "CODE"
            } else if (variableList.getChildren().get(i) instanceof VariableList) {
                processVariableList((VariableList) variableList.getChildren().get(i));
            }
        }
    }

    /*
     * Processes a Variable node and its children.
     */
    private void processVariable(Variable variable) {
        String type = "";
        String name = "";

        for (int i = 0; i < variable.getChildren().size(); i++) {
            if (variable.getChildren().get(i) instanceof INT) {
                type = "int";
            } else if (variable.getChildren().get(i) instanceof STRING) {
                type = "str";
            } else if (variable.getChildren().get(i) instanceof Labels) {
                name = ((Labels) variable.getChildren().get(i)).getLab();
                variableTypes.put(name, type);

                // Generate Python variable declaration with default value
                if (type.equals("int")) {
                    pythonCode.append(name).append(" = 0\n");
                } else if (type.equals("str")) {
                    pythonCode.append(name).append(" = \"\"\n");
                }
            } else if (variable.getChildren().get(i) instanceof ArrayVariable) {
                processArrayVariable((ArrayVariable) variable.getChildren().get(i));
            }
        }
    }

    /*
     * Processes an ArrayVariable node and its children.
     */
    private void processArrayVariable(ArrayVariable arrayVariable) {
        String type = "";
        String name = "";
        int size = 0;

        for (int i = 0; i < arrayVariable.getChildren().size(); i++) {
            if (arrayVariable.getChildren().get(i) instanceof INT) {
                type = "int";
            } else if (arrayVariable.getChildren().get(i) instanceof STRING) {
                type = "str";
            } else if (arrayVariable.getChildren().get(i) instanceof Labels) {
                name = ((Labels) arrayVariable.getChildren().get(i)).getLab();
                variableTypes.put(name, type + "[]");
            } else if (arrayVariable.getChildren().get(i) instanceof Numbers) {
                size = ((Numbers) arrayVariable.getChildren().get(i)).getVal();
                arrayDimensions.put(name, size);

                // Generate Python array declaration with default values
                pythonCode.append(name).append(" = [");
                if (type.equals("int")) {
                    for (int j = 0; j < size; j++) {
                        pythonCode.append("0");
                        if (j < size - 1) {
                            pythonCode.append(", ");
                        }
                    }
                } else if (type.equals("str")) {
                    for (int j = 0; j < size; j++) {
                        pythonCode.append("\"\"");
                        if (j < size - 1) {
                            pythonCode.append(", ");
                        }
                    }
                }
                pythonCode.append("]\n");
            }
        }
    }

    /*
     * Processes a SubList node and its children.
     */
    private void processSubList(SubList subList) {
        for (int i = 0; i < subList.getChildren().size(); i++) {
            if (subList.getChildren().get(i) instanceof Labels) {
                String subroutineName = ((Labels) subList.getChildren().get(i)).getLab();
                subroutines.put(subroutineName, "");

                // Start a Python function definition
                pythonCode.append("def ").append(subroutineName).append("():\n");
                indentLevel++;
            } else if (subList.getChildren().get(i) instanceof CodeList) {
                processCodeList((CodeList) subList.getChildren().get(i));
            } else if (subList.getChildren().get(i) instanceof SubList) {
                processSubList((SubList) subList.getChildren().get(i));
            }
        }
    }

    /*
     * Processes a CodeList node and its children.
     */
    private void processCodeList(CodeList codeList) {
        for (int i = 0; i < codeList.getChildren().size(); i++) {
            if (codeList.getChildren().get(i) instanceof CodeLine) {
                processCodeLine((CodeLine) codeList.getChildren().get(i));
            } else if (codeList.getChildren().get(i) instanceof CodeList) {
                processCodeList((CodeList) codeList.getChildren().get(i));
            }
        }
    }

    /*
     * Processes a CodeLine node and its children.
     */
    private void processCodeLine(CodeLine codeLine) {
        for (int i = 0; i < codeLine.getChildren().size(); i++) {
            if (codeLine.getChildren().get(i) instanceof LineLabel) {
                processLineLabel((LineLabel) codeLine.getChildren().get(i));
            } else if (codeLine.getChildren().get(i) instanceof PRINT) {
                // Handle print statement
                addIndent();
                pythonCode.append("print( ");

                // Find expression in children
                for (int j = 0; j < codeLine.getChildren().size(); j++) {
                    if (codeLine.getChildren().get(j) instanceof Expression) {
                        processExpression((Expression) codeLine.getChildren().get(j));
                    }
                }

                pythonCode.append(" )\n");
            } else if (codeLine.getChildren().get(i) instanceof GOSUB) {
                // Handle subroutine call
                for (int j = 0; j < codeLine.getChildren().size(); j++) {
                    if (codeLine.getChildren().get(j) instanceof Labels) {
                        String subroutineName = ((Labels) codeLine.getChildren().get(j)).getLab();
                        addIndent();
                        pythonCode.append(subroutineName).append("()\n");
                    }
                }
            } else if (codeLine.getChildren().get(i) instanceof Condition) {
                processCondition((Condition) codeLine.getChildren().get(i));
            } else if (codeLine.getChildren().get(i) instanceof Loop) {
                processLoop((Loop) codeLine.getChildren().get(i));
            }
        }
    }

    /*
     * Processes a LineLabel node and its children.
     */
    private void processLineLabel(LineLabel lineLabel) {
        String varName = "";

        for (int i = 0; i < lineLabel.getChildren().size(); i++) {
            if (lineLabel.getChildren().get(i) instanceof Labels) {
                varName = ((Labels) lineLabel.getChildren().get(i)).getLab();
            } else if (lineLabel.getChildren().get(i) instanceof AssignmentVar) {
                processAssignment((AssignmentVar) lineLabel.getChildren().get(i), varName);
            }
        }
    }

    /*
     * Processes an AssignmentVar node and its children.
     */
    private void processAssignment(AssignmentVar assignment, String varName) {
        boolean isArrayAssignment = false;
        String arrayIndex = "";

        // Check if this is an array assignment
        for (int i = 0; i < assignment.getChildren().size(); i++) {
            if (assignment.getChildren().get(i) instanceof Numbers &&
                    i > 0 && assignment.getChildren().get(i-1) instanceof HardOpen) {
                isArrayAssignment = true;
                arrayIndex = String.valueOf(((Numbers) assignment.getChildren().get(i)).getVal());
            }
        }

        // Generate assignment statement
        addIndent();
        if (isArrayAssignment) {
            pythonCode.append(varName).append("[").append(arrayIndex).append("]");
        } else {
            pythonCode.append(varName);
        }
        pythonCode.append("= ");

        // Process right-hand side (expression or input)
        for (int i = 0; i < assignment.getChildren().size(); i++) {
            if (assignment.getChildren().get(i) instanceof ExpressionOrInput) {
                processExpressionOrInput((ExpressionOrInput) assignment.getChildren().get(i));
            }
        }

        pythonCode.append("\n");
    }

    /*
     * Processes an ExpressionOrInput node and its children.
     */
    private void processExpressionOrInput(ExpressionOrInput expressionOrInput) {
        for (int i = 0; i < expressionOrInput.getChildren().size(); i++) {
            if (expressionOrInput.getChildren().get(i) instanceof Expression) {
                processExpression((Expression) expressionOrInput.getChildren().get(i));
            } else if (expressionOrInput.getChildren().get(i) instanceof INPUT_INT) {
                pythonCode.append("int(input(\"\"))");
            } else if (expressionOrInput.getChildren().get(i) instanceof INPUT_STRING) {
                pythonCode.append("input(\"\")");
            }
        }
    }

    /*
     * Processes an Expression node and its children.
     */
    private void processExpression(Expression expression) {
        for (int i = 0; i < expression.getChildren().size(); i++) {
            if (expression.getChildren().get(i) instanceof Term) {
                processTerm((Term) expression.getChildren().get(i));
            } else if (expression.getChildren().get(i) instanceof TermTail) {
                processTermTail((TermTail) expression.getChildren().get(i));
            }
        }
    }

    /*
     * Processes a Term node and its children.
     */
    private void processTerm(Term term) {
        for (int i = 0; i < term.getChildren().size(); i++) {
            if (term.getChildren().get(i) instanceof Factor) {
                processFactor((Factor) term.getChildren().get(i));
            } else if (term.getChildren().get(i) instanceof FactorTail) {
                processFactorTail((FactorTail) term.getChildren().get(i));
            }
        }
    }

    /*
     * Processes a TermTail node and its children.
     */
    private void processTermTail(TermTail termTail) {
        for (int i = 0; i < termTail.getChildren().size(); i++) {
            if (termTail.getChildren().get(i) instanceof AddOp) {
                pythonCode.append(" ").append(((AddOp) termTail.getChildren().get(i)).getOp().toString()).append(" ");
            } else if (termTail.getChildren().get(i) instanceof Term) {
                processTerm((Term) termTail.getChildren().get(i));
            } else if (termTail.getChildren().get(i) instanceof TermTail) {
                processTermTail((TermTail) termTail.getChildren().get(i));
            }
        }
    }

    /*
     * Processes a Factor node and its children.
     */
    private void processFactor(Factor factor) {
        for (int i = 0; i < factor.getChildren().size(); i++) {
            if (factor.getChildren().get(i) instanceof SoftOpen) {
                pythonCode.append("(");
            } else if (factor.getChildren().get(i) instanceof SoftClose) {
                pythonCode.append(")");
            } else if (factor.getChildren().get(i) instanceof Numbers) {
                pythonCode.append(((Numbers) factor.getChildren().get(i)).getVal());
            } else if (factor.getChildren().get(i) instanceof CharacterString) {
                pythonCode.append("\"").append(((CharacterString) factor.getChildren().get(i)).getVal()).append("\"");
            } else if (factor.getChildren().get(i) instanceof Labels) {
                String varName = ((Labels) factor.getChildren().get(i)).getLab();
                pythonCode.append(varName);
            } else if (factor.getChildren().get(i) instanceof Expression) {
                processExpression((Expression) factor.getChildren().get(i));
            } else if (factor.getChildren().get(i) instanceof PossibleArray) {
                processPossibleArray((PossibleArray) factor.getChildren().get(i));
            }
        }
    }

    /*
     * Processes a FactorTail node and its children.
     */
    private void processFactorTail(FactorTail factorTail) {
        for (int i = 0; i < factorTail.getChildren().size(); i++) {
            if (factorTail.getChildren().get(i) instanceof MultOp) {
                pythonCode.append(" ").append(((MultOp) factorTail.getChildren().get(i)).getOp().toString()).append(" ");
            } else if (factorTail.getChildren().get(i) instanceof Factor) {
                processFactor((Factor) factorTail.getChildren().get(i));
            } else if (factorTail.getChildren().get(i) instanceof FactorTail) {
                processFactorTail((FactorTail) factorTail.getChildren().get(i));
            }
        }
    }

    /*
     * Processes a PossibleArray node and its children.
     */
    private void processPossibleArray(PossibleArray possibleArray) {
        for (int i = 0; i < possibleArray.getChildren().size(); i++) {
            if (possibleArray.getChildren().get(i) instanceof HardOpen) {
                pythonCode.append("[");
            } else if (possibleArray.getChildren().get(i) instanceof ArrayNumberOrLabel) {
                processArrayNumberOrLabel((ArrayNumberOrLabel) possibleArray.getChildren().get(i));
            }
        }
    }

    /*
     * Processes an ArrayNumberOrLabel node and its children.
     */
    private void processArrayNumberOrLabel(ArrayNumberOrLabel arrayNumberOrLabel) {
        for (int i = 0; i < arrayNumberOrLabel.getChildren().size(); i++) {
            if (arrayNumberOrLabel.getChildren().get(i) instanceof Numbers) {
                pythonCode.append(((Numbers) arrayNumberOrLabel.getChildren().get(i)).getVal());
            } else if (arrayNumberOrLabel.getChildren().get(i) instanceof Labels) {
                pythonCode.append(((Labels) arrayNumberOrLabel.getChildren().get(i)).getLab());
            } else if (arrayNumberOrLabel.getChildren().get(i) instanceof HardClose) {
                pythonCode.append("]");
            }
        }
    }

    /*
     * Processes a Condition node and its children.
     */
    private void processCondition(Condition condition) {
        addIndent();
        pythonCode.append("if ");

        // Process left expression
        for (int i = 0; i < condition.getChildren().size(); i++) {
            if (condition.getChildren().get(i) instanceof Expression && i == 0) {
                processExpression((Expression) condition.getChildren().get(i));
            } else if (condition.getChildren().get(i) instanceof CompOp) {
                pythonCode.append(" ").append(((CompOp) condition.getChildren().get(i)).getOp().toString()).append(" ");
            } else if (condition.getChildren().get(i) instanceof Expression && i > 0) {
                processExpression((Expression) condition.getChildren().get(i));
            } else if (condition.getChildren().get(i) instanceof ThenCodeList) {
                pythonCode.append(" :\n");
                indentLevel++;
                processThenCodeList((ThenCodeList) condition.getChildren().get(i));
                indentLevel--;
            }
        }
    }

    /*
     * Processes a ThenCodeList node and its children.
     */
    private void processThenCodeList(ThenCodeList thenCodeList) {
        boolean hasElse = false;

        for (int i = 0; i < thenCodeList.getChildren().size(); i++) {
            if (thenCodeList.getChildren().get(i) instanceof CodeLine) {
                processCodeLine((CodeLine) thenCodeList.getChildren().get(i));
            } else if (thenCodeList.getChildren().get(i) instanceof ThenCodeList) {
                processThenCodeList((ThenCodeList) thenCodeList.getChildren().get(i));
            } else if (thenCodeList.getChildren().get(i) instanceof ELSE) {
                hasElse = true;
                indentLevel--;
                addIndent();
                pythonCode.append("else:\n");
                indentLevel++;
            } else if (thenCodeList.getChildren().get(i) instanceof ElseCodeList) {
                processElseCodeList((ElseCodeList) thenCodeList.getChildren().get(i));
            }
        }

        // If there was no else section, we need to handle END_IF differently
        if (!hasElse) {
            for (int i = 0; i < thenCodeList.getChildren().size(); i++) {
                if (thenCodeList.getChildren().get(i) instanceof END_IF) {
                }
            }
        }
    }

    /*
     * Processes an ElseCodeList node and its children.
     */
    private void processElseCodeList(ElseCodeList elseCodeList) {
        for (int i = 0; i < elseCodeList.getChildren().size(); i++) {
            if (elseCodeList.getChildren().get(i) instanceof CodeLine) {
                processCodeLine((CodeLine) elseCodeList.getChildren().get(i));
            } else if (elseCodeList.getChildren().get(i) instanceof ThenCodeList) {
                processThenCodeList((ThenCodeList) elseCodeList.getChildren().get(i));
            }
        }
    }

    /*
     * Processes a Loop node and its children.
     */
    private void processLoop(Loop loop) {
        addIndent();
        pythonCode.append("while ");

        // Process expressions and CompOp
        for (int i = 0; i < loop.getChildren().size(); i++) {
            if (loop.getChildren().get(i) instanceof Expression && i == 0) {
                processExpression((Expression) loop.getChildren().get(i));
            } else if (loop.getChildren().get(i) instanceof CompOp) {
                pythonCode.append(" ").append(((CompOp) loop.getChildren().get(i)).getOp()).append(" ");
            } else if (loop.getChildren().get(i) instanceof Expression && i > 0) {
                processExpression((Expression) loop.getChildren().get(i));
            } else if (loop.getChildren().get(i) instanceof WhileCodeList) {
                pythonCode.append(" :\n");
                indentLevel++;
                processWhileCodeList((WhileCodeList) loop.getChildren().get(i));
                indentLevel--;
            }
        }
    }

    /*
     * Processes a WhileCodeList node and its children.
     */
    private void processWhileCodeList(WhileCodeList whileCodeList) {
        for (int i = 0; i < whileCodeList.getChildren().size(); i++) {
            if (whileCodeList.getChildren().get(i) instanceof CodeLine) {
                processCodeLine((CodeLine) whileCodeList.getChildren().get(i));
            } else if (whileCodeList.getChildren().get(i) instanceof WhileCodeList) {
                processWhileCodeList((WhileCodeList) whileCodeList.getChildren().get(i));
            }
        }
    }

    /*
     * Adds the appropriate number of indentation tabs to the Python code.
     */
    private void addIndent() {
        for (int i = 0; i < indentLevel; i++) {
            pythonCode.append("\t");
        }
    }
}
