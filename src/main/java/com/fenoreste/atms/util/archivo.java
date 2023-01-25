package com.fenoreste.atms.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.springframework.stereotype.Service;

import lombok.Getter;

@Getter
@Service
public class archivo {

	String fichero = "atms_config.txt";
	String nbd = "";
	String ipbd = "";

	public archivo(){
        leeTxt();
    }

	private String getHome() {
		return System.getProperty("user.home");
	}

	private String getSeparator() {
		return System.getProperty("file.separator");
	}

	private File getTxt() {
		String ruta_fichero = getHome() + getSeparator()+ fichero;
		File file = new File(ruta_fichero);
		if (file.exists()) {			
		} else {
			System.out.println("El fichero " + ruta_fichero+ " no existe");			
		}		
		return file;
	}

	private void leeTxt() {
		if (getTxt() != null) {
			try {
				FileReader fr = new FileReader(getTxt());
				BufferedReader br = new BufferedReader(fr);
				String linea;
				while ((linea = br.readLine()) != null) {
					leer_lineas(linea);
				}
			} catch (Exception e) {
				System.out.println("Excepcion leyendo el txt " + fichero + ": " + e);
			}
		} else {
			System.out.println("No se encontro el fichero.");
		}
	}

	private void leer_lineas(String linea) {
		if (linea.contains("base_de_datos")) {
			nbd = linea.split("=")[1].trim();
		}
		if (linea.contains("direccion_servidor")) {
			ipbd = linea.split("=")[1].trim();
		}
	}

}
