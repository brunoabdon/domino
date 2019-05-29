package com.github.abdonia.domino.app;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import com.github.abdonia.domino.motor.DominoConfig;

/**
 * Não é thread safe!
 */
class ConfigHandler extends DefaultHandler2 {

    private static final String ELEMENT_JOGADOR = "jogador";
    private static final String ELEMENT_EVENT_LISTENER = "event-listener";
    
    private static final String ATT_NOME = "nome";
    private static final String ATT_CLASSE = "classe";

    final DominoConfig dominoConfig;
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
            
            dominoConfig.setJogador(
                nome, 
                classe, 
                idxJogador>>1, 
                idxJogador++&1);

        } else if(qName.equals(ELEMENT_EVENT_LISTENER)){
            this.dominoConfig.addEventListener(classe);
        }
    }
}