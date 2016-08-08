package br.nom.abdon.domino.app;


public class DominoAppException extends Exception {

	public DominoAppException(final Exception e, final String msg) {
		super(msg,e);
	}

	public DominoAppException(final String msg) {
		super(msg);
	}
}
