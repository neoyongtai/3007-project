package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import lexer.Lexer;

import org.junit.Test;

import frontend.Token;
import frontend.Token.Type;
import static frontend.Token.Type.*;

/**
 * This class contains unit tests for your lexer. Currently, there is only one test, but you
 * are strongly encouraged to write your own tests.
 */
public class LexerTests {
	// helper method to run tests; no need to change this
	private final void runtest(String input, Token... output) {
		Lexer lexer = new Lexer(new StringReader(input));
		int i=0;
		Token actual=new Token(MODULE, 0, 0, ""), expected;
		try {
			do {
				assertTrue(i < output.length);
				expected = output[i++];
				try {
					actual = lexer.nextToken();
					assertEquals(expected, actual);
				} catch(Error e) {
					if(expected != null)
						fail(e.getMessage());
					/* return; */
				}
			} while(!actual.isEOF());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/** Example unit test. */
	@Test
	public void testKWs() {
		// first argument to runtest is the string to lex; the remaining arguments
		// are the expected tokens
		runtest("module false return while",
				new Token(MODULE, 0, 0, "module"),
				new Token(FALSE, 0, 7, "false"),
				new Token(RETURN, 0, 13, "return"),
				new Token(WHILE, 0, 20, "while"),
				new Token(EOF, 0, 25, ""));
	}

	@Test
	public void testStringLiteralWithDoubleQuote() {
		runtest("\"\"\"",
				new Token(STRING_LITERAL, 0, 0, ""),
				(Token)null,
				new Token(EOF, 0, 3, ""));
	}

	@Test
	public void testStringLiteral() {
		runtest("\"\\n\"", 
				new Token(STRING_LITERAL, 0, 0, "\\n"),
				new Token(EOF, 0, 4, ""));
	}
	
	@Test
	public void testKeywords() {
		runtest("boolean break else false if import int module public return true type void while",
				new Token(BOOLEAN, 0, 0, "boolean"),
				new Token(BREAK, 0, 8, "break"),
				new Token(ELSE, 0, 14, "else"),
				new Token(FALSE, 0, 19, "false"),
				new Token(IF, 0, 25, "if"),
				new Token(IMPORT, 0, 28, "import"),
				new Token(INT, 0, 35, "int"),
				new Token(MODULE, 0, 39, "module"),
				new Token(PUBLIC, 0, 46, "public"),
				new Token(RETURN, 0, 53, "return"),
				new Token(TRUE, 0, 60, "true"),
				new Token(TYPE, 0, 65, "type"),
				new Token(VOID, 0, 70, "void"),
				new Token(WHILE, 0, 75, "while"),
				new Token(EOF, 0, 80, ""));
	}
	
	@Test
	public void testPunctuation() {
		runtest(",[{(]});",
				new Token(COMMA, 0, 0, ","),
				new Token(LBRACKET, 0, 1, "["),
				new Token(LCURLY, 0, 2, "{"),
				new Token(LPAREN, 0, 3, "("),
				new Token(RBRACKET, 0, 4, "]"),
				new Token(RCURLY, 0, 5, "}"),
				new Token(RPAREN, 0, 6, ")"),
				new Token(SEMICOLON, 0, 7, ";"),
				new Token(EOF, 0, 8, ""));
	}
	
	@Test
	public void testOperators() {
		runtest("/===>=><=<-!=+*",
				new Token(DIV, 0, 0, "/"),
				new Token(EQEQ, 0, 1, "=="),
				new Token(EQL, 0, 3, "="),
				new Token(GEQ, 0, 4, ">="),
				new Token(GT, 0, 6, ">"),
				new Token(LEQ, 0, 7, "<="),
				new Token(LT, 0, 9, "<"),
				new Token(MINUS, 0, 10, "-"),
				new Token(NEQ, 0, 11, "!="),
				new Token(PLUS, 0, 13, "+"),
				new Token(TIMES, 0, 14, "*"),
				new Token(EOF, 0, 15, ""));
	}
	
	@Test
	public void testEqual() {
		runtest("== = == ==== ===",
				new Token(EQEQ, 0, 0, "=="),
				new Token(EQL, 0, 3, "="),
				new Token(EQEQ, 0, 5, "=="),
				new Token(EQEQ, 0, 8, "=="),
				new Token(EQEQ, 0, 10, "=="),
				new Token(EQEQ, 0, 13, "=="),
				new Token(EQL, 0, 15, "="),
				new Token(EOF, 0, 16, ""));
	}
	
	@Test
	public void testGEQ() {
		runtest(">==",
				new Token(GEQ, 0, 0, ">="),
				new Token(EQL, 0, 2, "="),
				new Token(EOF, 0, 3, ""));
	}
	
	@Test
	public void testID() {
		runtest("a a_ 8 A8 A8_",
				new Token(ID, 0, 0, "a"),
				new Token(ID, 0, 2, "a_"),
				(Token)null,
				new Token(ID, 0, 7, "A8"),
				new Token(ID, 0, 10, "A8_"),
				new Token(EOF, 0, 13, ""));
	}
	
	@Test
	public void testINT() {
		runtest("123 12 0 -10",
				new Token(INT_LITERAL, 0, 0, "123"),
				new Token(INT_LITERAL, 0, 4, "12"),
				new Token(INT_LITERAL, 0, 7, "0"),
				(Token)null,
				new Token(INT_LITERAL, 0, 10, "10"),
				new Token(EOF, 0, 12, ""));
	}
	
	@Test
	public void testSTRING() {
		runtest("asd \"asd\" \"\\n\"",
				new Token(ID, 0, 0, "asd"),
				new Token(STRING_LITERAL, 0, 4, "asd"),
				(Token)null,
				new Token(EOF, 0, 14, ""));
	}
	
	@Test
	public void testNewline() {
		runtest("ID ID2\nID3 ID4",
				new Token(ID, 0, 0, "ID"),
				new Token(ID, 0, 3, "ID2"),
				new Token(ID, 1, 0, "ID3"),
				new Token(ID, 1, 4, "ID4"),
				new Token(EOF, 1, 7, ""));
	}
	
	@Test
	public void testDigitWithLetter() {
		runtest("123a",
				new Token(INT_LITERAL, 0, 0, "123"),
				new Token(ID, 0, 3, "a"),
				new Token(EOF, 0, 4, ""));
	}
	
	@Test
	public void testSpacing() {
		runtest("a   a",
				new Token(ID, 0, 0, "a"),
				new Token(ID, 0, 4, "a"),
				new Token(EOF, 0, 5, ""));
	}
}
