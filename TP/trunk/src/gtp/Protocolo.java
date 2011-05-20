package gtp;

import javax.swing.text.Position;

import juego.Constantes;
import juego.EstimadorGradosLibertad;
import juego.Estrategia;
import juego.EstrategiaAlfaBeta;
import juego.Jugador;
import juego.Posicion;
import juego.Tablero;

public class Protocolo {
	private String version = "1";
	private String name = "Sansa";
	private int versionProtocolo = 2;
	private String[] comandos = { "boardsize", "clear_board", "genmove",
			"known_command", "komi", "list_commands", "name", "play",
			"protocol_version", "quit", "ref-nodes", "ref-playouts",
			"ref-score", "version" };
	
	private Jugador jugador;
	//harcodeado por ahora
	Estrategia estrategia = new EstrategiaAlfaBeta(new EstimadorGradosLibertad(), 5, 5);
	

	public Protocolo() {
		this.jugador = new Jugador();
		this.jugador.setEstrategia(estrategia);
	}

	//es asi o es al revés?
	//se debe llamar siempre!!!!
	private String boardsize(int size){
		if(size <= 19 | size>= 5){
			jugador.setTablero(new Tablero(size));
			return null;
		}
		return "unacceptable size";
	}
	
	private String clear_board(){
		int size = jugador.getTablero().getDimension();
		jugador.setTablero(new Tablero(size));
		return null;
	}
	
	
	private int getXfromVertex (int vertexNumeric){
		return jugador.getTablero().getDimension() - vertexNumeric;
	}
	
	private int getYfromVertex (Character vertexABC){
		return "ABCDEFGHJKLMNOPQRSTUVWXYZ".indexOf(vertexABC);
	}
	
	private String getVertexFromPosicion(Posicion posicion){
		String s = new String();
		String s2 = new String();
		s = "ABCDEFGHJKLMNOPQRSTUVWXYZ".substring(posicion.getY(), posicion.getY()+1);
		s2 = String.valueOf(jugador.getTablero().getDimension() - posicion.getX());
		return s.concat(s2);
		
	}
		
	//entiendo q es una jugada del otro q me viene
	private String play(String color, String vertex){
		Character vertexABC = vertex.charAt(0);
		int vertexNumeric = Integer.parseInt(vertex.substring(1));
		int x = getXfromVertex(vertexNumeric);
		int y = getYfromVertex(vertexABC);
		if(x <= (jugador.getTablero().getDimension()-1) && x >= 0 && y <= (jugador.getTablero().getDimension()-1) && y >= 0){
			if(isWhite(color)){
				Posicion jugadaOponente = new Posicion(x, y, Constantes.BLANCO);
			    jugador.agregarJugada(jugadaOponente);
			    return null;
			}else if(isBlack(color)){
				Posicion jugadaOponente = new Posicion(x, y, Constantes.NEGRO);
			    jugador.agregarJugada(jugadaOponente);
			    return null;
			}
		}
		return "illegal move";
	}
	
	//jugada q realiza el jugador con estategia
	private String genmove(String color){
		int colorJugada = 0;
		if (isBlack(color)){
			colorJugada = Constantes.NEGRO;
		}else if (isWhite(color)) {
			colorJugada = Constantes.BLANCO;
		}
		Posicion posicion = jugador.obtenerJugada(colorJugada);
		return getVertexFromPosicion(posicion);
	}
	
	private boolean isBlack(String color) {
		return (color.equals("b") | color.equals("B") | color.equals("BLACK") | color.equals("black"));
	}

	private boolean isWhite(String color) {
		return (color.equals("w") | color.equals("W") | color.equals("WHITE") | color.equals("white"));
	}

	public String procesarComando(String linea) {
		String[] token = linea.split(" ");
		String response = "";

		if (token[0].equals("protocol_version")) {
			response = "= " + versionProtocolo;
		} else if (token[0].equals("name")) {
			response = "= " + name;
		} else if (token[0].equals("version")) {
			response = "= " + this.version;
		} else if (token[0].equals("known_command")) {
			response = "= " + esSoportado(token[1]);
		} else if (token[0].equals("list_commands")) {
			response = "= " + listarComandos();
		} else if (token[0].equals("boardsize")) {
			boardsize(Integer.parseInt(token[1]));
			response = "= ";
		} else if (token[0].equals("clear_board")) {
			clear_board();
			response = "= ";
		} else if (token[0].equals("play")) {
			play(token[1], token[2]);
			response = "= ";
		} else if (token[0].equals("genmove")) {
			response = "= " + genmove(token[1]);
		} else if (token[0].equals("final_status_list")) { // kgs
			response = "= " + listadoFinal();
		} else if (token.length > 1) {
			if (token[1].equals("protocol_version")) {
				response = "=" + token[0] + " " + this.versionProtocolo;
			} else if (token[1].equals("name")) {
				response = "=" + token[0] + " " + name;
			} else if (token[1].equals("version")) {
				response = "=" + token[0] + " " + version;
			} else if (token[1].equals("known_command")) {
				response = "=" + token[0] + " " + esSoportado(token[2]);
			} else if (token[1].equals("list_commands")) {
				response = "=" + token[0] + " " + this.listarComandos();
			} else if (token[1].equals("boardsize")) {
				boardsize(Integer.parseInt(token[2]));
				response = "=" + token[0] + " ";
			} else if (token[1].equals("clear_board")) {
				clear_board();
				response = "=" + token[0] + " ";
			} else if (token[1].equals("play")) {
				play(token[2], token[3]);
				response = "=" + token[0] + " ";
			} else if (token[1].equals("genmove")) {
				response = "=" + token[0] + " " + genmove(token[2]);
			} else if (token[1].equals("final_status_list")) { // kgs
				response = "=" + token[0] + " " + this.listadoFinal();
			} else {
				response = "? " + "unknown command";
				//return;
			}
		} else {
			response = "? " + "unknown command";
			// return;
		}

		response = response + "\n\n";
		return response;
	}

	public boolean esSoportado(String command) {
		for (int i = 0; i < this.comandos.length; i++) {
			if (this.comandos[i].equals(command)) {
				return true;
			}
		}
		return false;
	}

	public String listarComandos() {
		String list = "";

		for (int i = 0; i < this.comandos.length; i++) {
			if (i == 0) {
				list += this.comandos[i];
			} else {
				list += "\n";
				list += this.comandos[i];
			}
		}
		return list;
	}

	public String listadoFinal() {
		return "";
	}

}
