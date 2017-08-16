package wci.frontend.pascal.tokens;

import wci.frontend.pascal.*;
import wci.frontend.*;

import static wci.frontend.pascal.PascalErrorCode.*;
import static wci.frontend.pascal.PascalTokenType.*;
import static java.lang.Float.MAX_EXPONENT;

public class PascalNumberToken extends PascalToken
{

	public PascalNumberToken(Source source) throws Exception
	{
		super(source);
	}

	// ----------------------------------------------------------------
	// Token methods

	/**
	* Extract a Pascal number token from the source.
	*
	* @throws Exception
	*/
	protected void extract() throws Exception
	{
		StringBuilder textBuffer = new StringBuilder();
		extractNumber(textBuffer);
		text = textBuffer.toString();
	}

	/**
	* Extract a Pascal number token from the source.
	*
	* @param textBuffer the buffer to append the token's characters.
	* @throws Exception
	*/
	private void extractNumber(
		StringBuilder textBuffer) throws Exception
	{
		String wholeDigits = null;
		String fractionDigits = null;
		String exponentDigits = null;
		char exponentSign = '+';
		boolean sawDotDot = false;
		char currentChar;

		type = INTEGER; // Assume INTEGER for now.

		// Extract the digits of the whole part of the number.
		wholeDigits = unsignedIntegerDigits(textBuffer);
		if (type == ERROR) {
			return;
		}

		// Is there a .?
		// Could be decimal or start of .. token.
		currentChar = currentChar();
		if (currentChar == '.') {

			if (peekChar() == '.') {
				sawDotDot = true; // Don't consume .. token.
			}

			else {
				type = REAL;
				textBuffer.append(currentChar);
				currentChar = nextChar();

				// Collect the digits of the fraction part of the
				// number.
				fractionDigits = unsignedIntegerDigits(textBuffer);
				if (type == ERROR) {
					return;
				}
			}
		}

		// Is there an exponent?
		// There can't be an exponent if we already saw a .. token.
		// XXX Why is this being called a second time?
		currentChar = currentChar();
		if (!sawDotDot &&
				(currentChar == 'E' || currentChar == 'e')) {
			type = REAL;
			textBuffer.append(currentChar);
			currentChar = nextChar();

			// Exponent sign?
			if (currentChar == '+' || currentChar == '-') {
				textBuffer.append(currentChar);
				exponentSign = currentChar;
				currentChar = nextChar();
			}

			// Extract the digits of the exponent.
			exponentDigits = unsignedIntegerDigits(textBuffer);
		}

		// Compute the value of an integer number token.
		if (type == INTEGER) {
			int integerValue = computeIntegerValue(wholeDigits);

			if (type != ERROR) {
				value = new Integer(integerValue);
			}
		}

		// Compute the value of a real number token.
		else if (type == REAL) {
			float floatValue = computeFloatValue(
				wholeDigits,fractionDigits,
				exponentDigits,exponentSign);

			if (type != ERROR) {
				value = new Float(floatValue);
			}
		}

	}

	// ----------------------------------------------------------------

	/**
	* Extract and return the digits of an unsigned integer.
	*
	* @param textBuffer the buffer to append the token's characters
	* @return the string of digits or null
	* @throws Exception
	*/
	private String unsignedIntegerDigits(
		StringBuilder textBuffer) throws Exception
	{
		char currentChar = currentChar();

		// Must have at least one digit.
		if (!Character.isDigit(currentChar)) {
			type = ERROR;
			value = INVALID_NUMBER;
			return null;
		}

		// Extract the digits.
		StringBuilder digits = new StringBuilder();
		while (Character.isDigit(currentChar)) {
			textBuffer.append(currentChar);
			digits.append(currentChar);
			currentChar = nextChar();
		}

		return digits.toString();
	}


	/**
	* Compute and return the integer value of a string of digits.
	* Check for overflow.
	*
	* @param digits the string of digits
	* @return the integer value
	*/
	private int computeIntegerValue(String digits)
	{
		if (digits == null) {
			return 0;
		}


		int integerValue = 0;
		int prevValue = -1;
		int index = 0;

		// Loop over the digits to compute the integer value
		// as long as there is no overflow.
		while (index < digits.length() && integerValue >= prevValue) {
			prevValue = integerValue;
			integerValue = 10 * integerValue +
				Character.getNumericValue(digits.charAt(index++));
		}

		// No overflow: return the integer value.
		// XXX integerValue will be negative if it overflows.
		if (integerValue >= prevValue) {
			return integerValue;
		}

		// Overflow: set the integer out of range error.
		else {
			type = ERROR;
			value = RANGE_INTEGER;
			return 0;
		}
	}


	/**
	* Compute and return the float value of a real number.
	*
	* @param wholeDigits the string of digits before the decimal
	* @param fractionalDigits the string of digits after the decimal
	* @param exponentDigits the string of digits after the exponent
	* @param exponentSign the sign of the exponents
	* @return the float value
	*/
	private float computeFloatValue(
		String wholeDigits,String fractionalDigits,
		String exponentDigits,char exponentSign)
	{
		double floatValue = 0.0;
		int exponentValue = computeIntegerValue(exponentDigits);
		String digits = wholeDigits; // Whole and fractional digits.

		// Negate the exponent if the sign is '-'.
		if (exponentSign == '-') {
			exponentValue = -exponentValue;
		}

		// If there are any fraction digits, adjust the exponent
		// value and append the fraction digits.
		if (fractionalDigits != null) {
			exponentValue -= fractionalDigits.length();
			digits += fractionalDigits;
		}

		// Check for a real number out of range error.
		if (Math.abs(exponentValue + wholeDigits.length()) >
				MAX_EXPONENT) {
			type = ERROR;
			value = RANGE_REAL;
			return 0.0f;
		}

		// Loop over the digits to compute the float value.
		int index = 0;
		while (index < digits.length()) {
			floatValue = 10 * floatValue +
				Character.getNumericValue(digits.charAt(index++));
		}

		// Adjust the float value based on the exponent value.
		if (exponentValue != 0) {
			floatValue *= Math.pow(10,exponentValue);
		}

		return (float) floatValue;
	}

}

