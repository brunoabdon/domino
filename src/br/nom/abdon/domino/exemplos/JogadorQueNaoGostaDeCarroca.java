package br.nom.abdon.domino.exemplos;

import java.util.ArrayList;
import java.util.List;

import br.nom.abdon.domino.Jogada;
import br.nom.abdon.domino.Jogador;
import br.nom.abdon.domino.Lado;
import br.nom.abdon.domino.Mesa;
import br.nom.abdon.domino.Numero;
import br.nom.abdon.domino.Pedra;

/**
 * {@link Jogador} que dá prioridade a jogar as carroças. Não tendo carroça, 
 * joga a primeira {@link Pedra} encontrar que caiba na {@link Mesa}.
 *
 * @author bruno
 *
 */
public class JogadorQueNaoGostaDeCarroca extends JogadorMamao {

    private Pedra[] carrocas;
    private int quantasCarrocasEuTenho;

    @Override
    public void recebeMao(Pedra[] mao) {
        /* Separando as carroças das nao carroças. 
           As carroças eu mantenho no array carrocas, cada uma guardada no 
           indice de seu número (carroça de limpo no carrocas[0], carroça de 
           terno no carrocas[3], etc...). Onde ficar null, é pq não tenho aquela 
           carroça.
           As não-carroças eu passo pra a super, que é um JogadorMamao, e 
           vai jogar alguma entre elas sempre que eu não tiver carroça pra
           jogar.
         */
        List<Pedra> naoCarrocas = new ArrayList<>();
        this.carrocas = new Pedra[7];
        quantasCarrocasEuTenho = 0;
        for (Pedra pedra : mao) {
            if (pedra.isCarroca()) {
                carrocas[pedra.getPrimeiroNumero().getNumeroDePontos()] = pedra;
                quantasCarrocasEuTenho++; //manter esse contador pra ajudar.
            } else {
                naoCarrocas.add(pedra);
            }
        }
        //as não carroças, passo pro super.
        super.recebeMao(naoCarrocas.toArray(new Pedra[naoCarrocas.size()]));
    }

    /**
     * Joga a maior carroça que puder jogar. Se não tiver carroça que dê pra 
     * jogar, joga como {@link JogadorMamao mamão} memso.
     * 
     * @return Uma {@link Pedra}, de preferência carroça.
     */
    @Override
    public Jogada joga() {
        Jogada jogada;

        if (quantasCarrocasEuTenho == 0) {
            //nem tenho carroça mais. simplificando e jogando feito mamão.
            jogada = super.joga();
        } else if (mesa.taVazia()) {
            //tenho carroca e é jogada inicial.... jogo a maior.
            jogada = fazJogadaDeMaiorCarroca();
        } else {
            /* Tenho algumas carroças. Vou ver da pra jogar alguma delas. Vou 
             dar preferência pra jogar o maior número primeiro. Se não tiver,
             jogo o menor. Se não tiver, então não tem como jogar carroça. 
             Jogo como mamão mesmo. */
            Lado ladoComMaiorNumero = pegaLadoComMaiorNumero(mesa);
            jogada = fazJogadaDeAlgumaCarroca(mesa, ladoComMaiorNumero);

            if (jogada == null) {
                //As carroças que eu tenho não cabem na mesa. Vou jogar como 
                //mamão mesmo.
                jogada = super.joga();
            }
        }
        //seja o que Deus quiser.
        return jogada;
    }

    /**
     * Diz qual o {@link Lado} da mesa tem o maior {@link Numero}. Se empatar, 
     * diz qualquer um.
     * 
     * @param mesa Uma mesa de uma partida que já começou. A mesa não pode
     * {@link Mesa#taVazia() estar vazia}.
     * 
     * @return O lado da mesa que tem o maior número (ou um lado qualquer se a
     * mesa estiver fechada.
     */
    private Lado pegaLadoComMaiorNumero(Mesa mesa) {
        return mesa.getNumeroEsquerda().compareTo(mesa.getNumeroDireita()) > 0 
                ? Lado.ESQUERDO 
                : Lado.DIREITO;
    }

