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
			if (capturaEnemigo(posicion)) {
				nuevoTablero.estado = Constantes.setGanador(posicion.getTurno());
			}
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
		if (!perteneceTablero(posicion.getX(), posicion.getY())) {
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
		if (capturaEnemigo(jugada)) {
			return false;
		}
		if (grupoAmigoLibre(jugada)) {
			return false;
		}
		return true;
	}
	
	private boolean capturaEnemigo(Posicion posicion) {
		List<Integer> libertadEnemigos = administradorGrupos.gradosLibertadEnemigosVecinos(posicion);
		for (Integer libertad : libertadEnemigos) {
			if (libertad == 1) {
				return true;
			}
		}
		return false;
	}

	private boolean grupoAmigoLibre(Posicion posicion) {
		List<Integer> libertadAmigos = administradorGrupos.gradosLibertadAmigosVecinos(posicion);
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
		
		if (perteneceTablero(x - 1, y)) {
			vecinos.add(new Posicion(x - 1, y, tablero[x - 1][y]));
		}
		if (perteneceTablero(x + 1, y)) {
			vecinos.add(new Posicion(x + 1, y, tablero[x + 1][y]));
		}
		if (perteneceTablero(x, y - 1)) {
			vecinos.add(new Posicion(x, y - 1, tablero[x][y - 1]));
		}
		if (perteneceTablero(x, y + 1)) {
			vecinos.add(new Posicion(x, y + 1, tablero[x][y + 1]));
		}
		
		return vecinos;
	}

	private boolean perteneceTablero(int x, int y) {
		if ((x < 0) || (y < 0) || (x >= tablero.length) || (y >= tablero.length)) {
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
			copia.tablero = new int[this.tablero.length][this.tablero.length];
			for (int i = 0 ; i < this.tablero.length ; i++) {
				for (int j = 0 ; j < this.tablero.length ; j++) {
					copia.tablero[i][j] = this.tablero[i][j];
				}
			}
			copia.administradorGrupos = (AdministradorGrupos) this.administradorGrupos.clone();
		} catch (CloneNotSupportedException e) {}
		
		return copia;
	}

	public AdministradorGrupos getAdministradorGrupos() {
		return administradorGrupos;
	}
}
 