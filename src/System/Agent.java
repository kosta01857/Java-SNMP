package System;

import java.io.IOException;
import java.net.SocketException;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;

/* INICIJALIZUJE:
 *  KONEKCIJU IZMEDJU APLIKACIJE I MIB BAZE DATOG RUTERA
 *  SNMP PROTOKOL SA UDP
 */
abstract public class Agent{
	protected PDU pdu;
	protected Snmp snmp;
	protected DefaultUdpTransportMapping transport;
	protected CommunityTarget<UdpAddress> target;
	public Agent(String adr,String community) {
		target = new CommunityTarget<UdpAddress>();
		initAdr(adr,community);
		initSNMP();
	}
	public String getAddress() {
		UdpAddress adr = target.getAddress();
		return adr.toString();
	}
	public UdpAddress getUdpAddress() {
		return target.getAddress();
	}
	protected void initAdr(String adr, String community) {
		if(adr != null)setAdr(adr);
		setCommunity(community);
		target.setVersion(SnmpConstants.version2c);
	}
	public synchronized void setAdr(String adr) {
		UdpAddress udpadr = new UdpAddress(adr+"/"+161);
		target.setAddress(udpadr);
	}
	public void setCommunity(String community) {
		target.setCommunity(new OctetString(community));
	}
	protected void initSNMP() {
		try {
			transport = new DefaultUdpTransportMapping();
			transport.listen();
			this.snmp = new Snmp(transport);
		} catch (SocketException e) {} catch (IOException e) {}
	}
	abstract protected void work();

}
