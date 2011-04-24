package juego;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EstrategiaAlfaBeta implements Estrategia {

	private Estimador estimador;
	private int profundidadMaxima;
	private int cantidadMaximaCandidatos;
	
	private class JugadaEstimada {
		private Posicion jugada;
		private int estimacion;
		
		public JugadaEstimada(Posicion jugada, int estimacion) {
			this.jugada = jugada;
			this.estimacion = estimacion;
		}
	}
	
    private class ComparatorJugadaEstimada implements Comparator<JugadaEstimada> {
    	public int compare(JugadaEstimada jugada1, JugadaEstimada jugada2) {
            if (jugada1.estimacion > jugada2.estimacion)
                return -1;
            else if (jugada1.estimacion == jugada2.estimacion)
                return 0;
            else
                return 1;
        }
    }
	
	public EstrategiaAlfaBeta(Estimador estimador, int profundidadMaxima, int cantidadMaximaCandidatos) {
		this.estimador = estimador;
		this.profundidadMaxima = Math.max(profundidadMaxima, 1);
		this.cantidadMaximaCandidatos = Math.max(cantidadMaximaCandidatos, 1);
	}
	
	@Override
	public Posicion obtenerMejorJugada(Tablero tablero) {
		JugadaEstimada jugadaEstimada = expandirAlfaBeta(tablero, profundidadMaxima, 
				Integer.MIN_VALUE, Integer.MAX_VALUE);
		return jugadaEstimada.jugada;
	}

	private JugadaEstimada expandirAlfaBeta(Tablero tablero,
			int profundidadMaxima, int alfa, int beta) {
		int turno = tablero.getTurno();
		
		JugadaEstimada mejorAlMomento = new JugadaEstimada(null, Integer.MIN_VALUE);
		
		if ((profundidadMaxima == 0) || (tablero.getEstado() != Constantes.EN_PROGRESO)) {
			mejorAlMomento.estimacion = estimador.estimarSituacionActual(tablero, turno);
			return mejorAlMomento;
		}
		
		List<Posicion> jugadasCandidatas = obtenerCandidatosOrdenados(tablero, turno, estimador);
		if (jugadasCandidatas.size() == 0) {
			mejorAlMomento.estimacion = estimador.estimarSituacionActual(tablero, turno);
			return mejorAlMomento;
		}
		
		int cantidadCandidatos = (cantidadMaximaCandidatos > jugadasCandidatas.size() ?   
              jugadasCandidatas.size() : cantidadMaximaCandidatos); 
		for (int i = 0 ; i < cantidadCandidatos ; i++) {
			Posicion jugadaCandidata = jugadasCandidatas.get(i);
			Tablero tableroSiguiente = tablero.agregarJugada(jugadaCandidata);
			
			if (tableroSiguiente != null) {
	            int estimacionCandidata = -(expandirAlfaBeta(tableroSiguiente, profundidadMaxima - 1, 
	            		-beta, -alfa).estimacion);
	            
	            if (mejorAlMomento.estimacion < estimacionCandidata) {
	            	mejorAlMomento.estimacion = estimacionCandidata;
	            	mejorAlMomento.jugada = jugadaCandidata;
	            }
	            
	            if (alfa < estimacionCandidata) {
	            	alfa = estimacionCandidata;
	            }
	            
	            if (alfa >= beta) {
	            	mejorAlMomento.estimacion = alfa;
	            	return mejorAlMomento;
	            } 
			}
		}
		mejorAlMomento.estimacion = alfa;
		return mejorAlMomento;
	}

	private List<Posicion> obtenerCandidatosOrdenados(Tablero tablero,
			int turno, Estimador estimador) {
		List<Posicion> candidatos = tablero.obtenerJugadasPosibles(Constantes.oponente(turno));
		List<JugadaEstimada> candidatosEstimados = new ArrayList<JugadaEstimada>();
		List<Posicion> resultado = new ArrayList<Posicion>();
		
		for (Posicion candidato : candidatos) {
			Tablero proximoTablero = tablero.agregarJugada(candidato);
			if (proximoTablero != null) {
				int estimacion = estimador.estimarSituacionActual(proximoTablero, turno);
				candidatosEstimados.add(new JugadaEstimada(candidato, estimacion));
			}
		}
		
		Collections.sort(candidatosEstimados, new ComparatorJugadaEstimada());
		for (JugadaEstimada jugadaEstimada : candidatosEstimados) {
			resultado.add(jugadaEstimada.jugada);
		}
		
		return resultado;
	}

}
