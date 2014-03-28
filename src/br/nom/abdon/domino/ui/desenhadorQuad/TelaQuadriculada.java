package br.nom.abdon.domino.ui.desenhadorQuad;

import br.nom.abdon.domino.Lado;
import br.nom.abdon.domino.ui.Direcao;


class TelaQuadriculada {

	private final int quadradosPorLinha, quadradosPorColuna;
	private Vaga ultimaVagaUsadaEsquerda, ultimaVagaUsadaDireita;
	
	public TelaQuadriculada(final int quadradosPorLinha, final int quadradosPorColuna) {
		this.quadradosPorLinha = quadradosPorLinha;
		this.quadradosPorColuna = quadradosPorColuna;
		
	}
	
	public Vaga getVaga(final Lado lado, final boolean ehCarroca) {
		Vaga vaga;
		if(ultimaVagaUsadaEsquerda == null){
			//primeira chamada
			vaga = primeiraVaga(ehCarroca);
			
			ultimaVagaUsadaEsquerda = vaga;
			ultimaVagaUsadaDireita = pegaVagaComSentidoInverso(vaga);
		} else {
			final boolean ehPraEsquerda = lado == Lado.ESQUERDO;
			final Vaga ultimaVagaUsadaNesseLado = ehPraEsquerda ? ultimaVagaUsadaEsquerda : ultimaVagaUsadaDireita;
			
			Direcao direcao = ultimaVagaUsadaNesseLado.getDirecao();
			final boolean andandoNoSentidoDecrescenteDasCoordenadas = 
					direcao == Direcao.PRA_ESQUERDA || direcao == Direcao.PRA_CIMA;

			
			vaga = aindaCabe(ultimaVagaUsadaNesseLado, andandoNoSentidoDecrescenteDasCoordenadas) 
					? proximaVaga(ultimaVagaUsadaNesseLado,andandoNoSentidoDecrescenteDasCoordenadas, ehCarroca)
                    : proximaVagaFazendoCurva(ultimaVagaUsadaNesseLado, andandoNoSentidoDecrescenteDasCoordenadas);
					
			if(ehPraEsquerda){
				ultimaVagaUsadaEsquerda = vaga;
			} else {
				ultimaVagaUsadaDireita = vaga;
			}
			
		}
		
		return vaga;
	}

	/**
	 * Retorna a primeira vaga, que vai ser bem no meiozinho da mesa (mais
	 * ou menos). 
	 * 
	 * @param ehDeCarroca Se é pra ser vaga pra carroça ou não.
	 * @return
	 */
	private Vaga primeiraVaga(final boolean ehDeCarroca) {

		final int xInicial = quadradosPorLinha / 2 ;
		final int yInicial = quadradosPorColuna / 2 ;

		return new Vaga(xInicial, yInicial, Direcao.PRA_ESQUERDA, ehDeCarroca);
	}

	/**
	 * Diz se ainda tem vaga pra pelo menos mais uma pedra (de carroça ou não) do 
	 * lado dessa vaga, sem ter que fazer curva.
	 * 
	 * @param vaga A vaga que se está testando pra ver se já estava no limite 
	 * da mesa.
	 * @param andandoNoSentidoDecrescenteDasCoordenadas 
	 * 
	 * @return <code>true</code>, se ainda rolar colocar mais uma Pedra do lado
	 * dessa vaga, ou <code>false</code> se tiver já na hora de fazer a curva;
	 */
	private boolean aindaCabe(final Vaga vaga, boolean andandoNoSentidoDecrescenteDasCoordenadas) {
		final boolean cabe;

		final Direcao direcao = vaga.getDirecao();
		
		final int coordenadaRelevante = direcao.ehHorizontal() ? vaga.getX() : vaga.getY();
		final int espacoDeSeguranca = vaga.ehDeCarroca() ? 3 : 4;
		
		if(andandoNoSentidoDecrescenteDasCoordenadas){
			cabe = coordenadaRelevante >= espacoDeSeguranca;
		} else {
			cabe = coordenadaRelevante <= this.quadradosPorLinha - espacoDeSeguranca;
		}

		return cabe;
	}

	/**
	 * Retorna a próxima {@link Vaga}, a partir de uma vaga dada (que deve ter sido a
	 * última usada nessa cabeça, assumindo que {@link #aindaCabe(Vaga) tem espaço pra 
	 * mais uma pedra}.
	 * 
	 * @param vagaAnterior A vaga a partir do qual se vai pegar a próxima.
	 * 
	 * @param ehPraCarroca Se a próxima vaga que se quer é ou não pra carroça.
	 * @param ehCarroca 
	 * 
	 * @return Uma vaga pra ser usada.
	 */
	private Vaga proximaVaga(final Vaga vagaAnterior, final boolean andandoNoSentidoDecrescenteDasCoordenadas, final boolean ehPraCarroca) {
		
		final int xAnterior = vagaAnterior.getX();
		final int yAnterior = vagaAnterior.getY();
		
		final Direcao direcao = vagaAnterior.getDirecao();
		final boolean foiCarroca = vagaAnterior.ehDeCarroca();
		
		final boolean ehHorizontal = direcao.ehHorizontal();
		int distancia = (ehPraCarroca || !foiCarroca)? 2 : 1;
		
		if(andandoNoSentidoDecrescenteDasCoordenadas) distancia *=-1;
		
		final int coordenadaPraAlterar = (ehHorizontal ? xAnterior : yAnterior) + distancia;
		
		final Localizacao localizacao = fazLocalizacao(xAnterior, yAnterior, ehHorizontal, coordenadaPraAlterar);

		return new Vaga(localizacao, direcao, ehPraCarroca);
		
	}

	private Vaga proximaVagaFazendoCurva(final Vaga vagaAnterior, boolean andandoNoSentidoDecrescenteDasCoordenadas) {

		final int xAnterior = vagaAnterior.getX();
		final int yAnterior = vagaAnterior.getY();

		final Direcao direcaoAnterior = vagaAnterior.getDirecao();
		final boolean ehHorizontal = direcaoAnterior.ehHorizontal();

		final int incremento  = andandoNoSentidoDecrescenteDasCoordenadas ? -2 : 2;
		
		final int coordenadaPraAlterar = (ehHorizontal ? yAnterior : xAnterior) + incremento;
		
		final Direcao direcao = proximaDirecao(direcaoAnterior);
		
		final Localizacao localizacao = fazLocalizacao(xAnterior, yAnterior, ehHorizontal, coordenadaPraAlterar);

		return new Vaga(localizacao, direcao, false);
	}

	
	private Direcao proximaDirecao(final Direcao direcao) {
		return direcao == Direcao.PRA_ESQUERDA? Direcao.PRA_CIMA    
		     : direcao == Direcao.PRA_CIMA?     Direcao.PRA_DIREITA 
		     : direcao == Direcao.PRA_DIREITA?  Direcao.PRA_BAIXO   
		     : Direcao.PRA_ESQUERDA;
	}

	private Vaga pegaVagaComSentidoInverso(final Vaga vaga) {
		return new Vaga(vaga.getLocalizacao(),
						vaga.getDirecao().inverver(), 
						vaga.ehDeCarroca());
	}
	
	private Localizacao fazLocalizacao(
			final int xAnterior, 
			final int yAnterior, 
			final boolean ehHorizontal, 
			final int novoValorCoordenada) {

		Localizacao localizacao = ehHorizontal 
				? new Localizacao(novoValorCoordenada, yAnterior)
				: new Localizacao(xAnterior, novoValorCoordenada);
		return localizacao;
	}

	
}
