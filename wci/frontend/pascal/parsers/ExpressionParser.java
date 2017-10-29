package wci.frontend.pascal.parsers;

import java.util.EnumSet;
import java.util.HashMap;

import wci.intermediate.ICodeNode;
import wci.intermediate.ICodeNodeType;
import wci.intermediate.ICodeFactory;
import wci.intermediate.SymTabEntry;


import wci.intermediate.icodeimpl.ICodeNodeTypeImpl;
import wci.intermediate.icodeimpl.ICodeKeyImpl;

import wci.frontend.Token;
import wci.frontend.TokenType;

import wci.frontend.pascal.PascalParserTD;
import wci.frontend.pascal.PascalTokenType;

import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.*;
import static wci.intermediate.icodeimpl.ICodeKeyImpl.*;

import static wci.frontend.pascal.PascalTokenType.*;

import static wci.frontend.pascal.PascalErrorCode.IDENTIFIER_UNDEFINED;
import static wci.frontend.pascal.PascalErrorCode.MISSING_RIGHT_PAREN;
import static wci.frontend.pascal.PascalErrorCode.UNEXPECTED_TOKEN;

/**
* <h1>ExpressionParser</h1>
*
* <p>Root parser for expressions.</p>
*/
public class ExpressionParser extends StatementParser
{

	// Set of relational tokens.
	private static final EnumSet<PascalTokenType> REL_OPS;
	// Map relational operator tokens to node types.
	private static final HashMap<PascalTokenType,ICodeNodeType>
		REL_OPS_MAP;

	// Set of additive tokens.
	private static final EnumSet<PascalTokenType> ADD_OPS;
	// Map additive operator tokens to node types.
	private static final HashMap<PascalTokenType,ICodeNodeType>
		ADD_OPS_MAP;

	// Set of multiplicative tokens.
	private static final EnumSet<PascalTokenType> MULT_OPS;
	// Map multiplicative tokens to node types.
	private static final HashMap<PascalTokenType,ICodeNodeType>
		MULT_OPS_MAP;

	static final EnumSet<PascalTokenType> EXPR_START_SET;

	static
	{
		REL_OPS =
			EnumSet.of(EQUALS,NOT_EQUALS,LESS_THAN,LESS_EQUALS,
				GREATER_THAN,GREATER_EQUALS);
		ADD_OPS = EnumSet.of(PLUS,MINUS,PascalTokenType.OR);
		MULT_OPS =
			EnumSet.of(STAR,SLASH,DIV,PascalTokenType.MOD,
				PascalTokenType.AND);
		EXPR_START_SET =
			EnumSet.of(PLUS,MINUS,IDENTIFIER,INTEGER,REAL,STRING,
				PascalTokenType.NOT,LEFT_PAREN);

		REL_OPS_MAP = new HashMap<PascalTokenType,ICodeNodeType>();
		REL_OPS_MAP.put(EQUALS,EQ);
		REL_OPS_MAP.put(NOT_EQUALS,NE);
		REL_OPS_MAP.put(LESS_THAN,LT);
		REL_OPS_MAP.put(LESS_EQUALS,LE);
		REL_OPS_MAP.put(GREATER_THAN,GT);
		REL_OPS_MAP.put(GREATER_EQUALS,GE);

		ADD_OPS_MAP = new HashMap<PascalTokenType,ICodeNodeType>();
		ADD_OPS_MAP.put(PLUS,ADD);
		ADD_OPS_MAP.put(MINUS,SUBTRACT);
		ADD_OPS_MAP.put(
			PascalTokenType.OR,ICodeNodeTypeImpl.OR);

		MULT_OPS_MAP = new HashMap<PascalTokenType,ICodeNodeType>();
		MULT_OPS_MAP.put(STAR,MULTIPLY);
		MULT_OPS_MAP.put(SLASH,FLOAT_DIVIDE);
		MULT_OPS_MAP.put(DIV,INTEGER_DIVIDE);
		MULT_OPS_MAP.put(
			PascalTokenType.AND,ICodeNodeTypeImpl.AND);
		MULT_OPS_MAP.put(
			PascalTokenType.MOD,ICodeNodeTypeImpl.MOD);
	}

	public ExpressionParser(PascalParserTD parent)
	{
		super(parent);
	}

