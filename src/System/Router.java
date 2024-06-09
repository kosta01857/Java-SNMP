package System;

import java.util.ArrayList;

public class Router {
	ArrayList<Attribute> attributes = new ArrayList<Attribute>();
	String interfaceIP;
	Router(String ip,ArrayList<Attribute> atrs){
		attributes = atrs;
		interfaceIP = ip;
	}
	public static Router[] createRouterAttributes(Attribute[] atrs) {
		ArrayList<String> ip_list = new ArrayList<String>();
		for(short i = 0;i < atrs.length;i++) {
			String ip = atrs[i].getRouter();
			if(!ip_list.contains(ip))ip_list.add(ip);
		}
		Router[] routers = new Router[ip_list.size()];
		for(short i = 0;i < ip_list.size();i++) {
			ArrayList<Attribute> atr_list = new ArrayList<Attribute>();
			for(short j = 0;j < atrs.length;j++) {
				if(ip_list.get(i).equals(atrs[j].getRouter()))atr_list.add(atrs[j]);
			}
			routers[i] = new Router(ip_list.get(i),atr_list);
		}
		return routers;
	}
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Received from: ").append(interfaceIP+'\n');
		for(short i = 0;i < attributes.size();i++) {
			builder.append(attributes.get(i).toString());
		}
		return builder.toString();
	}
}
