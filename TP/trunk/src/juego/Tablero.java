package juego;
import java.util.ArrayList;
import java.util.List;

public class Tablero implements Cloneable {
	private int tablero[][];
	private AdministradorGrupos administradorGrupos;
	private int estado;
	private int turno;
	
	public Tablero(int dimension) {
		administradorGrupos = new AdministradorGrupos();
		estado = Constantes.EN_PROGRESO;
		turno = Constantes.NEGRO;
		
		tablero = new int[dimension][dimension];
		for (int i = 0 ; i < dimension ; i++) {
			for (int j = 0 ; j < dimension ; j++) {
				tablero[i][j] = Constantes.VACIO;
			}
		}
	}
	
	public Tablero agregarJugada(Posicion posicion) {
		if (esLegal(posicion)) {
			Tablero nuevoTablero = (Tablero)this.clone();
			nuevoTablero.tablero[posicion.getX()][posicion.getY()] = posicion.getTurno();
			nuevoTablero.administradorGrupos.agregarFicha(posicion, nuevoTablero.buscarLibertades(posicion));
			nuevoTablero.turno = Constantes.oponente(this.turno);
			return nuevoTablero;
		}
		return null;
	}

	public int getTurno() {
		return turno;
	}
	
	public List<Posicion> obtenerJugadasPosibles(int color) {
		List<Posicion> jugadas = new ArrayList<Posicion>();
		for (int i = 0 ; i < tablero.length ; i++) {
			for (int j = 0 ; j < tablero.length ; j++) {
				if (tablero[i][j] == Constantes.VACIO) {
					jugadas.add(new Posicion(i, j, color));
				}
			}
		}
		
		return jugadas;
	}
	
	public int getEstado() {
		return estado;
	}
	
	private boolean esLegal(Posicion posicion) {
		if (!perteneceTablero(posicion)) {
			return false;
		}
		if (turnoInvalido(posicion.getTurno())) {
			return false;
		}
		if (espacioOcupado(posicion)) {
			return false;
		}
		if (esSuicidio(posicion)) {
			return false;
		}
		
		return true;
	}

	private boolean espacioOcupado(Posicion posicion) {
		if (tablero[posicion.getX()][posicion.getY()] != Constantes.VACIO) {
			return true;
		}
		return false;
	}

	private boolean turnoInvalido(int color) {
		if ((color != Constantes.BLANCO) && (color != Constantes.NEGRO)) {
			return true;
		}
		return false;
	}

	private List<Posicion> buscarLibertades(Posicion posicion) {
		List<Posicion> libertades = new ArrayList<Posicion>();
		List<Posicion> posicionesVecinas = obtenerVecinos(posicion);
		for (Posicion vecino : posicionesVecinas) {
			if (vecino.getTurno() == Constantes.VACIO) {
				libertades.add(vecino);
			}
		}
		
		return libertades;
	}

	private boolean esSuicidio(Posicion jugada) {
		if (obtenerEspaciosVecinos(jugada).size() > 0) {
			return false;
		}
		List<Integer> libertadAmigos = administradorGrupos.gradosLibertadAmigosVecinos(jugada);
		List<Integer> libertadEnemigos = administradorGrupos.gradosLibertadEnemigosVecinos(jugada);
		if (grupoAmigoLibre(libertadAmigos)) {
			return false;
		}
		if (capturaEnemigo(libertadEnemigos)) {
			estado = jugada.getTurno();
			return false;
		}
		
		return true;
	}
	
	private boolean capturaEnemigo(List<Integer> libertadEnemigos) {
		for (Integer libertad : libertadEnemigos) {
			if (libertad == 1) {
				return true;
			}
		}
		return false;
	}

	private boolean grupoAmigoLibre(List<Integer> libertadAmigos) {
		for (Integer libertad : libertadAmigos) {
			if (libertad > 1) {
				return true;
			}
		}
		return false;
	}

	private List<Posicion> obtenerVecinos(Posicion posicion) {
		int x = posicion.getX();
		int y = posicion.getY();
		
		List<Posicion> vecinos = new ArrayList<Posicion>();
		
		Posicion vecino1 = new Posicion(x - 1, y, tablero[x - 1][y]);
		if (perteneceTablero(vecino1)) {
			vecinos.add(vecino1);
		}
		Posicion vecino2 = new Posicion(x + 1, y, tablero[x + 1][y]);
		if (perteneceTablero(vecino2)) {
			vecinos.add(vecino2);
		}
		Posicion vecino3 = new Posicion(x, y - 1, tablero[x][y - 1]);
		if (perteneceTablero(vecino3)) {
			vecinos.add(vecino3);
		}
		Posicion vecino4 = new Posicion(x, y + 1, tablero[x][y + 1]);
		if (perteneceTablero(vecino4)) {
			vecinos.add(vecino4);
		}
		
		return vecinos;
	}

	private boolean perteneceTablero(Posicion posicion) {
		if ((posicion.getX() < 0) || (posicion.getY() < 0) 
				|| (posicion.getX() >= tablero.length) || (posicion.getY() >= tablero.length)) {
			return false;
		}
		return true;
	}

	private List<Posicion> obtenerEspaciosVecinos(Posicion fichaGrupo) {
		List<Posicion> vecinos = obtenerVecinos(fichaGrupo);
		List<Posicion> espaciosVecinos = new ArrayList<Posicion>();
		
		for (Posicion vecino : vecinos) {
			if (vecino.getTurno() == Constantes.VACIO) {
				espaciosVecinos.add(vecino);
			}
		}
		
		return espaciosVecinos;
	}

	@Override
	protected Object clone() {
		Tablero copia = null;
		try {
			copia = (Tablero)super.clone();
			copia.administradorGrupos = (AdministradorGrupos) this.administradorGrupos.clone();
		} catch (CloneNotSupportedException e) {}
		
		return copia;
	}

	public AdministradorGrupos getAdministradorGrupos() {
		return administradorGrupos;
	}
}
 