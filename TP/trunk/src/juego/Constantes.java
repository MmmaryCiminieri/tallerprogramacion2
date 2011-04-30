package juego;

public class Constantes {
	public final static int VACIO = 0;
	public final static int BLANCO = 1;
	public final static int NEGRO = 2;
	
	public final static int GANA_BLANCO = 1;
	public final static int GANA_NEGRO = 2;
	public final static int EMPATE = 3;
	public final static int EN_PROGRESO = 4;
	
	public final static int VALOR_VICTORIA = Integer.MAX_VALUE;
	public final static int VALOR_DERROTA = -VALOR_VICTORIA;
	public final static int VALOR_EMPATE = 0;
	
	public final static int PENALIZACION_ATARI = 100;
	
	public static int oponente(int color) {
		if (color == BLANCO) {
			return NEGRO;
		}
		if (color == NEGRO) {
			return BLANCO;
		}
		return VACIO;
	}

	public static int setGanador(int color) {
		if (color == BLANCO) {
			return GANA_BLANCO;
		}
		if (color == NEGRO) {
			return GANA_NEGRO;
		}
		return EN_PROGRESO;
	}
}
