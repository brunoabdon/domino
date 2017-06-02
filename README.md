# domino

Jogo de Dominó pra Programadores.

Você implementa sua classe pra jogar com outras. 

Javadoc disponível em http://brunoabdon.github.io/domino/apidocs/

## Quickstart:

Você precisa ter a [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) e o [Maven](https://maven.apache.org/) instalado (e o [git](https://git-scm.com/) também).

Baixar os fontes
``` 
$ git https://github.com/brunoabdon/domino.git
``` 
Entrar no diretório
``` 
$ cd domino
``` 
Compilar
``` 
$ mvn compile
``` 
Rodar uma partida
``` 
$ mvn exec:java
``` 
Isso vai rodar uma partida com configurações defaults entre quatro implementações simples de Jogadores. O jogo dura menos de 2 segundos e vai sendo impresso no terminal ([exemplo de um jogo impresso](https://gist.github.com/brunoabdon/2821affbc692fe006947630d51de8dba)).

## Escolhendo as IAs que vão jogar
Copie o arquivo [`domino-config.xml`](https://gist.github.com/brunoabdon/6dd3e52167c3fc23a0e63babc84632d8) no diretório corrente e defina nele quais classes de Jogadores irão jogar contra quais outras.

## Implementando seu próprio [Jogador](http://brunoabdon.github.io/domino/apidocs/com/github/abdonia/domino/Jogador.html)
Considere usar o archetype maven [`domino-artchetype-jogador`](https://github.com/brunoabdon/domino-archetype-jogador) para criar um *projeto starter* pra seu Jogador.

O *Javadoc* é bem completo e está disponível em:
http://brunoabdon.github.io/domino/apidocs/

Um bom ponto inicial é a [referência pra a interface Jogador](http://brunoabdon.github.io/domino/apidocs/com/github/abdonia/domino/Jogador.html).

Olhe também as implementações de exemplo no pacote [`com.github.abdonia.domino.exemplos`](https://github.com/brunoabdon/domino/tree/master/src/main/java/com/github/abdonia/domino/exemplos).
