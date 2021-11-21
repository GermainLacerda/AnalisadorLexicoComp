package br.com.grupo_pica_pau.Parser;

import br.com.grupo_pica_pau.compilador.exceptions.LexicalException;
import br.com.grupo_pica_pau.compilador.exceptions.Token;
import br.com.grupo_pica_pau.compilador.lexico.PpScanner;

public class parser {
    private PpScanner  Scanner;
    private Token  token;
 
    public parser(PpScanner scanner) {
		this.Scanner = scanner;
	}
    public void E() {
		T();
		El();
		
	}
	
	public void El() {
		token = Scanner.nextToken();
		if (token != null) {
			OP();
			T();
			El();
		}
	}
	//pega o token do scanner e no parser ele faz a atribuição do token
	public void T() {
		token = Scanner.nextToken();
		if (token.getType() != Token.TK_IDENTIFIER && token.getType() != Token.TK_NUMBER) {
			throw new LexicalException("ID or NUMBER Expected!, found "+token.getType()+" ("+token.getText()+") at LINE "+token.getLine()+" and COLUMN "+token.getColumn());
		}
		
	}
	
	public void OP() {
		if (token.getType() != Token.TK_OPRelacional) {
			throw new LexicalException("Operator Expected, found "+token.getType()+" ("+token.getText()+")  at LINE "+token.getLine()+" and COLUMN "+token.getColumn());
		}
	}

}
