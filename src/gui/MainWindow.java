package gui;

import java.awt.BorderLayout;

import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Dialog;
import org.snmp4j.smi.IpAddress;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;

import System.AgentReader;
import System.MyFileReader;
import System.ReaderThread;
public class MainWindow extends Frame {
	static private MainWindow instance = null;
	static public MainWindow getInstance() {
		if(instance == null) {
			instance = new MainWindow();
		}
		return instance;
	}
	private static final long serialVersionUID = 1L;
	TextArea text_area;
	MenuBar menu_bar = new MenuBar();
	MyFileReader filereader;
	String[] OIDS;
	ReaderThread reader;
	Panel routerChoicePanel = new Panel();
	Button confirmRouter = new Button("OK");
	TextField routerIP = new TextField("ROUTER IP");
	AgentReader agent;
	public TextArea getTextArea() {
		return text_area;
	}
	public void initWindow() {
		routerIP.setBackground(Color.white);
		routerChoicePanel.add(routerIP,BorderLayout.LINE_END);
		confirmRouter.setBackground(Color.white);
		routerChoicePanel.add(confirmRouter,BorderLayout.LINE_END);
		confirmRouter.addActionListener((ae)->{
			initRead();
		});
		add(routerChoicePanel,BorderLayout.PAGE_START);
		text_area = new TextArea();
		text_area.setBackground(Color.black);
		text_area.setForeground(Color.white);
		add(text_area,BorderLayout.CENTER);
		Menu menu = new Menu("READ");
		MenuItem routing_table = new MenuItem("routing table");
		routing_table.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				setReadMode(MyFileReader.Mode.ROUTING_TABLE);
				getOIDS();
			}
		});
		menu.add(routing_table);
		MenuItem bgp_peers = new MenuItem("bgp peers");
		bgp_peers.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				setReadMode(MyFileReader.Mode.BGP_PEERS);
				getOIDS();
			}
		});
		menu.add(bgp_peers);
		MenuItem interfaces = new MenuItem("interfaces");
		interfaces.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				setReadMode(MyFileReader.Mode.INTERFACES);
				getOIDS();
			}
		});
		menu.add(interfaces);
		MenuItem attributes = new MenuItem("bgp attributes");
		attributes.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				setReadMode(MyFileReader.Mode.BGP_ATTRIBUTES);
				getOIDS();
			}
		});
		menu.add(attributes);
		menu_bar.add(menu);
		this.setMenuBar(menu_bar);
	}
	public void setAdr(String adr) {
		agent.setAdr(adr);
	}
	public void initRead() {
		if (OIDS == null) return;
		String txt = routerIP.getText();
		IpAddress ADR = new IpAddress();
		if(!ADR.parseAddress(txt)) {
			invalidInputMessage();
			return;
		}
		setAdr(txt);
		if(!reader.is_running()) reader.start();
	}
	public void invalidInputMessage() {
		Dialog d = new Dialog(this,"INVALID IP ADDRESS");
		d.setBounds(500, 500, 200, 100);
		d.setVisible(true);
		Button b = new Button("OK");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				d.dispose();
			}
		});
		d.add(b,BorderLayout.CENTER);
	}
	public void setReadMode(MyFileReader.Mode mode) {
		filereader.setMode(mode);
	}
	public void getOIDS() {
		OIDS = filereader.readFile();
		if(agent != null)agent.setOIDS(OIDS);
	}
	private MainWindow() {
		setBounds(700,200,1000,1000);
		setResizable(true);
		setBackground(Color.black);
		initWindow();
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if(reader != null) reader.stop();
				dispose();
			}
		});
		filereader = new MyFileReader(MyFileReader.Mode.ROUTING_TABLE);
		getOIDS();
		agent = new AgentReader(null,"si2019",OIDS);
		reader = new ReaderThread(agent,this);
	}
	public static void main(String[] args) {
		getInstance();
	}
}
