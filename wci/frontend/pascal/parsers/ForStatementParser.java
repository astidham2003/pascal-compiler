package wci.frontend.pascal.parsers;

import java.util.EnumSet;

import wci.intermediate.ICodeNode;
import wci.intermediate.ICodeFactory;

import wci.frontend.Token;
import wci.frontend.TokenType;

import wci.frontend.pascal.PascalTokenType;

import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.TEST;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.LOOP;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.COMPOUND;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.GT;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.LT;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.ADD;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.SUBTRACT;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.ASSIGN;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.
	INTEGER_CONSTANT;

import static wci.intermediate.icodeimpl.ICodeKeyImpl.VALUE;

import static wci.frontend.pascal.PascalTokenType.TO;
import static wci.frontend.pascal.PascalTokenType.DOWNTO;
import static wci.frontend.pascal.PascalTokenType.DO;

import static wci.frontend.pascal.PascalErrorCode.MISSING_TO_DOWNTO;
import static wci.frontend.pascal.PascalErrorCode.MISSING_DO;


/**
* <h1>ForStatementParser</h1>
*
* <p>Root parser for FOR statements.</p>
*/
public class ForStatementParser extends StatementParser
{

	// Synchronization set for TO or DOWNTO.
	static final EnumSet<PascalTokenType> TO_DOWNTO_SET;

	// Synchroniztion set for DO.
	private static final EnumSet<PascalTokenType> DO_SET;

	static
	{
		TO_DOWNTO_SET = ExpressionParser.EXPR_START_SET.clone();
		TO_DOWNTO_SET.add(TO);
		TO_DOWNTO_SET.add(DOWNTO);
		// PLUS,MINUS,IDENTIFIER,INTEGER,REAL,STRING,TO,DOWNTO,
		// SEMICOLON,END,ELSE,UNTIL,DOT
		TO_DOWNTO_SET.addAll(StatementParser.STMT_FOLLOW_SET);

		DO_SET = StatementParser.STMT_START_SET.clone();
		DO_SET.add(DO);
		// BEGIN,CASE,FOR,IF,REPEAT,WHILE,IDENTIFIER,SEMICOLON,DO,
		// SEMICOLON,END,ELSE,UNTIL,DOT
		DO_SET.addAll(StatementParser.STMT_FOLLOW_SET);
	}

	/**
	* Constructor.
	*
	* @param parent the parent parser
	*/
	public ForStatementParser(StatementParser parent)
	{
		super(parent);
	}

	// ----------------------------------------------------------------
	// StatementParser methods

	/**
	* Parse a FOR statement.
	*
	* @param token the initial FOR statemnt token.
	* @return the root node of the parse subtree
	* @throws Exception
	*/
	@Override
	public ICodeNode parse(Token token) throws Exception
	{
		// Consume the initial token.
		token = nextToken();
		Token targetToken = token;

		// Create the loop COMPOUND, LOOP, and TEST nodes.
		ICodeNode compoundNode =
			ICodeFactory.createICodeNode(COMPOUND);
		ICodeNode loopNode = ICodeFactory.createICodeNode(LOOP);
		ICodeNode testNode = ICodeFactory.createICodeNode(TEST);

		// Parse the embedded initial assignment.
		AssignmentStatementParser assignmentParser =
			new AssignmentStatementParser(this);
		ICodeNode initAssignNode = assignmentParser.parse(token);

		// Set the current line number attribute.
		setLineNumber(initAssignNode,targetToken);

		// The COMPOUND node adopts the initial ASSIGN and the loop
		// nodes as its first and second children.
		compoundNode.addChild(initAssignNode);
		compoundNode.addChild(loopNode);

		// Synchronize at the TO or DOWNTO.
		token = synchronize(TO_DOWNTO_SET);
		TokenType direction = token.getType();

		if (direction == TO || direction == DOWNTO) {
			token = nextToken(); // Consume the TO orDOWNTO.
		}
		else {
			direction = TO;
			errorHandler.flag(token,MISSING_TO_DOWNTO,this);
		}

		// Create a relational operator node: GT for TO or
		// LT for DOWNTO.
		ICodeNode relOpNode = ICodeFactory.
			createICodeNode(direction == TO ? GT : LT);

		// Copy the control variable node.  The relationalal operator
		// adopts the copied VARIABLE node as its first child.
		ICodeNode controlVarNode = initAssignNode.getChildren().get(0);
		relOpNode.addChild(controlVarNode.copy());

		// Parse the termination expression.  The relational operator
		// node adopts the expression as its second child.
		ExpressionParser expressionParser = new ExpressionParser(this);
		relOpNode.addChild(expressionParser.parse(token));

		// The TEST node adopts the relational operator node as its
		// only child.  The LOOP node adopts the TEST node node its
		// first child.
		testNode.addChild(relOpNode);
		loopNode.addChild(testNode);

		// Synchronize at the DO.
		token = synchronize(DO_SET);
		if (token.getType() == DO) {
			token = nextToken(); // Consume the DO.
		}
		else {
			errorHandler.flag(token,MISSING_DO,this);
		}

		// Parse the nested statement.  The LOOP node adopts the
		// statement node as its second child.
		StatementParser statementParser = new StatementParser(this);
		loopNode.addChild(statementParser.parse(token));

		// Create an assignment with a copy of the control variable
		// to advance the value of the variable.
		ICodeNode nextAssignNode =
			ICodeFactory.createICodeNode(ASSIGN);
		nextAssignNode.addChild(controlVarNode.copy());

		// Create the arithmetic operator node:
		// ADD for TO, or SUBTRACT for DOWNTO.
		ICodeNode arithOpNode =
			ICodeFactory.createICodeNode(
				direction == TO ? ADD : SUBTRACT);

		// The operator node adopts a copy of the loop variable node
		// as its first child and the value 1 as its second child.
		arithOpNode.addChild(controlVarNode.copy());
		ICodeNode oneNode =
			ICodeFactory.createICodeNode(INTEGER_CONSTANT);
		oneNode.setAttribute(VALUE,1);
		arithOpNode.addChild(oneNode);

		// The next assign node adopts the arithmetic operator as its
		// second child.  The loop node adopts the next ASSIGN node as
		// its third child.
		nextAssignNode.addChild(arithOpNode);
		loopNode.addChild(nextAssignNode);

		// Set the current line number attribute.
		setLineNumber(nextAssignNode,targetToken);

		return compoundNode;
	}
}

