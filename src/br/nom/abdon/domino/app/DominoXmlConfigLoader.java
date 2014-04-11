package br.nom.abdon.domino.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import br.nom.abdon.domino.Jogador;
import br.nom.abdon.domino.motor.JogadorWrapper;

public class DominoXmlConfigLoader {

	private static final String CONFIG_XML = "domino-config.xml";

	private ConfigHandler configHandler;

	public void carregaConfiguracoes() throws DominoAppException {

		configHandler = new ConfigHandler();
		configHandler.parseFile();
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

	private Jogador carrega(Class<? extends Jogador> classeJogador) throws DominoAppException {
		Jogador jogador;
		try {
			jogador = classeJogador.newInstance();
		} catch (InstantiationException e) {
			throw new DominoAppException("Jogador nem consegui ser inicializado: " + classeJogador);
		} catch (IllegalAccessException e) {
			throw new DominoAppException("Jogador escodeu até o construtor: " + classeJogador);
		}
		return jogador;
	}


	private JogadorWrapper getJogador(String nomeJogador) throws DominoAppException {
		
		Class<? extends Jogador> klass  = pegaClasseJogador(nomeJogador);
		Jogador jogador = carrega(klass);
		
		return new JogadorWrapper(jogador, nomeJogador);
	}

	
	private Class<? extends Jogador> pegaClasseJogador(String nomeJogador) throws DominoAppException {
		
		String jogadorClassName = configHandler.pegaClasseJogador(nomeJogador);
                
		Class<? extends Jogador> klass;
		try {
			klass = Class.forName(jogadorClassName).asSubclass(Jogador.class);
		} catch (ClassNotFoundException e) {
			throw new DominoAppException("Classe do jogador não encontrada. Tem que estar no classpath:" +  jogadorClassName);
		} catch (ClassCastException e) {
			throw new DominoAppException("A classe " + jogadorClassName + " nao implementa " + Jogador.class.getName());
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
		
		Map<String, String> jogadores = new HashMap<String,String>();
	
		private void parseFile() throws DominoAppException {
			try {
				File configFile = new File(CONFIG_XML);
				if(!configFile.exists()){
					System.err.println("Arquivo de configuracao nao econtrado:" + configFile.getAbsolutePath());
				}
				InputStream xmlConfigFileStream = new FileInputStream(configFile);
				SAXParserFactory.newInstance().newSAXParser().parse(xmlConfigFileStream, this);
				
			} catch (ParserConfigurationException e) {
				throw new DominoAppException(e, "Erro na criação do parse xml");
			} catch (SAXException e) {
				throw new DominoAppException(e, "Erro na criação do parse xml");
			} catch (IOException e) {
				throw new DominoAppException (e,  "Erro ao tentar ler arquivo de configuracao");
			}
		}

	
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
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
		
		public String pegaClasseJogador(String nomeJogador) throws DominoAppException {
			String jogadorClassName = jogadores.get(nomeJogador);
			if(jogadorClassName == null){
				throw new DominoAppException("Jogador desconhecido:" +  nomeJogador);
			}
			return jogadorClassName;
		}

	}
	

	
}
