package br.nom.abdon.domino.app;


public class DominoAppException extends Exception {

	public DominoAppException(Exception e, String msg) {
		super(msg,e);
	}

	public DominoAppException(String msg) {
		super(msg);
	}

}
