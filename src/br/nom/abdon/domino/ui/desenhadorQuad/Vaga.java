package br.nom.abdon.domino.ui.desenhadorQuad;

import br.nom.abdon.domino.ui.Direcao;

class Vaga {

	private Localizacao localizacao;
	private boolean ehDeCarroca;
	private Direcao direcao;

	public Vaga(int x, int y, Direcao direcao, boolean ehDeCarroca) {
		this(new Localizacao(x,y),direcao,ehDeCarroca);
	}
	
	public Vaga(Localizacao localizacao, Direcao direcao, boolean ehDeCarroca) {
		this.localizacao = localizacao;
		this.direcao = direcao;
		this.ehDeCarroca = ehDeCarroca;
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
	public boolean ehDeCarroca() {
		return ehDeCarroca;
	}
	public void setEhDeCarroca(boolean ehDeCarroca) {
		this.ehDeCarroca = ehDeCarroca;
	}
	public Direcao getDirecao() {
		return direcao;
	}
	public void setDirecao(Direcao direcao) {
		this.direcao = direcao;
	}
	public Localizacao getLocalizacao() {
		return localizacao;
	}
	public void setLocalizacao(Localizacao localizacao) {
		this.localizacao = localizacao;
	}
	
}
