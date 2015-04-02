package org.joliverie.sio.slam.worldstat;

public class Pays {
	private String id;
	private String name;
	private String capitale;
	private float esperanceDeVie;

	public Pays() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCapitale() {
		return capitale;
	}

	public void setCapitale(String capitale) {
		this.capitale = capitale;
	}

	public float getEsperanceDeVie() {
		return esperanceDeVie;
	}

	public void setEsperanceDeVie(float esperanceDeVie) {
		this.esperanceDeVie = esperanceDeVie;
	}

	@Override
	public String toString() {
		return name + "["+id+"]";
	}

}
