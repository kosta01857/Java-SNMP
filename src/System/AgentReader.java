package System;
import java.util.ArrayList;
import org.snmp4j.SNMP4JSettings;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.DictionaryOIDTextFormat;
import org.snmp4j.util.TreeEvent;
import org.snmp4j.util.TreeUtils;

import gui.MainWindow;
public class AgentReader extends Agent {
	String[] oids;
	java.util.List<TreeEvent> list;
	DictionaryOIDTextFormat txtformat;
	TreeUtils tree;
	public AgentReader(String adr,String comm,String[]oid) {
		super(adr,comm);
		oids = oid;
		txtformat = new DictionaryOIDTextFormat();
		if(oid != null)StringParser.registerName(txtformat, oid);
		SNMP4JSettings.setOIDTextFormat(txtformat);
		tree = new TreeUtils(this.snmp,new DefaultPDUFactory());
		
	}
	public synchronized void setOIDS(String[] oids) {
		this.oids = oids;
		StringParser.registerName(txtformat, this.oids);
	}
	protected OID[] getOIDS() {
		short n = (short)oids.length;
		OID[] oids = new OID[n];
		for(int i = 0;i < n;i++) {
			oids[i] = new OID(StringParser.removeName(this.oids[i]));
		}
		return oids;
	}
	public synchronized Router[] getData() {
		work();
		ArrayList<VariableBinding> varlist = new ArrayList<VariableBinding>();
		for(int i = 0;i < list.size();i++) {
			TreeEvent event = list.get(i);
			VariableBinding[] vars= event.getVariableBindings();
			if(vars != null) {
				for(int j = 0;j < vars.length;j++) {
					varlist.add(vars[j]);
				}	
			}
		}
		short n = (short)varlist.size();
		Attribute[] attributes = new Attribute[n];
		for(int i = 0;i < n;i++) {
			VariableBinding b = varlist.get(i);
			OID o = b.getOid();
			attributes[i] = new Attribute(o.format(),b.getVariable().toString());
			
		} return Router.createRouterAttributes(attributes);
	}
	@Override
	protected synchronized void work() {
		OID[] oid = getOIDS();
		if(oid.length == 1) {
			list = tree.getSubtree(this.target, oid[0]);
		}
		list = tree.walk(this.target,oid);
	}
	public static void track_interfaces(MainWindow f) {
		MyFileReader oidgetter = new MyFileReader(MyFileReader.Mode.IP_ADDRESS);
		String[] ips = oidgetter.readFile();
		oidgetter.setMode(MyFileReader.Mode.INTERFACES);
		String[] oids = oidgetter.readFile();
		short n = (short)ips.length;
		ReaderThread workers[] = new ReaderThread[n];
		for(int i = 0;i < n;i++) {
			workers[i] = new ReaderThread(new AgentReader(ips[i],"si2019",oids),f);
			workers[i].start();
		}
	}
	public static void main(String[] args) {
		track_interfaces(null);
	}

}
