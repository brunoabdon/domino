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

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.introspector.BeanAccess;

import com.github.abdonia.domino.motor.DominoConfig;
import com.github.abdonia.domino.motor.DominoConfigException;


/**
 * Responsável por carregar as {@link DominoConfig configurações do jogo} a
 * partir de um documento YAML.
 *
 * @author Bruno Abdon
 */
class DominoYAMLConfigLoader {

    private static final String ERRO_CRIAR_PARSER =
            "Erro no parse do yaml";
    private static final String ERRO_EXECUTAR_CONFIG =
            "Erro ao executar configuração";

    private static final Yaml YAML;

    static {
        YAML = new Yaml();
        YAML.setBeanAccess(BeanAccess.FIELD);
    }

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

        final DominoConfig dominoConfig;
        try {
            final DominoYAMLConfig yamlCfg =
                YAML.loadAs(
                    configInputStream,
                    DominoYAMLConfig.class
                );

            dominoConfig = loadBuilder(yamlCfg).build();

        } catch (YAMLException e) {
            throw new DominoAppException(e, ERRO_CRIAR_PARSER);
        } catch (final DominoConfigException e) {
            throw new DominoAppException(e, ERRO_EXECUTAR_CONFIG);
        }

        return dominoConfig;
    }

    private static DominoConfig.Builder loadBuilder(
            final DominoYAMLConfig yamlCfg) {
        final JogadorConfig[] dupla0 = yamlCfg.getDupla0();
        final JogadorConfig d0jogador0 = dupla0[0];
        final JogadorConfig d0jogador1 = dupla0[1];

        final JogadorConfig[] dupla1 = yamlCfg.getDupla1();
        final JogadorConfig d1jogador0 = dupla1[0];
        final JogadorConfig d1jogador1 = dupla1[1];

        final DominoConfig.Builder builder =
            new DominoConfig.Builder()
            .withNomeJogador0Dupla0(d0jogador0.getNome())
            .withJogador0Dupla0(d0jogador0.getClasse())

            .withNomeJogador0Dupla1(d1jogador0.getNome())
            .withJogador0Dupla1(d1jogador0.getClasse())

            .withNomeJogador1Dupla0(d0jogador1.getNome())
            .withJogador1Dupla0(d0jogador1.getClasse())

            .withNomeJogador1Dupla1(d1jogador1.getNome())
            .withJogador1Dupla1(d1jogador1.getClasse());

        for (final String listener : yamlCfg.getListeners()) {
            builder.withEventListener(listener);
        }
        return builder;
    }
}