package br.nom.abdon.domino.ui.fx;

public enum Direcao {
    PRA_ESQUERDA(270), PRA_BAIXO(0), PRA_DIREITA(90), PRA_CIMA(180);

    private final double graus;    
    
    private Direcao(double graus) {
        this.graus = graus;
    }

    public double getGraus() {
        return this.graus;
    }
        
	public Direcao inverver(){
		
		Direcao direcao;
		
		switch(this){
			case PRA_BAIXO:    direcao = PRA_CIMA; break;
			case PRA_CIMA:     direcao = PRA_BAIXO; break;
			case PRA_DIREITA:  direcao = PRA_ESQUERDA; break;
			case PRA_ESQUERDA: direcao = PRA_DIREITA; break;
			default: throw new IllegalStateException();
		}
		
		return direcao;
	}
	
	public boolean ehHorizontal(){
		return this == PRA_DIREITA || this == PRA_ESQUERDA;
	}
	
	public boolean ehVertical(){
		return !ehHorizontal();
	}
	
	@Override
	public String toString() {
		String str;
		switch (this) {
			case PRA_ESQUERDA: str = "<-"; break;
			case PRA_BAIXO: str = "v"; break;
			case PRA_CIMA: str = "^"; break;
			case PRA_DIREITA: str = "->"; break;
			default: throw new IllegalStateException();
		}
		return str;
	}
}
