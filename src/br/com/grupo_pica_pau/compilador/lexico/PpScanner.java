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
				} else if (isIgual(currentChar)) {
					term += currentChar;
					estado = 6;
				} else if (isOp_Rel(currentChar)) {
					term += currentChar;
					estado = 7;
					/*token = new Token();
					token.setText(term);
					token.setType(token.TK_OPRelacional);
					nome = "Op Relacional ";
					token.setNome(nome);
					token.setLine(line);
					token.setColumn(column - term.length());
					return token;*/
				} else if (isOp_Ari(currentChar)) {
					term += currentChar;
					token = new Token();
					token.setText(term);
					token.setType(token.TK_OPAritmetric);
					nome = "Op Aritmetrico";
					token.setNome(nome);
					token.setLine(line);
					token.setColumn(column - term.length());
					return token;
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
					throw new RuntimeException("Caractere não reconhecido");
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
					throw new LexicalException("Simbolo numerico nao reconhecido");
				}
				break;

			case 3:
				if (isDigit(currentChar)) {
					estado = 4;
					term += currentChar;
				} else if (isSpace(currentChar)) {
					term = "";
					estado = 0;
				} else {
					estado = 0;
				}
				break;

			case 4:
				if (isDigit(currentChar)) {
					estado = 4;
					term += currentChar;
				} else {
					estado = 5;
				}
				break;

			case 5:

				token = new Token();
				token.setText(term);
				token.setType(token.TK_Float);
				nome = "Float         ";
				token.setNome(nome);
				token.setLine(line);
				token.setColumn(column - term.length());
				back();
				return token;

			case 6:
				if (isIgual(currentChar)) {
					estado = 7;
					term += currentChar;
				} else {
					back();
					estado = 8;
				}
				break;

			case 7:
				if (isSpace(currentChar) || isEOF(currentChar) || isDigit(currentChar) || isChar(currentChar)
						|| isChar(currentChar)) {
					
					if (term.compareTo(">") == 0) {
						back();
						token = new Token();
						token.setText(term);
						token.setType(token.TK_OPRelacional);
						nome = "Maior que     ";
						token.setNome(nome);
						token.setLine(line);
						token.setColumn(column - term.length());

						return token;
					} else if (term.compareTo("<") == 0) {
						back();
						token = new Token();
						token.setText(term);
						token.setType(token.TK_OPRelacional);
						nome = "Menor que     ";
						token.setNome(nome);
						token.setLine(line);
						token.setColumn(column - term.length());

						return token;
					} else if (term.compareTo(">=") == 0) {
						back();
						token = new Token();
						token.setText(term);
						token.setType(token.TK_OPRelacional);
						nome = "Maior ou igual";
						token.setNome(nome);
						token.setLine(line);
						token.setColumn(column - term.length());

						return token;
					} else if (term.compareTo("<=") == 0) {
						back();
						token = new Token();
						token.setText(term);
						token.setType(token.TK_OPRelacional);
						nome = "Menor ou igual";
						token.setNome(nome);
						token.setLine(line);
						token.setColumn(column - term.length());

						return token;
					} else if (term.compareTo("!=") == 0) {
						back();
						token = new Token();
						token.setText(term);
						token.setType(token.TK_OPRelacional);
						nome = "Diferente     ";
						token.setNome(nome);
						token.setLine(line);
						token.setColumn(column - term.length());

						return token;
					} else {
						back();
						token = new Token();
						token.setText(term);
						token.setType(token.TK_OPRelacional);
						nome = "igual         ";
						token.setNome(nome);
						token.setLine(line);
						token.setColumn(column - term.length());

						return token;
					}
				} else if (isIgual(currentChar)) {
					estado = 8;
				} else {
					term += currentChar;
					System.out.print("\"" + term + "\" ");
					throw new LexicalException("Operador relacional mal formado");
				}
				break;
			case 8:
				if (isSpace(currentChar) || isEOF(currentChar) || isDigit(currentChar) || isChar(currentChar)) {
					
					if (isOp_Ari(currentChar)) {
						back();
						token = new Token();
						token.setText(term);
						token.setType(token.TK_OPRelacional);
						nome = "somar         ";
						token.setNome(nome);
						token.setLine(line);
						token.setColumn(column - term.length());
						return token;
					} else if (isOp_Ari(currentChar)) {
						back();
						token = new Token();
						token.setText(term);
						token.setType(token.TK_OPRelacional);
						nome = "subtr         ";
						token.setNome(nome);
						token.setLine(line);
						token.setColumn(column - term.length());
						return token;
					} else if (isOp_Ari(currentChar)) {
						back();
						token = new Token();
						token.setText(term);
						token.setType(token.TK_OPRelacional);
						nome = "mult         ";
						token.setNome(nome);
						token.setLine(line);
						token.setColumn(column - term.length());
						return token;
					} else if (isOp_Ari(currentChar)) {
						back();
						token = new Token();
						token.setText(term);
						token.setType(token.TK_OPRelacional);
						nome = "dividir         ";
						token.setNome(nome);
						token.setLine(line);
						token.setColumn(column - term.length());
						return token;
					} else {
						back();
						token = new Token();
						token.setText(term);
						token.setType(token.TK_OPRelacional);
						nome = "igual         ";
						token.setNome(nome);
						token.setLine(line);
						token.setColumn(column - term.length());
						return token;
					}
				} else {
					term += currentChar;
					System.out.print("\"" + term + "\" ");
					throw new LexicalException("Operador Aritmetico mal formado");
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

		return c == '?' || c == '.' || c == '.';
	}

	private boolean isOp_Rel(char c) {// 4

		return c == '<' || c == '>' || c == '!';
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

	private boolean isIgual(char c) {
		return c == '=';
	}

	private void back() {
		pos--;
		column--;
	}
}