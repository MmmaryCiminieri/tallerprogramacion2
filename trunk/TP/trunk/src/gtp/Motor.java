package gtp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 
 * @author pablo Tiene la responsabilidad de determinar el modo de conexion del
 *         GTP Engine.
 */
public class Motor {
	private BufferedReader entrada = null;
	private OutputStreamWriter salida = null;
	private final int CARACTER_ESPACIO = 32;
	private final int CARACTER_RETORNO_CARRO = 13;
	private final int CARACTER_TAB = 9;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//Crear motor
		//Crear jugador
		//Crear un protocolo
		//Levantar el tablero del archivo y setearselo al jugador.
		//Lo mismo con la estrategia
		//configurarEntradaSalida
		//While...para leer la entrada y jugar...
		Motor motor = new Motor();
		Protocolo protocolo = new Protocolo();
		String linea = "";
		String respuesta = "";
		
		while (true) {
			linea = motor.leerDeEntrada();
			//Validar que sea una linea
			
			respuesta = protocolo.procesarComando(linea);
			
			
		}
	}

	public void configurarEntradaSalida(String[] parametrosEntrada) {
		try {
			if (parametrosEntrada.length == 2) {
				String ip = parametrosEntrada[0];
				int puerto = Integer.valueOf(parametrosEntrada[1]);

				Socket socket = new Socket(ip, puerto);

				this.entrada = new BufferedReader(new InputStreamReader(socket
						.getInputStream()));
				this.salida = new OutputStreamWriter(socket.getOutputStream());
			} else {
				this.entrada = new BufferedReader(new InputStreamReader(
						System.in));
				this.salida = new OutputStreamWriter(System.out);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	public String leerDeEntrada() {
		String linea = "";
		try {
			linea = this.entrada.readLine();
			linea = linea.replace((char) CARACTER_RETORNO_CARRO,
					(char) CARACTER_ESPACIO);
			linea = linea.replace((char) CARACTER_TAB, (char) CARACTER_ESPACIO);
			linea = linea.trim();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return linea;
	}

	public void escribirEnSalida(String respuesta) {
		try {
			this.salida.write(respuesta + "\n\n");
			this.salida.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
