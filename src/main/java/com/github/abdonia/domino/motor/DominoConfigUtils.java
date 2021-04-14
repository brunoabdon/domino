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
package com.github.abdonia.domino.motor;

/**
 * Métodos utilitários usados atualmente pra a implemetação de {@link
 * DominoConfig}.
 *
 * @author Bruno Abdon
 */
class DominoConfigUtils {

    private static final String ERR_SUPER_KLASS =
        "A classe \"%s\" deveria mas nao implementa \"%s\".";

    private static final String ERR_CLASSE_DESCONHECIDA =
        "A classe \"%s\" foi mencionada mas ela nao pode ser encontrada.";

    private DominoConfigUtils() {
        super();
    }

    /**
     * Instancia um objeto dado o nome de sua classe, validando que esta classe
     * seja subclasse de uma dada superclasse.
     *
     * @param <K> O tipo do objeto a ser instanciado.
     * @param superKlass O supertipo que a classe nomeada deve necessariamente
     * extender.
     * @param className O nome da classe que deve ser instanciada.
     * @return Um objeto de classe cujo nome foi passado.
     *
     * @throws DominoConfigException Se a classe não for encotrada, se ela não
     * for uma subclasse da superclasse exigida, ou se houver um erro ao tentar
     * instanciar a classe (deve ter um construtor vazio acessível).
     */
    public static <K> K instancia(
            final Class<K> superKlass,
            final String className) throws DominoConfigException {

        final K instance;

        try {

            instance =
                instancia(
                    Class.forName(className).asSubclass(superKlass)
                )
            ;

        } catch (ClassCastException e) {
            throw new DominoConfigException(
                e,ERR_SUPER_KLASS,className,superKlass.getName());
        } catch (ClassNotFoundException e) {
            throw new DominoConfigException(
                e,ERR_CLASSE_DESCONHECIDA,className);
        }
        return instance;
    }

    /**
     * Instancia um objeto dada  sua classe, usando um construtor vazio
     * acessível.
     *
     * @param <K> O tipo do objeto a ser instanciado.
     * @param klass  A classe que deve ser instanciada.
     *
     * @return Um objeto de classe que foi passada.
     *
     * @throws DominoConfigException Se houver um erro ao tentar instanciar a
     * classe (deve ter um construtor vazio acessível).
     *
     * @throws NullPointerException se a classe passada for {@code null}.
     */
    public static <K> K instancia(final Class<K> klass)
            throws DominoConfigException{

        final K instance;

        try {

            instance = klass.newInstance();

        } catch (InstantiationException e) {
            throw new DominoConfigException(
                e, "Não consegui inicializar: %s.", klass.getName());
        } catch (IllegalAccessException e) {
            throw new DominoConfigException(
                e, "Preciso de um construtor: %s.", klass.getName());
        }
        return instance;
    }
}
