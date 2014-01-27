package br.nom.abdon.domino.exemplos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.nom.abdon.domino.Jogada;
import br.nom.abdon.domino.Jogador;
import br.nom.abdon.domino.Lado;
import br.nom.abdon.domino.Mesa;
import br.nom.abdon.domino.Numero;
import br.nom.abdon.domino.Pedra;

public class JogadorMamao implements Jogador {

	private List<Pedra> mao;

	@Override
	public void recebeMao(Pedra[] mao) {
		this.mao = new ArrayList<>(6);
		Collections.addAll(this.mao, mao);
	}

	@Override
	public Jogada joga(Mesa mesa) {
		Jogada jogada;

		Pedra pedraPraJogar = null;
		Lado ladoDeJogar = null;
		
		if(mesa.taVazia()){
			if(mao.size() == 6){
				pedraPraJogar = aMaiorCarroca();
			} else {
				pedraPraJogar = mao.get(0); //joga a primeira que tiver
			}
		} else {
			Numero numeroEsquerda = mesa.getNumeroEsquerda();
			Numero numeroDireita = mesa.getNumeroDireita();
			int i;
			for (Pedra pedraDaMao : mao) {
				if(pedraDaMao != null){
					if(pedraDaMao.temNumero(numeroEsquerda)){
						pedraPraJogar = pedraDaMao;
						ladoDeJogar = Lado.ESQUERDO;
						break;
					} else if (pedraDaMao.temNumero(numeroDireita)){
						pedraPraJogar = pedraDaMao;
						ladoDeJogar = Lado.DIREITO;
						break;
					}
				}
			}
		}
		
		if(pedraPraJogar == null){ //lasquei-me
			jogada = Jogada.TOQUE;
		} else {
			mao.remove(pedraPraJogar);
			jogada = new Jogada(pedraPraJogar,ladoDeJogar);
		}
		
		return jogada; 
	}

	private Pedra aMaiorCarroca() {
		Pedra maiorCarroca = null;
		for (Pedra pedra : mao) {
			if (pedra.isCarroca()) {
				if(maiorCarroca == null || (pedra.compareTo(maiorCarroca) >= 1)){
					maiorCarroca = pedra;
				}
			}
		}
		
		return maiorCarroca;
	}

	@Override
	public int vontadeDeComecar() {
		return 5;
	}
	
	@Override
	public String toString() {
		return "mamao";
	}


}
