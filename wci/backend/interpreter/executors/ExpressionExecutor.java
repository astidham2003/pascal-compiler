package wci.backend.interpreter.executors;

import java.util.ArrayList;
import java.util.EnumSet;

import wci.backend.interpreter.executors.AssignmentExecutor;

import wci.intermediate.ICodeNode;

import wci.intermediate.icodeimpl.ICodeNodeTypeImpl;

import wci.intermediate.SymTabEntry;

import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.*;
import static wci.intermediate.icodeimpl.ICodeKeyImpl.ID;
import static wci.intermediate.icodeimpl.ICodeKeyImpl.VALUE;

import static wci.intermediate.symtabimpl.SymTabKeyImpl.DATA_VALUE;

import static wci.backend.interpreter.RuntimeErrorCode.
	DIVISION_BY_ZERO;

/**
* <h1>ExpressionExecutor</h1>
*
* <p>Executes an expression.</p>
*/
public class ExpressionExecutor extends AssignmentExecutor
{

	// Set of arithmetic operator node types.
	private static final EnumSet<ICodeNodeTypeImpl> ARITH_OPS =
		EnumSet.of(ADD,SUBTRACT,MULTIPLY,FLOAT_DIVIDE,INTEGER_DIVIDE);

	/**
	* Constructor.
	*
	* @param parent the parent executor
	*/
	public ExpressionExecutor(AssignmentExecutor parent)
	{
		super(parent);
	}

	/**
	* Execute an expression.
	*
	* @param node the root intermediate code node of the compound
	* statement
	* @return the computed value
	*/
	@Override
	public Object execute(ICodeNode node)
	{
		ICodeNodeTypeImpl nodeType =
			(ICodeNodeTypeImpl) node.getType();

		if (nodeType == VARIABLE) {

			// Get the variable's symbol table entry and return its
			// value.
			SymTabEntry entry = (SymTabEntry) node.getAttribute(ID);
			return entry.getAttribute(DATA_VALUE);
		}

		else if (nodeType == INTEGER_CONSTANT) {
			return (Integer) node.getAttribute(VALUE);
		}

		else if (nodeType == REAL_CONSTANT) {
			return (Float) node.getAttribute(VALUE);
		}

		else if (nodeType == STRING_CONSTANT) {
			return (String) node.getAttribute(VALUE);
		}

		else if (nodeType == NEGATE) {
			// Get the NEGATE node's expression node child.
			ArrayList<ICodeNode> children = node.getChildren();
			ICodeNode expressionNode = children.get(0);

			// Execute the expression and return the negative of its
			// value.
			Object value = execute(expressionNode);
			if (value instanceof Integer) {
				return -((Integer) value);
			}

			else {
				return -((Float) value);
			}
		}

		else if (nodeType == NOT) {
			// Get the NOT node's expression node child.
			ArrayList<ICodeNode> children = node.getChildren();
			ICodeNode expressionNode = children.get(0);

			// Execute the expression and return the "not" of its
			// value.
			boolean value = (Boolean) execute(expressionNode);
			return !value;
		}

		// Must be a binary operator.
		else {
			return executeBinaryOperator(node,nodeType);
		}
	}

	/**
	* Execute binary operator.
	* @param node the root node of the expression
	* @param nodeType the node type
	* @return the computed value of the expression
	*/
	private Object executeBinaryOperator(
		ICodeNode node,ICodeNodeTypeImpl nodeType)
	{
		// Get the two operand children of the operator node.
		ArrayList<ICodeNode> children = node.getChildren();
		ICodeNode operandNode1 = children.get(0);
		ICodeNode operandNode2 = children.get(1);

		Object operand1 = execute(operandNode1);
		Object operand2 = execute(operandNode2);

		boolean integerMode = (operand1 instanceof Integer) &&
			(operand2 instanceof Integer);

		// ====================
		// Arithmetic Operators
		// ====================

		if (ARITH_OPS.contains(nodeType)) {
			if (integerMode) {
				int value1 = (Integer) operand1;
				int value2 = (Integer) operand2;

				// Integer operations.
				if (nodeType == ADD) {
					return value1 + value2;
				}

				else if (nodeType == SUBTRACT) {
					return value1 - value2;
				}

				else if (nodeType == MULTIPLY) {
					return value1 * value2;
				}

				else if (nodeType == FLOAT_DIVIDE) {

					// Check for division by 0.
					if (value2 != 0) {
						return (float) value1 / (float) value2;
					}

					else {
						errorHandler.flag(node,DIVISION_BY_ZERO,this);
						return 0;
					}
				}

				else if (nodeType == INTEGER_DIVIDE) {

					// Check for division by 0.
					if (value2 != 0) {
						return value1 / value2;
					}

					else {
						errorHandler.flag(node,DIVISION_BY_ZERO,this);
						return 0;
					}
				}

				else if (nodeType == MOD) {

					// Check for division by 0.
					if (value2 != 0) {
						return value1 % value2;
					}

					else {
						errorHandler.flag(node,DIVISION_BY_ZERO,this);
						return 0;
					}
				}
			}

			else {
				float value1 = operand1 instanceof Integer ?
					(Integer) operand1 : (Float) operand1;
				float value2 = operand2 instanceof Integer ?
					(Integer) operand2 : (Float) operand2;

				// Float operations.
				if (nodeType == ADD) {
					return value1 + value2;
				}

				else if (nodeType == SUBTRACT) {
					return value1 - value2;
				}

				else if (nodeType == MULTIPLY) {
					return value1 * value2;
				}

				else if (nodeType == FLOAT_DIVIDE) {

					// Check for division by 0.
					if (value2 != 0.0f) {
						return value1 / value2;
					}

					else {
						errorHandler.flag(node,DIVISION_BY_ZERO,this);
						return 0.0f;
					}
				}
			}
		}

		// ==========
		// AND and OR
		// ==========

		else if (nodeType == AND || nodeType == OR) {
			boolean value1 = (Boolean) operand1;
			boolean value2 = (Boolean) operand2;

			if (nodeType == AND) {
				return value1 && value2;
			}

			else if (nodeType == OR) {
				return value1 || value2;
			}
		}


		// ====================
		// Relational operators
		// ====================

		else if (integerMode) {
			int value1 = (Integer) operand1;
			int value2 = (Integer) operand2;

			if (nodeType == EQ) {
				return value1 == value2;
			}

			else if (nodeType == NE) {
				return value1 != value2;
			}

			else if (nodeType == LT) {
				return value1 < value2;
			}

			else if (nodeType == LE) {
				return value1 <= value2;
			}

			else if (nodeType == GT) {
				return value1 > value2;
			}

			else if (nodeType == GE) {
				return value1 >= value2;
			}
		}

		else {
			float value1 = operand1 instanceof Integer ?
				(Integer) operand1 : (Float) operand1;
			float value2 = operand2 instanceof Integer ?
				(Integer) operand2 : (Float) operand2;

			if (nodeType == EQ) {
				return value1 == value2;
			}

			else if (nodeType == NE) {
				return value1 != value2;
			}

			else if (nodeType == LT) {
				return value1 < value2;
			}

			else if (nodeType == LE) {
				return value1 <= value2;
			}

			else if (nodeType == GT) {
				return value1 > value2;
			}

			else if (nodeType == GE) {
				return value1 >= value2;
			}
		}

		// XXX But we would never know if we did.  Maybe an error
		// should be flagged?
		return 0; // Should never get here.
	}

}

