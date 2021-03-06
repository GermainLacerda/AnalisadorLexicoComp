package br.com.grupo_pica_pau.compilador.main;

import java.io.File;

import br.com.grupo_pica_pau.Parser.parser;
import br.com.grupo_pica_pau.compilador.exceptions.Token;
import br.com.grupo_pica_pau.compilador.lexico.PpScanner;

public class MainClass {
	
	public static void main(String[] args){
		File arq = new File("C:\\Users\\germa\\eclipse-workspace\\AnalisadorLaxicoComp\\src\\br\\com\\grupo_pica_pau\\compilador\\main\\CodigoLexico.txt");
        if (!arq.exists()) {
            arq.mkdir();
        }
		
			PpScanner sc = new PpScanner(arq.getPath());
			parser pa = new parser (sc);
			Token token = null;
			System.out.println("Especificações do documento:  ");
			do {
				token = sc.nextToken();
				if (token != null) {
					System.out.println(token);
				}
			} while (token != null);
		
	}
}


