package com.github.abdonia.domino.app;


import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;


/**
 * Responsável por carregar as {@link DominoConfig configurações do jogo} a 
 * partir de um documento XML.
 * 
 * @author Bruno Abdon
 */
class DominoXmlConfigLoader {

    private DominoXmlConfigLoader(){}
   
    /**
     * Carrega {@link DominoConfig configurações do jogo} a 
     * partir do {@link InputStream} do documento XML.
     * @param configInputStream O {@link InputStream} do documento XML de 
     * configurações.
     * @return As {@link DominoConfig configurações} do jogo, que estavam no 
     * documento XML.
     * @throws DominoAppException Caso ocorra algum erro ao tentar ler a stream
     * ou interpretar seu conteúdo.
     */
    public static DominoConfig carregaConfiguracoes(
        final InputStream configInputStream) 
            throws DominoAppException {
        return new ConfigHandler().parseFile(configInputStream);
    }

    /**
     * Não é thread safe!
     */
    private static class ConfigHandler extends DefaultHandler2 {

        private static final String ELEMENT_JOGADOR = "jogador";
        private static final String ELEMENT_EVENT_LISTENER = "event-listener";
        private static final String ATT_NOME = "nome";
        private static final String ATT_CLASSE = "classe";

        private DominoConfig dominoConfig;

        private int idxDupla, idxJogador;
        
        private DominoConfig parseFile(final InputStream is) 
                throws DominoAppException {
            this.dominoConfig = new DominoConfig();
            this.idxDupla = this.idxJogador = 1;
            
            try {
                SAXParserFactory
                    .newInstance()
                    .newSAXParser()
                    .parse(is, this);

            } catch (ParserConfigurationException | SAXException e) {
                throw new DominoAppException(e, "Erro na criação do parse xml");
            } catch (IOException e) {
                throw new DominoAppException(
                    e, "Erro ao tentar ler arquivo de configuração");
            }
            
            return this.dominoConfig;
        }

        @Override
        public void startElement(
                final String uri, 
                final String localName, 
                final String qName, 
                final Attributes attributes) throws SAXException {

            if(qName.equals(ELEMENT_JOGADOR)){
                final String nome = attributes.getValue(ATT_NOME);
                final String classe = attributes.getValue(ATT_CLASSE);
                
                dominoConfig.setNomeEClasse(
                    nome, 
                    classe, 
                    idxDupla, 
                    idxJogador);
                
                if(++idxJogador == 3) {
                    idxJogador = 1;
                    idxDupla = 2;
                }
            } else if(qName.equals(ELEMENT_EVENT_LISTENER)){
                final String classe = attributes.getValue(ATT_CLASSE);
                this.dominoConfig.addEventListener(classe);
            }
        }
    }
}