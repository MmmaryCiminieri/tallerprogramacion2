package juego;

import java.util.ArrayList;
import java.util.List;

public class AdministradorGrupos implements Cloneable {
	private List<Grupo> gruposNegros;
	private List<Grupo> gruposBlancos;
	
	public AdministradorGrupos() {
		gruposNegros = new ArrayList<Grupo>();
		gruposBlancos = new ArrayList<Grupo>();
	}
	
	public void agregarFicha(Posicion posicion, List<Posicion> libertades) {
		reducirLibertadesEnemigos(posicion);
		actualizarGruposAmigos(posicion, libertades);
	}
	
	public int minimoGradoLibertad(int color) {
		List<Grupo> gruposColor = null;
		if (color == Constantes.BLANCO) {
			gruposColor = gruposBlancos;
		}else {
			gruposColor = gruposNegros;
		}
		
		int minimoGradoLibertad = Integer.MAX_VALUE;
		for (Grupo grupo : gruposColor) {
			if (grupo.cantidadLibertades() < minimoGradoLibertad) {
				minimoGradoLibertad = grupo.cantidadLibertades();
			}
		}
		return minimoGradoLibertad;
	}
	
	public List<Integer> gradosLibertadAmigosVecinos(Posicion posicion) {
		List<Integer> gradosLibertad = new ArrayList<Integer>();
		
		List<Grupo> gruposAmigos = obtenerGruposAmigos(posicion);
		for (Grupo amigo : gruposAmigos) {
			if (esVecino(posicion, amigo)) {
				gradosLibertad.add(amigo.cantidadLibertades());
			}
		}
		return gradosLibertad;
	}

	public List<Integer> gradosLibertadEnemigosVecinos(Posicion posicion) {
		List<Integer> gradosLibertad = new ArrayList<Integer>();
		
		List<Grupo> gruposEnemigos = obtenerGruposEnemigos(posicion);
		for (Grupo enemigo : gruposEnemigos) {
			if (esVecino(posicion, enemigo)) {
				gradosLibertad.add(enemigo.cantidadLibertades());
			}
		}
		return gradosLibertad;
	}
	
	@Override
	public Object clone() {
		AdministradorGrupos administrador = null;
		try {
			administrador = (AdministradorGrupos)super.clone();
		} catch (CloneNotSupportedException e) {}
		
		administrador.gruposNegros = new ArrayList<Grupo>();
		for (Grupo grupoNegro : gruposNegros) {
			administrador.gruposNegros.add((Grupo)grupoNegro.clone());
		}
		
		administrador.gruposBlancos = new ArrayList<Grupo>();
		for (Grupo grupoBlanco : gruposBlancos) {
			administrador.gruposBlancos.add((Grupo)grupoBlanco.clone());
		}
		
		return administrador;
	}

	private void actualizarGruposAmigos(Posicion posicion, List<Posicion> libertades) {
		List<Grupo> gruposAmigos = obtenerGruposAmigos(posicion);
		List<Grupo> amigosVecinos = new ArrayList<Grupo>();
		Grupo nuevoGrupo = crearGrupo(posicion, libertades);
		
		for (Grupo grupo : gruposAmigos) {
			if (esVecino(posicion, grupo)) {
				grupo.sacarLibertad(posicion);
				nuevoGrupo = grupo.fusionar(nuevoGrupo);
				amigosVecinos.add(grupo);
			}
		}
		gruposAmigos.removeAll(amigosVecinos);
		gruposAmigos.add(nuevoGrupo);
	}

	private boolean esVecino(Posicion posicion, Grupo grupo) {
		return grupo.esLibertad(posicion.getX(), posicion.getY());
	}

	private Grupo crearGrupo(Posicion posicion, List<Posicion> libertades) {
		Grupo grupo = new Grupo();
		grupo.agregarFicha(posicion);
		for (Posicion libertad : libertades) {
			grupo.agregarLibertad(libertad);
		}
		return grupo;
	}

	private List<Grupo> obtenerGruposAmigos(Posicion posicion) {
		if (posicion.getTurno() == Constantes.NEGRO) {
			return gruposNegros;
		}
		if (posicion.getTurno() == Constantes.BLANCO) {
			return gruposBlancos;
		}
		return null;
	}

	private void reducirLibertadesEnemigos(Posicion posicion) {
		List<Grupo> gruposEnemigos = obtenerGruposEnemigos(posicion);
		for (Grupo grupo : gruposEnemigos) {
			grupo.sacarLibertad(posicion);
		}
	}

	private List<Grupo> obtenerGruposEnemigos(Posicion posicion) {
		if (posicion.getTurno() == Constantes.NEGRO) {
			return gruposBlancos;
		}
		if (posicion.getTurno() == Constantes.BLANCO) {
			return gruposNegros;
		}
		return null;
	}
}
