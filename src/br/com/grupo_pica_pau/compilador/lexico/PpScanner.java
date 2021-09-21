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

	private boolean isDigit(char c) {// 1
		return c >= '0' && c <= '9';
	}

	private boolean isChar(char c) {// 2
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
	}

	private boolean isPontuation(char c) {// 3

		return c == '?' || c == '.';
	}

	private boolean isOp_Rel(char c) {// 4

		return c == '<' || c == '>' || c == '=' || c == '!';
	}

	private boolean isOp_Ari(char c) {// 5
		return c == '+' || c == '-' || c == '*' || c == '/';
	}

	private boolean isCaracter_esp(char c) { // 6
		return c == ')' || c == '(' || c == '{' || c == '}' || c == ',' || c == ';';
	}

	private boolean isSpace(char c) {
		return c == ' ' || c == '\t' || c == '\n' || c == '\r';
	}

	private boolean isEOF() {
		return pos == content.length;
	}

	private char nextChar() {
		return content[pos++];
	}

	private void back() {
		pos--;
	}

	public Token nextToken() {
		char currentChar;
		String term = "";
		if (isEOF()) {
			return null;
		}
		estado = 0;
		while (true) {
			currentChar = nextChar();
			switch (estado) {
				case 0:
					if (isChar(currentChar)) {
						term += currentChar;
						estado = 1;
					} else if (isDigit(currentChar)) {
						term += currentChar;
						estado = 3;						
					} else if (isSpace(currentChar)) {
						estado = 0;
					} else if (isOp_Rel(currentChar)) {
						term += currentChar;
						estado = 7;
					} else if (isOp_Ari(currentChar)) {
						term += currentChar;
						estado = 9;
					} else if (isCaracter_esp(currentChar)) {
						term += currentChar;
						estado = 11;
					} else if (isPontuation(currentChar)) {
						term += currentChar;
						estado = 5;
					} else {
						throw new RuntimeException("Caractere não reconhecido");
					}
					break;
				// =================================================letras
				case 1: // para characteres
					if (isChar(currentChar) || isDigit(currentChar)) {
						estado = 1;
						term += currentChar;
					} else {
						estado = 2;
					}
					break;
				case 2:

					Token token = new Token();
					token.setText(term);
					token.setType(token.TK_CHARACTER);
					back();
					return token;
				// ==============================================numeros
				case 3:// para numeros
					if (isDigit(currentChar)) {
						estado = 3;
						term += currentChar;
					} else if (isChar(currentChar)) {
						estado = 1;
					} else if (isSpace(currentChar) || isOp_Rel(currentChar)) {
						estado = 4;
					} else {
						throw new RuntimeException("Simbolo numerico nao reconhecido");
					}
					break;
				case 4:
					token = new Token();
					token.setText(term);
					token.setType(token.TK_NUMBER);
					back();
					return token;
				// ============================================Pontuação
				case 5:// pontuação
					if (isPontuation(currentChar)) {
						estado = 5;
						term += currentChar;
					} else if (isSpace(currentChar)|| isOp_Rel(currentChar)) {
						estado = 6;
					} else {
						throw new RuntimeException("Simbolo de pontuação nao reconhecido");
					}
					break;
				case 6:
					token = new Token();
					token.setText(term);
					token.setType(token.TK_PONTUATION);
					back();
					return token;
				// ================================operadores relacionais
				case 7:// operadores relacionais
					if (isOp_Rel(currentChar)) {
						estado = 7;
						term += currentChar;
					} else if (isSpace(currentChar) || isEOF()) {
						estado = 8;
					} else {
						throw new RuntimeException("Simbolo relacional nao reconhecido");
					}
					break;
				case 8:
					token = new Token();
					token.setText(term);
					token.setType(token.TK_ASSIGN);
					back();
					return token;
				// ================================operadores aritmetricos
				case 9:// operadores aritmetricos
					if (isOp_Ari(currentChar)) {
						estado = 9;
						term += currentChar;
					} else if (isSpace(currentChar)|| isOp_Rel(currentChar)) {
						estado = 10;
					} else {
						throw new RuntimeException("Simbolo aritmetrico nao reconhecido");
					}
					break;
				case 10:
					token = new Token();
					token.setText(term);
					token.setType(token.TK_OPAritmetric);
					back();
					return token;
				// ================================Caracteres epeciais
				case 11:// Caracteres epeciais
					if (isCaracter_esp(currentChar)) {
						estado = 11;
						term += currentChar;
					} else if (isSpace(currentChar)|| isOp_Rel(currentChar)) {
						estado = 12;
					} else {
						throw new RuntimeException("Caracteres epecial não reconhecido");
					}
					break;
				case 12:
					token = new Token();
					token.setText(term);
					token.setType(token.TK_CEsp);
					back();
					return token;
			}

		}
	}

}
