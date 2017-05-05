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
        
        final ConfigHandler configHandler = new ConfigHandler();

        try {
            SAXParserFactory
                .newInstance()
                .newSAXParser()
                .parse(configInputStream, 
                        configHandler);

        } catch (ParserConfigurationException | SAXException e) {
            throw new DominoAppException(e, "Erro na criação do parse xml");
        } catch (IOException e) {
            throw new DominoAppException(
                e, "Erro ao tentar ler arquivo de configuração");
        }
        
        return configHandler.dominoConfig;

    }

    /**
     * Não é thread safe!
     */
    private static class ConfigHandler extends DefaultHandler2 {

        private static final String ELEMENT_JOGADOR = "jogador";
        private static final String ELEMENT_EVENT_LISTENER = "event-listener";
        private static final String ATT_NOME = "nome";
        private static final String ATT_CLASSE = "classe";

        private final DominoConfig dominoConfig;
        private int idxJogador;
        
        public ConfigHandler(){
            this.dominoConfig = new DominoConfig();
            this.idxJogador = 0;
        }

        @Override
        public void startElement(
                final String uri, 
                final String localName, 
                final String qName, 
                final Attributes attributes) throws SAXException {

            final String classe = attributes.getValue(ATT_CLASSE);

            if(qName.equals(ELEMENT_JOGADOR)){
                final String nome = attributes.getValue(ATT_NOME);
                
                dominoConfig.setNomeEClasse(
                    nome, 
                    classe, 
                    (idxJogador>>1)+1, 
                    (idxJogador++&1)+1);

            } else if(qName.equals(ELEMENT_EVENT_LISTENER)){
                this.dominoConfig.addEventListener(classe);
            }
        }
    }
}