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

public class DominoXmlConfigLoader {

	private static final String CONFIG_XML = "domino-config.xml";

	private Object classeJogadores[];
	private String nomesJogadores[];
	

	public void carregaConfiguracoes() throws DominoAppException {

		ConfigHandler configHandler = new ConfigHandler();
		configHandler.parseFile();
		
		classeJogadores = configHandler.loadJogadores();
		nomesJogadores = configHandler.nomesJogagores();
	}

	@SuppressWarnings("unchecked")
	public Class<? extends Jogador> getClasseJogador1Dupla1() {
		return (Class<? extends Jogador>) classeJogadores[0];
	}

	@SuppressWarnings("unchecked")
	public Class<? extends Jogador> getClasseJogador1Dupla2() {
		return (Class<? extends Jogador>) classeJogadores[1];
	}

	@SuppressWarnings("unchecked")
	public Class<? extends Jogador> getClasseJogador2Dupla1() {
		return (Class<? extends Jogador>) classeJogadores[2];
	}

	@SuppressWarnings("unchecked")
	public Class<? extends Jogador> getClasseJogador2Dupla2() {
		return (Class<? extends Jogador>) classeJogadores[3];
	}
	
	public String getNomeJogador1Dupla1() {
		return nomesJogadores[0];
	}

	public String getNomeJogador1Dupla2() {
		return nomesJogadores[1];
	}

	public String getNomeJogador2Dupla1() {
		return nomesJogadores[2];
	}

	public String getNomeJogador2Dupla2() {
		return nomesJogadores[3];
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
		
		private Map<String, String> jogadores = new HashMap<String,String>();
	
		private Object[] loadJogadores() throws DominoAppException {
			return new Object[] {
					loadJogador(jogador1_dupla1),	
					loadJogador(jogador1_dupla2),
					loadJogador(jogador2_dupla1),	
					loadJogador(jogador2_dupla2),
			};
		}

		public String[] nomesJogagores() {
			return new String[]{
					jogador1_dupla1,	
					jogador1_dupla2,
					jogador2_dupla1,	
					jogador2_dupla2,
			};
		}

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

	
		private Class<? extends Jogador> loadJogador(String nomeJogador) throws DominoAppException {
			
			String jogadorClassName = jogadores.get(nomeJogador);
			if(jogadorClassName == null){
				throw new DominoAppException("Jogador desconhecido:" +  nomeJogador);
			}
			
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
	}
	

	
}
