package proxy;
import java.net.*;
import java.io.*;
/**
 * 
 * @author pablo
 * Tiene la resposabilidad de conmutar entre la entrada y salida standard y la de un socket.
 */
class Conmutador {
    private Socket socket;
    private BufferedReader entrada = null;
    private DataOutputStream salida = null;
 
    public Conmutador(Socket socket) {
        this.socket = socket;
    }
 
    /**
     * 
     * @throws IOException
     */
    void conmutar() throws IOException {
    	//TODO: Cuando corta esto?
        while (true) {

			this.entrada = new BufferedReader(new InputStreamReader(System.in));
			this.salida = new DataOutputStream(this.socket.getOutputStream());
        	/**
        	 * Se espera un comando desde GoGUI-twoGTP y se envia por Socket al Remote GTP Engine.
        	 */
			this.salida.writeBytes(this.entrada.readLine() + "\n");
			this.salida.flush();
			/**
			 * Se conmuta la entrada al Socket.
			 */
			this.entrada = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			/**
			 * Se espera por la respuesta del Remote GTP Engine y se muestra por salida standard.
			 */
			System.out.println(this.entrada.readLine() + "\n");
		}
    }
}
