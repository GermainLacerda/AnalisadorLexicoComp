package br.com.grupo_pica_pau.compilador.lexico;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import br.com.grupo_pica_pau.compilador.exceptions.LexicalException;
import br.com.grupo_pica_pau.compilador.exceptions.Token;

public class PpScanner {

	private char[] content;
	private int estado;
	private int pos;
	private int column;
	private int line;
	private String nome;

	public PpScanner(String filename) {
		try {
			line = 1;
			column = 0;
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

	public Token nextToken() {
		char currentChar;
		String term = "";
		Token token;
		if (isEOF()) {
			return null;
		}
		estado = 0;
		while (true) {
			currentChar = nextChar();
			switch (estado) {
			case 0:
				if (isChar(currentChar) || isCoisinha(currentChar)) {
					term += currentChar;
					estado = 1;
				} else if (isDigit(currentChar)) {
					term += currentChar;
					estado = 2;
				} else if (isSpace(currentChar)) {
					estado = 0;
				} /*
					 * else if (isIgual(currentChar)) { term += currentChar; estado = 6; }
					 */ else if (isOp_Rel(currentChar)) {
					term += currentChar;
					estado = 5;
				} else if (isOp_Ari(currentChar)) {
					term += currentChar;
					estado = 7;
				} else if (isCaracter_esp(currentChar)) {
					term += currentChar;
					token = new Token();
					token.setText(term);
					token.setType(token.TK_CEsp);
					nome = "Caracter esp  ";
					token.setNome(nome);
					token.setLine(line);
					token.setColumn(column - term.length());
					return token;
				} else if (isPontuation(currentChar)) {
					term += currentChar;
					token = new Token();
					token.setText(term);
					token.setType(token.TK_PONTUATION);
					nome = "Pontuação     ";
					token.setNome(nome);
					token.setLine(line);
					token.setColumn(column - term.length());
					return token;
				} else {
					if (isEOF()) {
						System.out.println("Fim do Arquivo!");
						System.exit(0);
					} else {
						throw new RuntimeException("Caracter inserido não é reconhecido por essa linguagem");
					}

				}
				break;

			case 1:
				if (isChar(currentChar) || isCoisinha(currentChar)) {
					estado = 1;

					term += currentChar;
					if (isReserved_char(term)) {
						token = new Token();
						token.setText(term);
						token.setType(token.TK_CResv);
						nome = "Reservados    ";
						token.setNome(nome);
						return token;
					} else if (isPica_pau(term)) {
						token = new Token();
						token.setText(term);
						token.setType(token.TK_Integrantes);
						nome = "Integrante    ";
						token.setNome(nome);
						return token;
					}
				} else if (isDigit(currentChar) || isSpace(currentChar) || isOp_Rel(currentChar)
						|| isOp_Ari(currentChar) || isCaracter_esp(currentChar) || isEOF(currentChar)) {
					if (!isEOF(currentChar))
						back();
					token = new Token();
					token.setType(Token.TK_CHARACTER);
					token.setText(term);
					nome = "Caracter      ";
					token.setNome(nome);
					token.setLine(line);
					token.setColumn(column - term.length());
					return token;

				} else {
					throw new LexicalException("Malformed Identifier");
				}
				break;

			case 2:
				if (isDigit(currentChar)) {
					estado = 2;
					term += currentChar;
				} else if (isChar(currentChar)) {
					estado = 1;
				} else if (isSpace(currentChar) || isOp_Rel(currentChar) || isOp_Ari(currentChar)
						|| isCaracter_esp(currentChar) || isEOF(currentChar)) {
					if (!isEOF(currentChar))
						back();
					token = new Token();
					token.setType(Token.TK_CHARACTER);
					token.setText(term);
					nome = "Numérico      ";
					token.setNome(nome);
					token.setLine(line);
					token.setColumn(column - term.length());
					return token;
				} else if (isFloat(currentChar)) {
					estado = 3;
					term += currentChar;
				} else {
					throw new LexicalException("Simbolo numérico nao reconhecido");
				}
				break;

			case 3: // is float
				if (isDigit(currentChar)) {
					estado = 4;
					term += currentChar;
				} else {
					back();
					throw new LexicalException(
							"Simbolo numérico nescessário para Float à direita do '.' não reconhecido na coluna: "
									+ pos);
				}
				break;

			case 4:
				if (isDigit(currentChar)) {
					estado = 4;
					term += currentChar;
				} else {

					token = new Token();
					token.setText(term);
					token.setType(token.TK_Float);
					nome = "Float         ";
					token.setNome(nome);
					token.setLine(line);
					token.setColumn(column - term.length());
					back();
					return token;
				}
				break;

			case 5:
				// caso optar para o sistema de cada op rel ser um token dif usar a atribuição
				// do tolen dentro dos ifs
				// assim como os nomes.
				if (currentChar != '=') {
					if (term.compareTo("<") == 0) {
						nome = "Op Relacional (menor que)";
					} else if (term.compareTo(">") == 0) {
						nome = "Op Relacional (maior que)";
					} else if (term.compareTo("=") == 0) {
						nome = "Op Aritmetico (atri )";
						back();
						token = new Token();
						token.setText(term);
						token.setType(token.TK_OPArithmetic);
						token.setNome(nome);
						token.setLine(line);
						token.setColumn(column - term.length());
						return token;
					}
					estado = 6;
				} else {
					term += currentChar;
					if (term.charAt(0) == '!') {
						nome = "Op Relacional (diferente)";
					} else if (term.charAt(0) == '<') {
						nome = "Op Relacional (menor ou igual que)";
					} else if (term.charAt(0) == '>') {
						nome = "Op Relacional (maior ou igual que)";
					} else if (term.charAt(0) == '=') {
						nome = "Op Relacional (igual)";
					}
					estado = 6;
				}
				break;
			case 6:
				back();
				token = new Token();
				token.setText(term);
				token.setType(token.TK_OPRelacional);
				token.setNome(nome);
				token.setLine(line);
				token.setColumn(column - term.length());
				return token;
			case 7:
				// caso optar para o sistema de cada op arit ser um token dif usar a atribuição
				// do tolen dentro dos ifs assim como os nomes.
				if (term.compareTo("+") == 0) {
					back();
					nome = "Op Aritmetico (soma )";
					token = new Token();
					token.setText(term);
					token.setType(token.TK_OPArithmetic);
					token.setNome(nome);
					token.setLine(line);
					token.setColumn(column - term.length());
					return token;
				} else if (term.compareTo("-") == 0) {
					back();
					nome = "Op Aritmetico (subtr)";
					token = new Token();
					token.setText(term);
					token.setType(token.TK_OPArithmetic);
					token.setNome(nome);
					token.setLine(line);
					token.setColumn(column - term.length());
					return token;
				} else if (term.compareTo("*") == 0) {
					back();
					nome = "Op Aritmetico (mult )";
					token = new Token();
					token.setText(term);
					token.setType(token.TK_OPArithmetic);
					token.setNome(nome);
					token.setLine(line);
					token.setColumn(column - term.length());
					return token;
				} else if (term.compareTo("/") == 0) {
					back();
					nome = "Op Aritmetico (divi )";
					token = new Token();
					token.setText(term);
					token.setType(token.TK_OPArithmetic);
					token.setNome(nome);
					token.setLine(line);
					token.setColumn(column - term.length());
					return token;
				}

			}
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

	private boolean isCoisinha(char c) {
		return (c == '"' || c == '‘' || c == '’');
	}

	private boolean isPontuation(char c) {// 3

		return c == '?' || c == '.' || c == ',';
	}

	private boolean isOp_Rel(char c) {// 4

		return c == '<' || c == '>' || c == '!' || c == '=';
	}

	private boolean isOp_Ari(char c) {// 5
		return c == '+' || c == '-' || c == '*' || c == '/';
	}

	private boolean isCaracter_esp(char c) { // 6
		return c == ')' || c == '(' || c == '{' || c == '}' || c == ',' || c == ';' || c == '@';
	}

	private boolean isReserved_char(String c) {
		return c.equals("main") || c.equals("if") || c.equals("else") || c.equals("while") || c.equals("do")
				|| c.equals("for") || c.equals("int") || c.equals("float") || c.equals("char");
	}

	private boolean isPica_pau(String c) {
		return c.equals("Aroldo") || c.equals("Felipe") || c.equals("Germain") || c.equals("Letícia")
				|| c.equals("Rodrigo") || c.equals("Ryan");
	}

	private boolean isSpace(char c) {
		if (c == '\n' || c == '\r') {
			line++;
			column = 0;
		}
		return c == ' ' || c == '\t' || c == '\n' || c == '\r';
	}

	private boolean isEOF() {

		return pos >= content.length;
	}

	private boolean isEOF(char c) {
		return c == '\0';
	}

	private char nextChar() {
		if (isEOF()) {
			return '\0';
		}
		return content[pos++];

	}

	/*
	 * private boolean isIgual(char c) { return c == '='; }
	 */

	private void back() {
		pos--;
		column--;
	}
}