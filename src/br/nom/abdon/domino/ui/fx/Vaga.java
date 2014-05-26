package br.nom.abdon.domino.ui.fx;

import java.util.Objects;

class Vaga {

	private final Localizacao localizacao;
	private final Direcao direcao;

	public Vaga(int x, int y, Direcao direcao) {
		this(new Localizacao(x,y),direcao);
	}
	
	public Vaga(Localizacao localizacao, Direcao direcao) {
		this.localizacao = localizacao;
		this.direcao = direcao;
	}
	
	public int getX() {
		return getLocalizacao().getX();
	}
	public void setX(int x) {
		this.getLocalizacao().setX(x);
	}
	public int getY() {
		return getLocalizacao().getY();
	}
	public void setY(int y) {
		this.getLocalizacao().setY(y);
	}
	public Direcao getDirecao() {
		return direcao;
	}
	public Localizacao getLocalizacao() {
		return localizacao;
	}
	@Override
	public String toString() {
		return "[" + localizacao + "," + direcao + ")]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Vaga)) return false;
		
		Vaga that = (Vaga) obj;
		return this.getDirecao() == that.getDirecao() 
				&& this.getLocalizacao().equals(that.getLocalizacao());
	}

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.localizacao);
        hash = 47 * hash + Objects.hashCode(this.direcao);
        return hash;
    }
	
}
