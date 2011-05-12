package juego;

public class Jugador {
	Tablero tablero;
	Estrategia estrategia;
	
	public Jugador(){}
	
	/**
	 * Se obtiene una jugada utilizando la estrategia
	 * @return la posicion jugada
	 */
	public Posicion obtenerJugada(){
		Posicion posicion = this.estrategia.obtenerMejorJugada(this.tablero);
		this.tablero = this.tablero.agregarJugada(posicion);
		return posicion;
	}
	/**
	 * Se setea una jugada del oponente
	 * @param posicion
	 */
	public void setJugada(Posicion posicion){
		this.tablero = this.tablero.agregarJugada(posicion);
	}
	
	/**
	 * @return the tablero
	 */
	public Tablero getTablero() {
		return tablero;
	}

	/**
	 * @param tablero the tablero to set
	 */
	public void setTablero(Tablero tablero) {
		this.tablero = tablero;
	}

	/**
	 * @return the estrategia
	 */
	public Estrategia getEstrategia() {
		return estrategia;
	}

	/**
	 * @param estrategia the estrategiaAlfaBeta to set
	 */
	public void setEstrategia(Estrategia estrategia) {
		this.estrategia = estrategia;
	}
}