    /**
     * Retorna uma {@link Jogada} de uma das carroças que tenho, dando 
     * preferência a um dos {@link Lado}s, caso seja possível jogar uma das 
     * carroças (retorna <code>null</code> se não rolar).
     * a ma
     * @param mesa Uma mesa de uma partida que já começou. A mesa não pode
     * {@link Mesa#taVazia() estar vazia}.
     * 
     * @param ladoPreferencial O lado que deve se dar preferencia pra jogar.
     * @return Uma jogada de carroça, ou <code>null</code> se não tiver carroça
     * pra isso.
     */
    private Jogada fazJogadaDeAlgumaCarroca(
            final Mesa mesa, final Lado ladoPreferencial) {
        
        Jogada jogada = pegaJogadaDeCarroca(mesa, ladoPreferencial);
        if (jogada == null) {
            
            Lado ladoDepreciado = 
                    ladoPreferencial == Lado.ESQUERDO 
                        ? Lado.DIREITO 
                        : Lado.ESQUERDO;
            
            jogada = pegaJogadaDeCarroca(mesa, ladoDepreciado);
        }
        return jogada;
    }

    /**
     * Faz uma {@link Jogada} inical com a maior carroça que eu tiver.
     * @return Uma jogada de carroça (sem dizer qual o {@link Lado}, já que é
     * pra ser jogada inicial).
     */
    private Jogada fazJogadaDeMaiorCarroca() {
        Jogada jogada = null;
        for (int i = 6; i >= 0; i--) {
            if (carrocas[i] != null) {
                jogada = fazJogadaCarroca(i);
                break;
            }
        }
        return jogada;
    }

    /**
     * Retorna uma {@link Jogada} de uma carroça que eu tenha num dos 
     * {@link Lado}s da mesa, ou <code>null</code> se eu não tiver a carroça do 
     * {@link Numero} daquele lado da {@link Mesa}.
     *
     * @param mesa A mesa onde se vai jogar. A partida já deve estar rolando e a
     * mesa não pode {@link Mesa#taVazia() estar vazia}.
     * 
     * @param ladoPraJogar O lado da mesa onde é pra jogar.
     * @return Uma jogada de carroça no lado dado, ou <code>null</code> se não 
     * tiver a carroça.
     */
    private Jogada pegaJogadaDeCarroca(
            final Mesa mesa, final Lado ladoPraJogar) {

        final Jogada jogada;

        final Numero numeroDaCabeca
                = ladoPraJogar == Lado.ESQUERDO
                ? mesa.getNumeroEsquerda()
                : mesa.getNumeroDireita();

        final int indexNoArrayDeCarrocas = numeroDaCabeca.getNumeroDePontos();
        final Pedra carroca = carrocas[indexNoArrayDeCarrocas];

        if (carroca != null) {
            jogada = fazJogadaCarroca(indexNoArrayDeCarrocas, ladoPraJogar);
        } else {
            jogada = null;
        }
        return jogada;
    }

    /**
     * Cria uma {@link Jogada} inicial (sem {@link Lado}) pra a carroça que tá 
     * no dado indice do array de carroças, e tira a carroça do array, pra não
     * usar mais ela. Diminui também o contador de quantas carroças eu tenho. 
     * 
     * @param indexNoArrayDeCarrocas O indice da carroça no array.
     * @return Uma jogada inical com aquela carroça.
     */
    private Jogada fazJogadaCarroca(final int indexNoArrayDeCarrocas) {
        Pedra carroca = tiraCarrocaDoArray(indexNoArrayDeCarrocas);
        return new Jogada(carroca);
    }

    /**
     * Cria uma {@link Jogada} pra a carroça que tá 
     * no dado indice do array de carroças, em um {@link Lado} específico da 
     * {@link Mesa}, e tira a carroça do array, pra não usar mais ela. Diminui
     * também o contador de quantas carroças eu tenho. 
     * 
     * @param indexNoArrayDeCarrocas O indice da carroça no array.
     * @return Uma jogada inical com aquela carroça.
     */
    private Jogada fazJogadaCarroca(
            final int indexNoArrayDeCarrocas, final Lado ladoPraJogar) {
        
        Pedra carroca = tiraCarrocaDoArray(indexNoArrayDeCarrocas);
        return new Jogada(carroca, ladoPraJogar);
    }

    /**
     * Tira e retorna a carroça que tá num índice específico do array, 
     * diminuindo também o contador de quantas carroças eu tenho. 
     * 
     * @param indexNoArrayDeCarrocas O indice da carroça no array.
     * @return A carroça que tava lá.
     */
    private Pedra tiraCarrocaDoArray(final int indexNoArrayDeCarrocas) {
        final Pedra carroca = this.carrocas[indexNoArrayDeCarrocas];
        this.carrocas[indexNoArrayDeCarrocas] = null;
        this.quantasCarrocasEuTenho--;
        return carroca;
    }
}