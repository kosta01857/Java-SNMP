package System;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.BufferedReader;
public class MyFileReader {
	static public enum Mode{
		ROUTING_TABLE,BGP_PEERS,INTERFACES,BGP_ATTRIBUTES,IP_ADDRESS
	}
	Mode mode;
	public MyFileReader(Mode m) {
		mode = m;
	}
	public void setMode(Mode m) {
		mode = m;
	}
	public String[] readFile() {
		 
		ArrayList<String> OIDS = new ArrayList<String>();
		try {
		String jarFilePath = MyFileReader.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		String jarDir = new File(jarFilePath).getParent();
		jarDir+="/configs";
		String filePath = jarDir + File.separator + "table";
		switch(mode) {
			case ROUTING_TABLE:{
				filePath = jarDir + File.separator + "table";
				break;
			}
			case BGP_PEERS:{
				filePath = jarDir + File.separator + "peers";
				break;
			}
			case BGP_ATTRIBUTES:{
				filePath = jarDir + File.separator + "bgp_attributes";
				break;
			}
			case INTERFACES:{
				filePath = jarDir + File.separator + "interfaces";
				break;
			}
			case IP_ADDRESS:{
				filePath = jarDir + File.separator + "ip_adr";
				break;
			}
		}
		File oids = new File(filePath);
		
			BufferedReader buf = new BufferedReader(new FileReader(oids));
			String line = buf.readLine();
			while(line != null) {
				if(line.charAt(0) != '/') OIDS.add(line);
				line = buf.readLine();
			}
			buf.close();
		} catch (Exception e) {System.out.println("NO FILE");return null;}
		Object[] objects = OIDS.toArray();
		String[] strings = new String[objects.length];
		for (short i = 0;i < objects.length;i++) {
			strings[i] = objects[i].toString();
		}
		return strings;
	}
}
 