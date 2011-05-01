package proxy;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Proxy {

	/**
	 * @param args puerto para escuchar conexiones entrantes.
	 */
	public static void main(String[] args){
		int puerto = -1;
		ServerSocket serverSocket = null;
		Socket socket = null;
		
		try {
			puerto = Integer.valueOf(args[0]);
			serverSocket = new ServerSocket(puerto);
			socket = serverSocket.accept();
			Conmutador conmutador = new Conmutador(socket);
			conmutador.conmutar();
			
		} catch (NumberFormatException e) {
			Proxy.mostrarUso();
		}catch (IOException e){
			System.out.println("ERROR EN LA CONEXION");
			e.printStackTrace();
		}
	}
	
	/**
	 * Muestra cómo se debe usar el proxy desde línea de comando.
	 */
	public static void mostrarUso(){
		System.out.println("Proxy  GTP Engine");
		System.out.println("Modo de uso:");
		System.out.println("java ProxyGTPEngine puerto");
	}
}
