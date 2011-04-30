package juego;

public class EstimadorGradosLibertad implements Estimador {

	@Override
	public int estimarSituacionActual(Tablero tablero, int color) {
		int estadoJuego = tablero.getEstado();
		if (estadoJuego == Constantes.EN_PROGRESO) {
			return evaluacionHeuristicaEstado(tablero, color);
		}
		return resultadoJuego(estadoJuego, color);
	}

	private int evaluacionHeuristicaEstado(Tablero tablero, int color) {
		int gradosLibertad = tablero.getAdministradorGrupos().minimoGradoLibertad(color);
		int gradosLibertadOponente = tablero.getAdministradorGrupos().minimoGradoLibertad(Constantes.oponente(color));
		
		int estimacion;
		if (faltaGrupo(gradosLibertad, gradosLibertadOponente)) {
			estimacion = evaluacionSinGrupos(gradosLibertad, gradosLibertadOponente);
		}else {
			estimacion = evaluacionConGrupos(gradosLibertad, gradosLibertadOponente, color, tablero);
		}
		
		return estimacion;
	}

	private boolean faltaGrupo(int gradosLibertad, int gradosLibertadOponente) {
		return ((gradosLibertad == Integer.MAX_VALUE) || (gradosLibertadOponente == Integer.MAX_VALUE));
	}
	
	private int evaluacionSinGrupos(int gradosLibertad,
			int gradosLibertadOponente) {
		if ((gradosLibertad == Integer.MAX_VALUE) && (gradosLibertadOponente == Integer.MAX_VALUE)){
			return 0;
		}
		if (gradosLibertad == Integer.MAX_VALUE) {
			return -gradosLibertadOponente;
		}
		if (gradosLibertadOponente == Integer.MAX_VALUE) {
			return gradosLibertad;
		}
		return 0;
	}
	
	private int evaluacionConGrupos(int gradosLibertad,
			int gradosLibertadOponente, int color, Tablero tablero) {
		
		int estimacion = gradosLibertad - gradosLibertadOponente;
		
		if ((tablero.getTurno() == color) && (gradosLibertadOponente == 1)) {
			estimacion = Constantes.VALOR_VICTORIA;
		}
		if ((tablero.getTurno() != color) && (gradosLibertad == 1)) {
			estimacion = Constantes.VALOR_DERROTA;
		}
		if ((tablero.getTurno() != color) && (gradosLibertadOponente == 1)) {
			estimacion = Constantes.PENALIZACION_ATARI;
		}
		if ((tablero.getTurno() == color) && (gradosLibertad == 1)) {
			estimacion = -Constantes.PENALIZACION_ATARI;
		}

		return estimacion;
	}

	private int resultadoJuego(int estadoJuego, int color) {
		switch (estadoJuego) {
			case Constantes.GANA_BLANCO:
				if (color == Constantes.BLANCO) {
					return Constantes.VALOR_VICTORIA;
				}else {
					return Constantes.VALOR_DERROTA;
				}
			case Constantes.GANA_NEGRO:
				if (color == Constantes.NEGRO) {
					return Constantes.VALOR_VICTORIA;
				}else {
					return Constantes.VALOR_DERROTA;
				}
			default:
				return 0;
		}
	}

}
