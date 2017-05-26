/*
 * Copyright (C) 2017 Bruno Abdon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.abdonia.domino.app;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardOpenOption.READ;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.abdonia.domino.Jogador;
import com.github.abdonia.domino.eventos.DominoEventListener;
import com.github.abdonia.domino.motor.DominoConfig;
import com.github.abdonia.domino.motor.DominoConfigException;
import com.github.abdonia.domino.motor.Jogo;

/**
 * <p>Uma aplicação simples. por linha de comando, que roda um {@link Jogo jogo de
 * dominó}.</p>
 * 
 * <p>As {@link DominoConfig configurações do Jogo} (isto é, quais implementações
 * de jogadores serão usadas, quais serão seus nomes, etc) pode ser informada
 * num arquivo chamado <code>domino-config.xml</code> que deve estar no 
 * diretório atual.</p>
 * 
 * <p>Um exemplo de conteudo do arquivo é:</p>
 * 
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;
 * &lt;domino&gt;
 * &lt;duplas&gt;
 *   &lt;dupla&gt; 
 *        &lt;jogador nome="Amanda" classe="com.example.Jogador"/&gt;
 *        &lt;jogador nome="Bruno" classe="com.acme.Jogador"/&gt;
 *     &lt;/dupla&gt;
 *     &lt;dupla&gt;
 *        &lt;jogador nome="Igor" classe="com.foo.bar.Jogador"/&gt;
 *        &lt;jogador nome="Ronaldo" classe="com.example.Jogador"/&gt;
 *     &lt;/dupla&gt;
 *  &lt;/duplas&gt;
 *
 *  &lt;event-listeners&gt;
 *      &lt;event-listener classe="com.github.abdonia.domino.log.LoggerDominoEventListener"/&gt;
 *      &lt;event-listener classe="com.acme.GuiEventListener"/&gt;
 *  &lt;/event-listeners&gt;
 *&lt;/domino&gt;
 * </pre>
 * 
 * <p>Caso o arquivo de configuração não seja encontrado, umaconfiguração 
 * default é usada onde ocorre um jogo entre algumas 
 * {@link com.github.abdonia.domino.exemplos implementações exemplo de 
 * jogadores} e onde um 
 * {@link com.github.abdonia.domino.log.LoggerDominoEventListener} é registrado
 * para imprimir na tela tudo o que acontece durante o jogo.</p>
 * 
 * @author Bruno Abdon
 */
public class DominoApp {

    private static final String CONFIG_XML = "domino-config.xml";
    private static final String DEFAULT_CONFIG_XML = "domino-config-default.xml";
    private static final String MSG_BUNDLE = "com.github.abdonia.domino.app.DominoAppMsg";

    private static final ResourceBundle RESOURCE_BUNDLE = 
        ResourceBundle.getBundle(MSG_BUNDLE);
    
    private DominoApp(){}
   
    /**
     * Roda um {@link Jogo}, de acordo com o arquivo 
     * <code>domino-config.xml</code> ou segundo configurações default caso o 
     * arquivo não exista.
     * 
     * @param args Argumentos são ignorados.
     */
    public static void main(final String[] args) {
        
        try {
            //carregando as configurações
            final DominoConfig dominoConfig = carregaConfiguracao();

            //criando o jogo com essas configurações
            final Jogo jogo = new Jogo(dominoConfig);

            //jogando
            jogo.jogar();

        } catch (DominoAppException e) {
            log(Level.WARNING, "Pipoco: ", e);
        } catch (DominoConfigException e) {
            log(Level.SEVERE, "Erro de configuração: " + e.getMessage(), e);
            
        }
            
    }

    /**
     * Carrega a {@link DominoConfig configuração do jogo} a ser usada, ou seja,
     * quais {@link Jogador jogadores} irão participar, como  
     * {@link DominoEventListener os eventos serão tratados}, etc..
     * 
     * @return Uma {@link DominoConfig configuração de jogo de dominó}.
     * @throws DominoAppException 
     */
    private static DominoConfig carregaConfiguracao() 
            throws DominoAppException {

        final InputStream streamConfiguracao = 
                abreStreamDocumentoDeConfiguracao();

        return DominoXmlConfigLoader
                .carregaConfiguracoes(streamConfiguracao);
    }

    /**
     * Retorna um {@link InputStream} de onde deverá ser lido o documento xml de 
     * configuração do jogo. Tenta primeiro encontrar no diretório corrente o
     * arquivo {@link #CONFIG_XML}. Caso não exista, vai usar um documento de 
     * configuração default que existe no classpath em
     * {@link #DEFAULT_CONFIG_XML}. Caso a aplicação esteja rodando num console,
     * uma mensagem podera ser exibida no caso da configuração default ser 
     * usada.
     * 
     * @return Um {@link InputStream} com o documento xml de configuração.
     * 
     * @throws DominoAppException Caso alguma falha aconteça ao tentar abrir o
     * arquivo.
     */
    private static InputStream abreStreamDocumentoDeConfiguracao() 
            throws DominoAppException {
        
        final InputStream streamConfiguracao;

        try {
            final Path configPath = 
                FileSystems.getDefault().getPath(CONFIG_XML);
            
            if(Files.exists(configPath)){
                streamConfiguracao = Files.newInputStream(configPath,READ);
            } else {
                tentarExibirAvisoConfiguracaoDefault();
                streamConfiguracao = 
                    DominoApp.class.getResourceAsStream(DEFAULT_CONFIG_XML);
            }
            
        } catch (IOException ex) {
            throw new DominoAppException(ex, "Erro ao ler arquivo");
        }
        return streamConfiguracao;
    }

    /**
     * Caso a aplicação esteja rodando num console, exibe uma mensagem avisando
     * que o jogo usará uma configuração default (por não ter encontrado nenhum
     * arquivo de configuração). Após exibir a mensagem, o programa espera o 
     * usuário apertar <code>ENTER</code> para prosseguir.
     * 
     * Se a aplicação não estiver rodando num console, nada acontece.
     * 
     */
    private static void tentarExibirAvisoConfiguracaoDefault() {
        final Console console = System.console();
        if(console != null){
            final String msg =
                    MessageFormat.format(RESOURCE_BUNDLE.getString("msg.defaultconfig"),
                        CONFIG_XML);
            console.writer().println(msg);
            console.readLine();
        }
    }

    private static void log(final Level l, final String msg, final Exception e){
        final Console console = System.console();
        if(console != null){
            console
            .writer()
            .println(MessageFormat
                .format(RESOURCE_BUNDLE.getString("error.config"),
                    e.getMessage()));
        } else {
            //System.err.printf("%s: %s\n",msg, e.getMessage());
            Logger.getLogger(DominoApp.class.getName()).log(l, msg, e);
        }
        
    }
}
