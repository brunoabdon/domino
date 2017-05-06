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
class DominoUtils {
  
    public static <K> K instancia(
            final Class<K> superKlass, 
            final String className) {
        
        final K instance;

        try {

            final Class<? extends K> klass = 
                Class.forName(className).asSubclass(superKlass);
            instance = klass.newInstance();

        } catch (InstantiationException e) {
            throw new IllegalArgumentException(
                "Não consegui inicializar: " + className, e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(
                "Preciso de um construtor: " + className, e);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(
                "Classe  não encontrada: " +  className, e);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(
                "A classe " 
                + className 
                + " não implementa " 
                + superKlass.getName(),e);
        }
        return instance;
    }
    
}
