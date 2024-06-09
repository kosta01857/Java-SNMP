package System;

import org.snmp4j.util.DictionaryOIDTextFormat;

public class StringParser {
	static public String removeName(String s) {
		short n = 0;
		for(short i = 0;i< s.length();i++) {
			char c = s.charAt(i);
			if(!Character.isDigit(c)) n++;
			else break;
		}
		return s.substring(n);
	}
	static public String removeOID(String s) {
		short n = 0;
		for(short i = 0;i< s.length();i++) {
			char c = s.charAt(i);
			if(!Character.isDigit(c)) n++;
			else break;
		}
		return s.substring(0,n-1);
	}
	static public void registerName(DictionaryOIDTextFormat format,String[] oids) {
		for(short i = 0;i < oids.length;i++) format.put(oids[i]);
	}
}