	/**
	* Parse an expression.  An expression is a simple expression.  Or
	* a simple expression followed by a relational operator
	* (=,<>,<,<=,>,>=) followed by a simple expression.
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
	* Parse a simple expression.  A simple expression is a
	* an optional sign (+ or -) followed by a term.
	* Or an optional sign (+ or -) followed by a term followed
	* by a either +,-,or OR followed by a term.
	*
	* @param token the initial token
	* @return the root of the generated parse subtree
	* @throws Exception
	*/
	private ICodeNode parseSimpleExpression(Token token)
		throws Exception
	{
		TokenType signType = null; // Type of leading sign if any.

		// Look for a leading + or - sign.
		TokenType tokenType = token.getType();
		if (tokenType == PLUS || tokenType == MINUS) {
			signType = tokenType;
			token = nextToken(); // Consume the + or -.
		}

		// Parse a term and make the root of its tree the root node.
		ICodeNode rootNode = parseTerm(token);

		// Was there a leading - sign?
		if (signType == MINUS) {
			// Create a NEGATE node and adopt the current tree
			// as its child.  The NEGATE node becomes the new root
			// node.
			ICodeNode negateNode =
				ICodeFactory.createICodeNode(NEGATE);
			negateNode.addChild(rootNode);
			rootNode = negateNode;
		}

		token = currentToken();
		tokenType = token.getType();

		// Loop over additive operators.
		while (ADD_OPS.contains(tokenType)) {
			// XXX Current root node will become the left subtree.
			// XXX Node created after opNode will become right subtree.
			// XXX opNode becomes the new root node.
			// XXX And the simple expression parse subtree is thus
			// XXX made.

			// Create a new operator node and adopt the current tree
			// as its first child.
			ICodeNodeType nodeType = ADD_OPS_MAP.get(tokenType);
			ICodeNode opNode =
				ICodeFactory.createICodeNode(nodeType);
			opNode.addChild(rootNode);

			token = nextToken(); // Consume the operator.

			// Parse another term.  The operator node adopts
			// the term's tree as its second child.
			opNode.addChild(parseTerm(token));

			// The operator node becomes the root node.
			rootNode = opNode;

			token = currentToken();
			tokenType = token.getType();
		}

		return rootNode;
	}

	/**
	* Parse a term.  A term is a factor, or a factor followed by any of
	* of the following: *,/,DIV,MOD,AND followed by another factor.
	*
	* @param token the initial token
	* @return the root node of the parse subtree
	* @throws Exception
	*/
	private ICodeNode parseTerm(Token token) throws Exception
	{
		// Parse a factor and make its node the root node.
		ICodeNode rootNode = parseFactor(token);

		token = currentToken();
		TokenType tokenType = token.getType();

		// Loop over multiplicative operators.
		while (MULT_OPS.contains(tokenType)) {
			// XXX Current root node will become the left subtree.
			// XXX Node created after opNode will become right subtree.
			// XXX opNode becomes the new root node.
			// XXX And the term parse subtree is thus
			// XXX made.
			// Create a new operator node and adopt the current tree
			// as its first child.
			ICodeNodeType nodeType = MULT_OPS_MAP.get(tokenType);
			ICodeNode opNode = ICodeFactory.createICodeNode(nodeType);
			opNode.addChild(rootNode);

			token = nextToken(); // Consume the operator.

			// Parse another factor.  The operator node adopts
			// the term's tree as its second child.
			opNode.addChild(parseFactor(token));

			// The op node becomes the root node.
			rootNode = opNode;

			token = currentToken();
			tokenType = token.getType();
		}

		return rootNode;
	}

	/**
	* Parse a factor.  A factor is a variable, or number, or string,
	* or NOT followed by another factor, or an expression between ().
	*
	* @param token the initial token
	* @return the root node of the parse subtree
	* @throws Exception
	*/
	private ICodeNode parseFactor(Token token) throws Exception
	{
		ICodeNode rootNode = null;
		TokenType tokenType = token.getType();

		if((PascalTokenType) tokenType == IDENTIFIER) {
			// Look up the identifier in the symbol table stack.
			// Flag the identifier as undefined if it's not found.
			String name = token.getText().toLowerCase();
			SymTabEntry id = symTabStack.lookup(name);

			if (id == null) {
				errorHandler.flag(token,IDENTIFIER_UNDEFINED,this);
				id = symTabStack.enterLocal(name);
			}

			rootNode = ICodeFactory.createICodeNode(VARIABLE);
			rootNode.setAttribute(ID,id);
			id.appendLineNumber(token.getLineNumber());

			token = nextToken(); // Consume the identifier.
		}

		else if((PascalTokenType) tokenType == INTEGER) {
			// Create an integer constant as the root node.
			rootNode = ICodeFactory.createICodeNode(INTEGER_CONSTANT);
			rootNode.setAttribute(VALUE,token.getValue());

			token = nextToken(); // Consume the integer.
		}

		else if((PascalTokenType) tokenType == REAL) {
			rootNode = ICodeFactory.createICodeNode(REAL_CONSTANT);
			rootNode.setAttribute(VALUE,token.getValue());

			token = nextToken(); // Consume the real.
		}

		else if((PascalTokenType) tokenType == STRING) {
			rootNode = ICodeFactory.createICodeNode(STRING_CONSTANT);
			rootNode.setAttribute(VALUE,token.getValue());

			token = nextToken(); // Consume the string.
		}

		else if((PascalTokenType) tokenType ==
				PascalTokenType.NOT) {
			// Create a NOT node as the root node.
			rootNode = ICodeFactory.createICodeNode(
				ICodeNodeTypeImpl.NOT);

			// Parse the factor.  The NOT node adopts the
			// factor node as its child.
			rootNode.addChild(parseFactor(token));
		}

		else if((PascalTokenType) tokenType == LEFT_PAREN) {
			token = nextToken(); // Consume the (.

			// Parse an expression and make its node the root node.
			rootNode = parseExpression(token);

			token = currentToken();
			if ((PascalTokenType) token.getType() == RIGHT_PAREN) {
				token = nextToken(); // Consume the ).
			}
			else
			{
				errorHandler.flag(token,MISSING_RIGHT_PAREN,this);
			}
		}

		else {
			errorHandler.flag(token,UNEXPECTED_TOKEN,this);
		}

		return rootNode;
	}
}

