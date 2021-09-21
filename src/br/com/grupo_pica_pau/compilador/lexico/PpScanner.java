package br.com.grupo_pica_pau.compilador.lexico;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import br.com.grupo_pica_pau.compilador.exceptions.Token;

public class PpScanner {

	private char[] content;
	private int estado;
	private int pos;
	
	public PpScanner (String filename) {
		try {
			String txtConteudo;
			txtConteudo = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
			System.out.println("DEBUG ----");
			System.out.println(txtConteudo);
			content = txtConteudo.toCharArray();
			pos = 0;
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/*
	wdj
	obtriwenfjo
	NADUFN
	Skdfji
	DBHF
	Kn 
	d
	fjkb
	UODF
	Ndf
	*/
/*
	public Token nextToken() {
		if(isEOF()) {
			return null;
		}
		estado = 0;
		while (true) {
			switch(estado) {
			case 0:
			}
		}
	}
*/
	private boolean isFloat (char c, char v) {
		return (c >= '0' && c <='9') && (v >= '0' && v <= '9');
	}
	private boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}
	
	private boolean isChar(char c) {
		return (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9');
	}
	
	private boolean isOp_Rel(char c) {
		return c == '<'|| c == '>' || c == '=' || c == '!';
	}
	
	private boolean isOp_Ari(char c) {
		return c == '+' || c == '-' || c == '*' || c == '=';
	}
	private boolean isCaracter_esp (char c) {
		return c == ')' || c == '(' || c == '{' || c == '}' || c == ',' || c == ';';
	}
	private char nextChar() {
		return content[pos++];
	}
	private boolean isEOF() {
		return pos == content.length;
	}
	private void back() {
		pos--;
	}
	
	public Token nextToken() {
		char currentChar;
		if(isEOF()) {
			return null;
		}
		estado = 0;
		while(true) {
			switch(estado) {
			case 0 : 
				if(isChar(currentChar)) {
					estado = 1;
				}
				else if (isDigit(currentChar)) {
					estado = 3;
				}
			//	else if(isSpace(currentChar)) {
			//		estado = 0;
			//	}
			//	else if(isOperator(currentChar)) {
			//		
			//	}
				else {
					throw new RuntimeException("Símbolo não reconhecido");
				}
				break;
			case 1:
				if(isChar(currentChar) || isDigit(currentChar)) {
					estado = 1;
				}
				else {
					estado = 2;
				}
				break;
			case 2:
				Token token = new Token();
				
			}
			
		}
	}

	
	

