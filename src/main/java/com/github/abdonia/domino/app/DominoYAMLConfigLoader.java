/*
 * Copyright (C) 2019 Bruno Abdon
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

import java.io.InputStream;

import com.github.abdonia.domino.motor.DominoConfig;


/**
 * Responsável por carregar as {@link DominoConfig configurações do jogo} a 
 * partir de um documento YAML.
 * 
 * @author Bruno Abdon
 */
class DominoYAMLConfigLoader {

    private static final String ERRO_LER_ARQUIVO = 
        "Erro ao tentar ler arquivo de configuração";
    private static final String ERRO_CRIAR_PARSER = 
            "Erro na criação do parse yaml";
    private static final String ERRO_EXECUTAR_CONFIG = 
            "Erro ao executar configuração";

    private DominoYAMLConfigLoader(){}
   
    /**
     * Carrega {@link DominoConfig configurações do jogo} a 
     * partir do {@link InputStream} do documento YAML.
     * @param configInputStream O {@link InputStream} do documento YAML de 
     * configurações. <p>Este método assume que o arquivo está bem formado.
     * Nenhuma nova validação será feita.</p>
     * @return As {@link DominoConfig configurações} do jogo, que estavam no 
     * documento YAML.
     * @throws DominoAppException Caso ocorra algum erro ao tentar ler a stream
     * ou interpretar seu conteúdo.
     */
    public static DominoConfig carregaConfiguracoes(
        final InputStream configInputStream) 
            throws DominoAppException {

        throw new UnsupportedOperationException("Under construction.");

    }
}