package System;

import java.io.IOException;
import java.net.SocketException;

import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;


import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class TrapAgent implements Runnable{
	Thread thr;
	Snmp snmp = new Snmp();
	DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping(new UdpAddress("192.168.122.1/"+2000));
	public TrapAgent() throws IOException {
		try {
			this.snmp = new Snmp(transport);
			transport.listen();
		} catch (SocketException e) {} catch (IOException e) {System.out.println("FAIL");}
		thr = new Thread(this);
		CommandResponder pduHandler = new CommandResponder() {
			public synchronized void processPdu(CommandResponderEvent e) {
				PDU pdu = e.getPDU();
				if (pdu != null) {	
				System.out.println(pdu);
	
				}
			}
		};
		snmp.addCommandResponder(pduHandler);
		
	};
	
	public void start() {
		thr.start();
	}
	@Override
	public void run() {
		while(true);
	}
	public static void main(String[] args) {
		TrapAgent agent;
		try {
			agent = new TrapAgent();
			agent.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
