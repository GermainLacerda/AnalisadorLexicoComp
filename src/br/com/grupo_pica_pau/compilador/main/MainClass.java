package br.com.grupo_pica_pau.compilador.main;

import java.io.File;
import java.util.Scanner;

import br.com.grupo_pica_pau.compilador.exceptions.Token;
import br.com.grupo_pica_pau.compilador.lexico.PpScanner;

public class MainClass {
	public static void main(String[] args){
		File arq = new File("C:\\Users\\germa\\eclipse-workspace\\AnalisadorLaxicoComp\\src\\br\\com\\grupo_pica_pau\\compilador\\main\\CodigoLexico.txt");
        if (!arq.exists()) {
            arq.mkdir();
        }
		try {
			PpScanner sc = new PpScanner(arq.getPath());
			Token token = null;
			System.out.println("pior maneira de debugar alguma coisa");
			do {
				token = sc.nextToken();
				if (token != null) {
					System.out.println(token);
				}
			} while (token != null);
		} catch (Exception e) {
			System.out.println("Generic Error");
		}

	}
}

// CodigoLexico
