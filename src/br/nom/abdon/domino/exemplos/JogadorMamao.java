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

/**
 * {@link Jogador} mais simplório possível. Procura a primeira 
 * {@link Pedra} na mão que dá pra jogar na mesa e joga. 
 * 
 * @author bruno
 *
 */
public class JogadorMamao implements Jogador {

	private List<Pedra> mao;

	boolean perguntouSeEuQueriaJogar = false;
	
	
	@Override
	public void recebeMao(Pedra[] mao) {
		//guardar como uma List
		this.mao = new ArrayList<>(6);
		Collections.addAll(this.mao, mao);
	}

	@Override
	public Jogada joga(Mesa mesa) {
		Jogada jogada;

		Pedra pedraPraJogar = null;
		Lado ladoDeJogar = null;
		
		if(mesa.taVazia()){
			//opa! minha vez e a mesa tah vazia? é pra comecar agora! sou o primeiro
			if(!perguntouSeEuQueriaJogar){
				//se nao me perguntaram se eu queria ser o da dupla a comecar a jogar, 
				//entao essa é a primeira partida, e o sistema jah se ligou que o
				//jogador que tem a maior carroca sou eu. Tenho que jogar ela, se nao
				//é roubo.
				pedraPraJogar = aMaiorCarroca();
			} else {
				//ah, eles perguntaram se eu queria ser o da dupla a comecar a jogar,
				//e acabou que vou ser eu mesmo a comecar. nao precisa ser carroca.
				//vou jogar a primeira que tiver.
				pedraPraJogar = mao.get(0); 
			}
		} else {
			//a mesa nao tah vazia. o jogo tah rolando. tenho que jogar uma peca
			//que se encaixe ou no lado esquerdo ou no direito dos dominos.
			
			//deixa eu ver quais sao os numeros
			Numero numeroEsquerda = mesa.getNumeroEsquerda();
			Numero numeroDireita = mesa.getNumeroDireita();
			//hum, otimo. agora deixa achar alguma pedra na minha mao
			//que tenha algum dessezs numeroos.
			for (Pedra pedraDaMao : mao) {
				if(pedraDaMao.temNumero(numeroEsquerda)){
					//opa, achei uma. vai ser ela mesmo.
					pedraPraJogar = pedraDaMao;
					//jogar do lado esquerdo, que foi o que eu vi que tenho o numero
					ladoDeJogar = Lado.ESQUERDO; 
					//precisa nem procurar mais.
					break;
				} else if (pedraDaMao.temNumero(numeroDireita)){
					//opa, achei uma. vai ser ela mesmo.
					pedraPraJogar = pedraDaMao;
					//jogar do lado direito, que foi o que eu vi que tenho o numero
					ladoDeJogar = Lado.DIREITO;
					//precisa nem procurar mais.
					break;
				}
			}
		}
		
		if(pedraPraJogar == null){ 
			//lasquei-me. nao achei nenhuma. toc toc
			jogada = Jogada.TOQUE;
		} else {
			//pronto, vou tirar essa pedra da minha mao, pra nao jogar duplicada depois
			mao.remove(pedraPraJogar);
			//criando a jogada: a pedra que escolhi e a cabeca em que vou colocar
			jogada = new Jogada(pedraPraJogar,ladoDeJogar);
		}
		//retornando a jogada
		return jogada; 
	}

	/**
	 * Retorna qual é a maior {@link Pedra#isCarroca() carroca} que eu 
	 * tenho na mão. Tem que ser essa {@link Pedra} pra jogar quando 
	 * sou o primeiro a jogar numa primeira partida.
	 *  
	 * @return Uma carroca
	 */
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

	/**
	 * Nao sei se comece... nao sei se nao comece... vai 5 mesmo.
	 * 
	 * @return 5
	 */
	@Override
	public int vontadeDeComecar() {
		return 5;
	}
}
