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

import com.github.abdonia.domino.motor.DominoConfig;
import com.github.abdonia.domino.motor.DominoConfigException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;


/**
 * Responsável por carregar as {@link DominoConfig configurações do jogo} a 
 * partir de um documento XML.
 * 
 * @author Bruno Abdon
 */
class DominoXmlConfigLoader {

    private static final String ERRO_LER_ARQUIVO = 
        "Erro ao tentar ler arquivo de configuração";
    private static final String ERRO_CRIAR_PARSER = 
            "Erro na criação do parse xml";
    private static final String ERRO_EXECUTAR_CONFIG = 
            "Erro ao executar configuração";

    private DominoXmlConfigLoader(){}
   
    /**
     * Carrega {@link DominoConfig configurações do jogo} a 
     * partir do {@link InputStream} do documento XML.
     * @param configInputStream O {@link InputStream} do documento XML de 
     * configurações. <p>Este método assume que o arquivo está bem formado.
     * Nenhuma nova validação será feita.</p>
     * @return As {@link DominoConfig configurações} do jogo, que estavam no 
     * documento XML.
     * @throws DominoAppException Caso ocorra algum erro ao tentar ler a stream
     * ou interpretar seu conteúdo.
     */
    public static DominoConfig carregaConfiguracoes(
        final InputStream configInputStream) 
            throws DominoAppException {
        
        final DominoConfig dominoConfig;
        final ConfigHandler configHandler = new ConfigHandler();

        try {
            SAXParserFactory
                .newInstance()
                .newSAXParser()
                .parse(
                    configInputStream,
                    configHandler);

            dominoConfig = configHandler.builder.build();

        } catch (ParserConfigurationException | SAXException e) {
            throw new DominoAppException(e, ERRO_CRIAR_PARSER);
        } catch (IOException e) {
            throw new DominoAppException(e, ERRO_LER_ARQUIVO);
        } catch (final DominoConfigException e) {
            throw new DominoAppException(e, ERRO_EXECUTAR_CONFIG);
        }
        
        
        return dominoConfig;

    }
}