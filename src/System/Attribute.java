package System;
public class Attribute {
	String val;
	String interfaceIP;
	String name;
	public Attribute(String str,String v) {
		interfaceIP = StringParser.removeName(str);
		name = StringParser.removeOID(str);
		val = v;
	}
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(name + ": ").append(val).append('\n');
		return builder.toString();
	}
	public String getName() {
		return name;
	}
	public String getVal() {
		return val;
	}
	public String getRouter() {
		return interfaceIP;
	}
}
