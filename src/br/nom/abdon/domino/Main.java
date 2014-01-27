package br.nom.abdon.domino;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class Main {
	public static void main(String[] args) {
		System.out.println(Numero.DUQUE.compareTo(Numero.DUQUE) >=0);
		
		Deque<String> d = new ArrayDeque<>();
		
		d.add("1");
		d.add("2");
		
		Iterator<String> iterator = d.iterator();
		System.out.println(iterator.next());
		
		d.add("3");
		d.add("3");
		d.add("5");
		
		System.out.println(iterator.next());
		System.out.println(iterator.next());
		System.out.println(iterator.next());
		System.out.println(iterator.next());
		
		
//		for (Pedra pedra : Pedra.values()) {
//		System.out.println(pedra);
//	}
//	
//	System.out.println("\uD83C\uDC32");
char pecas[] = new char[] {'\uDC31',
		'\uDC32',
		'\uDC33',
		'\uDC34',
		'\uDC35',
		'\uDC36',
		'\uDC37',
		'\uDC38',
		'\uDC39',
		'\uDC3A',
		'\uDC3B',
		'\uDC3C',
		'\uDC3D',
		'\uDC3E',
		'\uDC3F',
		'\uDC40',
		'\uDC41',
		'\uDC42',
		'\uDC43',
		'\uDC44',
		'\uDC45',
		'\uDC46',
		'\uDC47',
		'\uDC48',
		'\uDC49',
		'\uDC4A',
		'\uDC4B',
		'\uDC4C',
		'\uDC4D',
		'\uDC4E',
		'\uDC4F',
		'\uDC50',
		'\uDC51',
		'\uDC52',
		'\uDC53',
		'\uDC54',
		'\uDC55',
		'\uDC56',
		'\uDC57',
		'\uDC58',
		'\uDC59',
		'\uDC5A',
		'\uDC5B',
		'\uDC5C',
		'\uDC5D',
		'\uDC5E',
		'\uDC5F',
		'\uDC60',
		'\uDC61'};

for (char peca : pecas) {
	System.out.println("\uD83C" + peca);
}


	}
}
