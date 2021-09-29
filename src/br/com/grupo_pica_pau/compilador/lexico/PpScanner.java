package br.com.grupo_pica_pau.compilador.lexico;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import br.com.grupo_pica_pau.compilador.exceptions.Token;

public class PpScanner {

	private char[] content;
	private int estado;
	private int pos;
	private String nome;

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

	
	private boolean isFloat(char c) {
		return c == '.';
	}
		 
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

	private boolean isReserved_char(String c){
		return c.equals("main") || c.equals("if")  || c.equals("else") || c.equals("while") || c.equals("do") || c.equals("for")||
			   c.equals("int")  || c.equals("float") || c.equals("char");
	}
	private boolean isPica_pau(String c){
		return c.equals("Aroldo") ||  c.equals("Felipe") ||  c.equals("Germain") ||  c.equals("Letícia") ||  c.equals("Rodrigo") ||  c.equals("Ryan");
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
					} else if (isReserved_char(term)) {
						estado = 13;
					}else if(isPica_pau(term)){
						estado = 16;
					}else {
						estado = 2;
					}
					break;
				case 2:
					Token token = new Token();
					token.setText(term);
					token.setType(token.TK_CHARACTER);
					nome = "identific.";
					token.setNome(nome);
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
					}else if(isFloat(currentChar)){
						estado = 14;
						term += currentChar;
					} else {
						throw new RuntimeException("Simbolo numerico nao reconhecido");
					}
					break;
				case 4:
					token = new Token();
					token.setText(term);
					token.setType(token.TK_NUMBER);
					nome = "inteiros  ";
					token.setNome(nome);
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
					nome = "Pontuação ";
					token.setNome(nome);
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
					nome = "Op Relac  ";
					token.setNome(nome);
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
					nome = "Op Aritm  ";
					token.setNome(nome);
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
					nome = "Carac Esp ";
					token.setNome(nome);
					back();
					return token;
			//===============================================Caracteres reservados
			/*	case 13:
					exception : uma letra diferente do caractere reservado= */
				case 13:
					token = new Token();
					token.setText(term);
					token.setType(token.TK_CResv);
					nome = "Reservados";
					token.setNome(nome);
					back();
					return token;
			// ==================================================Float
				case 14:
					if (isDigit(currentChar)) {
						estado = 14;
						term += currentChar;
					}else if (isSpace(currentChar) || isOp_Rel(currentChar)) {
						estado = 15;
					}
						break;
					
				case 15:
					token = new Token();
					token.setText(term);
					token.setType(token.TK_Float);
					nome = "Float     ";
					token.setNome(nome);
					back();
					return token;
			//  =================================================Integrantes grupo
				case 16:
					token = new Token();
					token.setText(term);
					token.setType(token.TK_Integrantes);
					nome = "Integrante";
					token.setNome(nome);
					back();
					return token;

			}

		}
	}

}

