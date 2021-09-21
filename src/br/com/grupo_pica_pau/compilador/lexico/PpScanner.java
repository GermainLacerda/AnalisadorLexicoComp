package br.com.grupo_pica_pau.compilador.lexico;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import br.com.grupo_pica_pau.compilador.exceptions.Token;

public class PpScanner {

	private char[] content;
	private int estado;
	private int pos;

	public PpScanner(String filename) {
		try {
			String txtConteudo;
			txtConteudo = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
			System.out.println("DEBUG ----");
			System.out.println(txtConteudo += " ");
			System.out.println("----------");
			content = txtConteudo.toCharArray();
			pos = 0;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * private boolean isFloat(char c, char v) { return (c >= '0' && c <= '9') && (v
	 * >= '0' && v <= '9'); }
	 */

	private boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

	private boolean isChar(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
	}

	private boolean isOp_Rel(char c) {

		return c == '<' || c == '>' || c == '=' || c == '!';
	}

	private boolean isOp_Ari(char c) {
		return c == '+' || c == '-' || c == '*' || c == '/';
	}

	private boolean isCaracter_esp(char c) {
		return c == ')' || c == '(' || c == '{' || c == '}' || c == ',' || c == ';';
	}

	private boolean isEOF() {
		return pos == content.length;
	}

	private char nextChar() {
		return content[pos++];
	}

	private boolean isSpace(char c) {
		return c == ' ' || c == '\t' || c == '\n' || c == '\r';
	}

	private void back() {
		pos--;
	}

	public Token nextToken() {
		char currentChar = nextChar();
		String term = "";
		if (isEOF()) {
			return null;
		}
		estado = 0;
		while (true) {
			switch (estado) {
				case 0:
					if (isChar(currentChar)) {
						term += currentChar;
						estado = 1;
					} else if (isDigit(currentChar)) {
						estado = 3;
						term += currentChar;
						
					} else if (isSpace(currentChar)) {
						estado = 0;
					} else if (isOp_Rel(currentChar)) {
						estado = 2;
					} else if (isOp_Ari(currentChar)) {
						estado = 5;
					} else if (isCaracter_esp(currentChar)) {
						estado = 6;
					} else {
						throw new RuntimeException("Símbolo não reconhecido");
					}
					break;
				case 1:
					if (isChar(currentChar) || isDigit(currentChar)) {
						estado = 1;
						term += currentChar;
					} else {
						estado = 2;
					}
					break;
				case 2:
					back();
					Token token = new Token();
					token.setText(term);
					token.setType(token.TK_IDENTIFIER);
					return token;
					
				case 3:
					if(isDigit(currentChar)){
						estado=3;
						term += currentChar;
					}
					else if (!isChar(currentChar)){
						estado = 4;
					}
					else{
						throw new RuntimeException("unrecognized number");
					}
					
				case 4:
					token = new Token();
					token.setText(term);
					token.setType(token.TK_IDENTIFIER);
					back();
					return token;
				case 5:
					term += currentChar;
					token =  new Token ();
					token.setType(Token.TK_OPERATOR);
					token.setText(term);
					return token;
			}

		}
	}

}
