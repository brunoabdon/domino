# domino 🁏

**domino** é um "[Programming game](https://en.wikipedia.org/wiki/Programming_game)" 
onde os jogadores são classes programadas pra jogar um Jogo de Dominó, de acordo com as regras tradicionais de Recife.

## Quickstart:

Você precisa ter a [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) e o [Maven](https://maven.apache.org/) instalado (e o [git](https://git-scm.com/) também).

Em 4 passos: Baixar os fontes, entrar no diretório, compilar e rodar:
``` 
$ git clone https://github.com/brunoabdon/domino.git
$ cd domino
$ mvn compile
$ mvn exec:java
``` 
Isso vai rodar uma partida, com configurações default, entre quatro implementações simples de Jogadores. O jogo dura menos de 2 segundos e vai sendo impresso no terminal ([exemplo de um jogo impresso](https://gist.github.com/brunoabdon/2821affbc692fe006947630d51de8dba)).

## Escolhendo as IAs que vão jogar
Copie o arquivo [`domino-config.yaml`](https://gist.githubusercontent.com/brunoabdon/3f1847fd6acecd4c624c84e058e88110/raw/3b0971588aeaab4d0b87f93a72a6391e147abfa8/domino-config.yaml) no diretório corrente e configure nele quais classes de Jogadores irão jogar contra quais outras.

## Implementando seu próprio [Jogador](http://brunoabdon.github.io/domino/apidocs/com/github/abdonia/domino/Jogador.html)
Considere usar o archetype maven [`domino-artchetype-jogador`](https://github.com/brunoabdon/domino-archetype-jogador) para criar um *projeto starter* pra seu Jogador.

O [**Javadoc**](http://brunoabdon.github.io/domino/apidocs/) está completo e detalhado.

Olhe também o código fonte das implementações de exemplo no pacote [`com.github.abdonia.domino.exemplos`](https://github.com/brunoabdon/domino/tree/master/src/main/java/com/github/abdonia/domino/exemplos).
