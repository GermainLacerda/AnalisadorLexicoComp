package br.com.grupo_pica_pau.compilador.exceptions;

public class Token {
	public static final int TK_Space        = -1;
	public static final int TK_IDENTIFIER   = 0;
	public static final int TK_NUMBER       = 1;//numero
	public static final int TK_CHARACTER    = 2;
	public static final int TK_PONTUATION   = 3;//pontuação
	public static final int TK_OPRelacional = 4;//operador relacional
	public static final int TK_OPAritmetric = 5;//operador aritmetrico
	public static final int TK_CEsp         = 6;//caracter especial
	public static final int TK_CResv        = 7;//palavra reservda
	public static final int TK_Float        = 8;//float
	public static final int TK_Integrantes  = 9;//palavras reservados dos menbros
	

	private int type;
	private String text;
	private String nome;
	private int    line;
	private int    column;

	public Token(int type, String text) {
		super();
		this.type = type;
		this.text = text;
	}

	public Token() {
		super();
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String n) {
		this.nome = n;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	@Override
	public String toString() {
		return "Token type= " + type + " - "+ nome +" -  text=  " + text;
	}

}
