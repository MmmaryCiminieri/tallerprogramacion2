package juego;

import java.util.HashSet;
import java.util.Set;

public class Grupo implements Cloneable {
	private Set<Posicion> fichas;
	private Set<Posicion> libertades;
	
	public Grupo() {
		fichas = new HashSet<Posicion>();
		libertades = new HashSet<Posicion>();
	}
	
	public int cantidadLibertades(){
		return libertades.size();
	}

	public void agregarFicha(Posicion posicion) {
		fichas.add(posicion);
	}
	
	public void agregarLibertad(Posicion posicion) {
		if (posicion.getTurno() == Constantes.VACIO) {
			libertades.add(posicion);
		}
	}
	
	public boolean esLibertad(int x, int y) {
		Posicion aux = new Posicion(x, y, Constantes.VACIO);
		return libertades.contains(aux);
	}
	
	public Grupo fusionar(Grupo otroGrupo) {
		Grupo union = new Grupo();
		union.fichas.addAll(this.fichas);
		union.fichas.addAll(otroGrupo.fichas);
		union.libertades.addAll(this.libertades);
		union.libertades.addAll(otroGrupo.libertades);
		
		return union;
	}

	public void sacarLibertad(Posicion posicion) {
		libertades.remove(posicion);
	}

	@Override
	public Object clone() {
		Grupo grupo = null;
		try {
			grupo = (Grupo) super.clone();
		} catch (CloneNotSupportedException e) {}
		
		grupo.fichas = new HashSet<Posicion>();
		for (Posicion ficha : this.fichas) {
			grupo.fichas.add((Posicion)ficha.clone());
		}
		
		grupo.libertades = new HashSet<Posicion>();
		for (Posicion libertad : this.libertades) {
			grupo.libertades.add((Posicion)libertad.clone());
		}
		
		return grupo;
	}
}