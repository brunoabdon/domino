package com.github.abdonia.domino.app;

import java.util.HashMap;
import java.util.Map;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import com.github.abdonia.domino.Jogador;
import com.github.abdonia.domino.motor.JogadorWrapper;

public class DominoXmlConfigLoader {

    private ConfigHandler configHandler;

    public void carregaConfiguracoes(final InputStream configInputStream) 
            throws DominoAppException {
        configHandler = new ConfigHandler();
        configHandler.parseFile(configInputStream);
    }

    public JogadorWrapper getJogador1Dupla1() throws DominoAppException {
        return getJogador(configHandler.jogador1_dupla1);
    }

    public JogadorWrapper getJogador2Dupla1() throws DominoAppException {
        return getJogador(configHandler.jogador2_dupla1);
    }

    public JogadorWrapper getJogador1Dupla2() throws DominoAppException {
        return getJogador(configHandler.jogador1_dupla2);
    }
    
    public JogadorWrapper getJogador2Dupla2() throws DominoAppException {
        return getJogador(configHandler.jogador2_dupla2);
    }

    private Jogador carrega(final Class<? extends Jogador> classeJogador) 
            throws DominoAppException {
        
        final Jogador jogador;
        try {
                jogador = classeJogador.newInstance();
        } catch (InstantiationException e) {
            throw new DominoAppException(
                e,"Jogador nem conseguiu ser inicializado: " + classeJogador);
        } catch (IllegalAccessException e) {
            throw new DominoAppException(
                e,"Jogador escodeu até o construtor: " + classeJogador);
        }
        return jogador;
    }

    private JogadorWrapper getJogador(final String nomeJogador) 
            throws DominoAppException {

        final Class<? extends Jogador> klass  = pegaClasseJogador(nomeJogador);
        final Jogador jogador = carrega(klass);

        return new JogadorWrapper(jogador, nomeJogador);
    }

    private Class<? extends Jogador> pegaClasseJogador(
            final String nomeJogador) throws DominoAppException {

        final String jogadorClassName = 
            configHandler.pegaClasseJogador(nomeJogador);

        final Class<? extends Jogador> klass;
        try {
            klass = Class.forName(jogadorClassName).asSubclass(Jogador.class);
        } catch (ClassNotFoundException e) {
            throw new DominoAppException(
                e, "Classe do jogador não encontrada: " +  jogadorClassName);
        } catch (ClassCastException e) {
            throw new DominoAppException(
                e,
                "A classe " 
                + jogadorClassName 
                + " não implementa " 
                + Jogador.class.getName());
        }
        return klass;
    }

    private static class ConfigHandler extends DefaultHandler2 {

        private static final String ELEMENT_DUPLA1 = "dupla1";
        private static final String ELEMENT_DUPLA2 = "dupla2";
        private static final String ELEMENT_JOGADOR = "jogador";
        private static final String ATT_JOGADOR1 = "jogador1";
        private static final String ATT_JOGADOR2 = "jogador2";
        private static final String ATT_NOME = "nome";
        private static final String ATT_CLASSE = "classe";

        String jogador1_dupla1;
        String jogador2_dupla1;

        String jogador2_dupla2;
        String jogador1_dupla2;

        Map<String, String> jogadores = new HashMap<>();

        private void parseFile(final InputStream is) throws DominoAppException {
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
        }

        @Override
        public void startElement(
                final String uri, 
                final String localName, 
                final String qName, 
                final Attributes attributes) throws SAXException {

            if(qName.equals(ELEMENT_DUPLA1)){
                jogador1_dupla1 = attributes.getValue(ATT_JOGADOR1);
                jogador2_dupla1 = attributes.getValue(ATT_JOGADOR2);
            } else if(qName.equals(ELEMENT_DUPLA2)){
                jogador1_dupla2 = attributes.getValue(ATT_JOGADOR1);
                jogador2_dupla2 = attributes.getValue(ATT_JOGADOR2);
            } else if(qName.equals(ELEMENT_JOGADOR)){
                String nome = attributes.getValue(ATT_NOME);
                String classe = attributes.getValue(ATT_CLASSE);
                jogadores.put(nome, classe);
            }
        }

        public String pegaClasseJogador(final String nomeJogador) 
                throws DominoAppException {
            
            final String jogadorClassName = jogadores.get(nomeJogador);
            if(jogadorClassName == null){
                throw new DominoAppException(
                    "Jogador desconhecido: " 
                    +  nomeJogador);
            }
            return jogadorClassName;
        }
    }
}