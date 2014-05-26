package br.nom.abdon.domino.ui.fx;

class Localizacao {
	private int x, y;
	
	public Localizacao(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Localizacao)) return false;
			Localizacao that = (Localizacao) obj;
			return this.getX() == that.getX() && this.getY() == that.getY();
	}
	
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

}
