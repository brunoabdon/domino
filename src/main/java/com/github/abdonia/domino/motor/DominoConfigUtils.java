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
 *
 * @author Bruno Abdon
 */
class DominoConfigUtils {

    private static final String ERR_SUPER_KLASS = 
        "A classe \"%s\" deveria mas nao implementa \"%s\".";

    private static final String ERR_CLASSE_DESCONHECIDA = 
        "A classe \"%s\" foi mencionada mas ela nao pode ser encontrada.";    
    
    public static <K> K instancia(
            final Class<K> superKlass, 
            final String className) throws DominoConfigException {

        final K instance;
        
        try {
            final Class<? extends K> klass =
                Class.forName(className).asSubclass(superKlass);
            
            instance = instancia(superKlass, klass);

        } catch (ClassCastException e) {
            throw erroSuper(e, className, superKlass);
        } catch (ClassNotFoundException e) {
            throw new DominoConfigException(ERR_CLASSE_DESCONHECIDA, className);
        }
        return instance;
    }

    public static <K> K instancia(
            final Class<? super K> superKlass, 
            final Class<K> klass) throws DominoConfigException {
        
        final K instance;

        try {

            instance = klass.newInstance();

        } catch (InstantiationException e) {
            throw new DominoConfigException(
                e, "Não consegui inicializar: %s.", klass.getName());
        } catch (IllegalAccessException e) {
            throw new DominoConfigException(
                e, "Preciso de um construtor: %s.", klass.getName());
        } catch (ClassCastException e) {
            throw erroSuper(e, klass.getName(), superKlass);
        }
        return instance;
    }
    private static DominoConfigException erroSuper(
            final ClassCastException e,
            final String klass,
            final Class<?> superr){
        return 
            new DominoConfigException(e,ERR_SUPER_KLASS,klass,superr.getName());
    }
}
