import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import juego.Constantes;
import juego.EstimadorGradosLibertad;
import juego.Estrategia;
import juego.EstrategiaAlfaBeta;
import juego.Posicion;
import juego.Tablero;



public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("Hello GO!");

		Tablero tablero = new Tablero(10);
		Estrategia estrategia = new EstrategiaAlfaBeta(new EstimadorGradosLibertad(), 6, 6);
		
		while (true) {
			Posicion jugada = estrategia.obtenerMejorJugada(tablero);
			System.out.println("x: " + jugada.getX() + " y: " + jugada.getY());
			tablero = tablero.agregarJugada(jugada);
			
		    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		    String x = in.readLine();
		    String y = in.readLine();
		    Posicion jugadaOponente = new Posicion(Integer.parseInt(x), Integer.parseInt(y), Constantes.BLANCO);
		    tablero = tablero.agregarJugada(jugadaOponente);
		}
	}

}
