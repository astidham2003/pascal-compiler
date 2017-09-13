package wci.frontend.pascal.parsers;

import java.util.EnumSet;
import java.util.HashMap;

import wci.intermediate.ICodeNode;
import wci.intermediate.ICodeNodeType;
import wci.intermediate.ICodeFactory;

import wci.frontend.Token;
import wci.frontend.TokenType;

import wci.frontend.pascal.PascalParserTD;

import wci.frontend.pascal.PascalTokenType;

import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.*;

import static wci.frontend.pascal.PascalTokenType.*;

/**
* <h1>ExpressionParser</h1>
*/
public class ExpressionParser extends StatementParser
{
	private static final EnumSet<PascalTokenType> REL_OPS =
		EnumSet.of(EQUALS,NOT_EQUALS,LESS_THAN,LESS_EQUALS,
			GREATER_THAN,GREATER_EQUALS);
	private static final HashMap<PascalTokenType,ICodeNodeType>
		REL_OPS_MAP = new HashMap<PascalTokenType,ICodeNodeType>();


	static
	{
		REL_OPS_MAP.put(EQUALS,EQ);
		REL_OPS_MAP.put(NOT_EQUALS,NE);
		REL_OPS_MAP.put(LESS_THAN,LT);
		REL_OPS_MAP.put(LESS_EQUALS,LE);
		REL_OPS_MAP.put(GREATER_THAN,GT);
		REL_OPS_MAP.put(GREATER_EQUALS,GE);
	}

	public ExpressionParser(PascalParserTD parent)
	{
		super(parent);
	}

	/**
	* Parse an expression.
	*
	* @param token the initial token
	* @return the root node of the generated parse tree
	* @throws Exception
	*/
	public ICodeNode parse(Token token) throws Exception
	{
		return parseExpression(token);
	}

	/**
	* Parse an expression.
	*
	* @param token the intitial token
	* @return the root of the generated parse subtree
	* @throws Exception
	*/
	private ICodeNode parseExpression(Token token) throws Exception
	{
		// Parse a simple expression and make the root of its tree
		// the root node.
		ICodeNode rootNode = parseSimpleExpression(token);

		token = currentToken();
		TokenType tokenType = token.getType();

		// Look for a relational operator.
		if (REL_OPS.contains(tokenType)) {
			// Create a new operator node and adopt the current tree
			// as its first child.
			ICodeNodeType nodeType = REL_OPS_MAP.get(tokenType);
			ICodeNode opNode =
				ICodeFactory.createICodeNode(nodeType);
			opNode.addChild(rootNode);

			token = nextToken(); // Consume the operator.

			// Parse the second simple expression.  The operator
			// node adopts the simple expression's tree as its
			// second child.
			opNode.addChild(parseSimpleExpression(token));

			// The operator node becomes the new root node.
			rootNode = opNode;
		}

		return rootNode;
	}

	/**
	* Parse a simple expression.
	*
	* @param token the initial token
	* @return the root of the generated parse subtree
	* @throws Exception
	*/
	private ICodeNode parseSimpleExpression(Token token)
	{
		return (ICodeNode) null;
	}

}

