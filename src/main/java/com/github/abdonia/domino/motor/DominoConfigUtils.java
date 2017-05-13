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
  
    public static <K> K instancia(
            final Class<K> superKlass, 
            final String className) throws ConfigException {

        final K instance;
        
        try {
            final Class<? extends K> klass =
                Class.forName(className).asSubclass(superKlass);
            
            instance = instancia(superKlass, klass);

        } catch (ClassCastException e) {
            throw new ConfigException(
                e, 
                "A classe \"%s\" foi usada onde se esperava uma classe que implementasse \"%s\".",
                className,
                superKlass.getName());
            
        } catch (ClassNotFoundException e) {
            throw new ConfigException(
                "A classe \"%s\" foi mencionada ela não pode ser encontrada.", className);
        }
        return instance;
    }

    public static <K> K instancia(
            final Class<? super K> superKlass, 
            final Class<K> klass) throws ConfigException {
        
        final K instance;

        try {

            instance = klass.newInstance();

        } catch (InstantiationException e) {
            throw new ConfigException(
                e, "Não consegui inicializar: %s.", klass.getName());
        } catch (IllegalAccessException e) {
            throw new ConfigException(
                e, "Preciso de um construtor: %s.", klass.getName());
        } catch (ClassCastException e) {
            throw new ConfigException(
                e, 
                "A classe \"%s\" foi usada onde se esperava uma classe que implementasse \"%s\".",
                klass.getName(),
                superKlass.getName());
        }
        return instance;
    }
}
