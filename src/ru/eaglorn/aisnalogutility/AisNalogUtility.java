package ru.eaglorn.aisnalogutility;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.WinBase.PROCESS_INFORMATION;
import com.sun.jna.platform.win32.WinBase.STARTUPINFO;

public class AisNalogUtility {

	public static String APP_PROM_PATH = "";
	public static String APP_PROM_VERSION = "";
	
	public static String APP_OE_PATH = "";
	public static String APP_OE_VERSION = "";

	public static JFrame APP = null;
	
	public static int WIDTH = 750;
	public static int HEIGTH = 600;

	public static final String APP_VERSION = "7";
	
	public static boolean APP_PROM_INSTALLED = false;
	public static boolean APP_OE_INSTALLED = false;
	
	public static JMenuBar MENU_BAR = null;

	public static JButton BUTTON_INSTALL_UNINSTALLED_PROM_FIX = null;
	public static JButton BUTTON_INSTALL_ALL_PROM_FIX = null;
	public static JButton BUTTON_INSTALL_CHECKED_PROM_FIX = null;
	public static JButton BUTTON_INSTALL_UNCHECKED_PROM_FIX = null;
	
	public static JButton BUTTON_INSTALL_APP_PROM = null;

	public static LoadingThread LOAD_THREAD = null;

	public static Logger LOGGER = null;

	static JSplitPane SPLIT_PANE_PROM = null;
	static JSplitPane SPLIT_PANE_PROM_INSTALL = null;
	
	public static JButton buttonInstallAisProm() {
		BUTTON_INSTALL_APP_PROM = new JButton("Установить АИС-Налог 3 ПРОМ");

		BUTTON_INSTALL_APP_PROM.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!((JButton) e.getSource()).isEnabled())
					return;

				LoadingThread.IS_RUN = true;
				LOAD_THREAD = new LoadingThread();
				LoadingThread.TYPE_INSTALL = 1;
				LOAD_THREAD.start();
				Thread app_prom_thread = new PromAppThread();
				app_prom_thread.start();
			}
		});
		return BUTTON_INSTALL_APP_PROM;
	}
	
	public static JButton buttonUninstalledPromFix() {
		BUTTON_INSTALL_UNINSTALLED_PROM_FIX = new JButton("Установить новые фиксы");

		BUTTON_INSTALL_UNINSTALLED_PROM_FIX.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!((JButton) e.getSource()).isEnabled())
					return;

				LoadingThread.IS_RUN = true;
				LOAD_THREAD = new LoadingThread();
				LOAD_THREAD.start();
				Thread prom_fix_thread = new PromFixThread();
				prom_fix_thread.start();
			}
		});
		return BUTTON_INSTALL_UNINSTALLED_PROM_FIX;
	}

	public static JButton buttonAllPromFix() {
		BUTTON_INSTALL_ALL_PROM_FIX = new JButton("Установить все фиксы");

		BUTTON_INSTALL_ALL_PROM_FIX.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!((JButton) e.getSource()).isEnabled())
					return;

				LoadingThread.IS_RUN = true;
				LOAD_THREAD = new LoadingThread();
				LOAD_THREAD.start();
				PromFixThread.installMode = 1;
				Thread prom_fix_thread = new PromFixThread();
				prom_fix_thread.start();
			}
		});
		return BUTTON_INSTALL_ALL_PROM_FIX;
	}

	public static JButton buttonCheckedPromFix() {
		BUTTON_INSTALL_CHECKED_PROM_FIX = new JButton("Установить только выбранные фиксы");

		BUTTON_INSTALL_CHECKED_PROM_FIX.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!((JButton) e.getSource()).isEnabled())
					return;

				LoadingThread.IS_RUN = true;
				LOAD_THREAD = new LoadingThread();
				LOAD_THREAD.start();
				PromFixThread.installMode = 2;
				Thread prom_fix_thread = new PromFixThread();
				prom_fix_thread.start();
			}
		});
		return BUTTON_INSTALL_CHECKED_PROM_FIX;
	}
	
	public static JButton buttonUncheckedPromFix() {
		BUTTON_INSTALL_UNCHECKED_PROM_FIX = new JButton("Установить все фиксы кроме выбранных");

		BUTTON_INSTALL_UNCHECKED_PROM_FIX.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!((JButton) e.getSource()).isEnabled())
					return;

				LoadingThread.IS_RUN = true;
				LOAD_THREAD = new LoadingThread();
				LOAD_THREAD.start();
				PromFixThread.installMode = 3;
				Thread prom_fix_thread = new PromFixThread();
				prom_fix_thread.start();
			}
		});
		return BUTTON_INSTALL_UNCHECKED_PROM_FIX;
	}

	private static boolean checkPrivileges() {
		File testPriv = new File("c:\\Windows\\");
		if (!testPriv.canWrite())
			return false;
		File fileTest = null;
		try {
			fileTest = File.createTempFile("test", ".dll", testPriv);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, e.getMessage());
			return false;
		} finally {
			if (fileTest != null)
				fileTest.delete();
		}
		return true;
	}

	public static JList<String> getPromFixList() {
		DefaultListModel<String> prom_fix_list_init = new DefaultListModel<>();

		FixListSelectionDocument listSelectionDocument = new FixListSelectionDocument();
		File dir = new File(Data.CONFIG_APP.NET_PATH + "\\promfix");
		File[] arrFiles = dir.listFiles();
		
		Comparator<File> comp = new Comparator<File>() {
			@Override
			public int compare(File f1, File f2) {
				String name1 = f1.getName();
				Integer numb1 = Integer.valueOf(name1.substring(name1.indexOf("№") + 1, name1.lastIndexOf(".")));
				
				String name2 = f2.getName();
				Integer numb2 = Integer.valueOf(name2.substring(name2.indexOf("№") + 1, name2.lastIndexOf(".")));
				
				return numb1.compareTo(numb2);
			}
		};
		
		Arrays.sort(arrFiles, comp);
		
		
		List<File> lst = Arrays.asList(arrFiles);

		int i = 1;
		for (File file : lst) {
			prom_fix_list_init.addElement(file.getName());
			Data.PROM_FIXS.add(new Fix(i, file.getName()));
			i++;
		}

		JList<String> prom_fix_list = new JList<>(prom_fix_list_init);
		prom_fix_list.setCellRenderer(new PromFixCheckboxListCellRenderer<String>());
		prom_fix_list.addListSelectionListener(listSelectionDocument);
		prom_fix_list.setSelectionModel(new DefaultListSelectionModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void setSelectionInterval(int index0, int index1) {
				if (super.isSelectedIndex(index0)) {
					if (index0 >= 0)
						Data.PROM_FIXS.get(index0).CHECKED = false;
					super.removeSelectionInterval(index0, index1);
				} else {
					if (index0 >= 0)
						Data.PROM_FIXS.get(index0).CHECKED = true;
					super.addSelectionInterval(index0, index1);
				}
			}
		});
		prom_fix_list.setLayoutOrientation(JList.VERTICAL);
	    
		return prom_fix_list;
	}

	public static Logger getLogger() {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("C:/AisNalogUtility/log.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (writer != null) {
			writer.print("");
			writer.close();
		}
		

		Logger logger = Logger.getLogger("AisNalogUtilityLog");
		FileHandler fh;

		try {

			fh = new FileHandler("C:/AisNalogUtility/log.txt");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return logger;
	}

	public static JPanel getPanelPromFixList() {
		JScrollPane scroll_pane_prom_fix = new JScrollPane();
		scroll_pane_prom_fix.setViewportView(getPromFixList());
		
		scroll_pane_prom_fix.getViewport().setViewPosition(new Point(0,99999));

		JPanel panel_prom_fix = new JPanel(new BorderLayout());

		panel_prom_fix.add(scroll_pane_prom_fix);
		
		panel_prom_fix.setMinimumSize(new Dimension(300,0));
		
		WIDTH += 300;

		return panel_prom_fix;
	}
	
	public static JPanel getPanelAppProm() {

		JPanel panel_install = new JPanel();
		panel_install.setBorder(new EmptyBorder(10, 10, 10, 10));
		GridLayout layout = new GridLayout(3, 0, 0, 0);
		panel_install.setLayout(layout);
		
		JLabel old_version = new JLabel("Установленная версия: " + APP_PROM_VERSION, SwingConstants.CENTER);
		JLabel new_version = new JLabel("Актуальная версия: " + Data.CONFIG_APP.PROM_VERSION, SwingConstants.CENTER);

		panel_install.add(old_version);
		panel_install.add(buttonInstallAisProm());
		panel_install.add(new_version);
		
		panel_install.setMinimumSize(new Dimension(250,0));
		
		WIDTH += 250;
		
		return panel_install;
	}

	public static JPanel getPanelPromFixInstall() {

		JPanel panel_install = new JPanel();
		panel_install.setBorder(new EmptyBorder(10, 10, 10, 10));
		GridLayout layout = new GridLayout(4, 0, 0, 0);
		panel_install.setLayout(layout);
		
		String path = "";
		String version = "";
		if (new File("c:\\Program Files (x86)\\Ais3Prom\\Client\\version.txt").exists()) {
			APP_PROM_PATH = "c:\\Program Files (x86)\\Ais3Prom\\";
			path = APP_PROM_PATH + "Client\\version.txt";
			APP_PROM_INSTALLED = true;
		} else if (new File("c:\\Program Files\\Ais3Prom\\Client\\version.txt").exists()) {
			APP_PROM_PATH = "c:\\Program Files\\Ais3Prom\\";
			path = APP_PROM_PATH + "Client\\version.txt";
			APP_PROM_INSTALLED = true;
		} else {
			version = "НЕ УСТАНОВЛЕН!";
		}
		
		File file = new File(path);
		
		if (file.exists()) {
			try {
				version = String.join("", FileUtils.readLines(file, StandardCharsets.UTF_8));
			} catch (IOException e) {
				LOGGER.log(Level.WARNING, e.getMessage());
				e.printStackTrace();
			}
		}
		
		APP_PROM_VERSION = version;
		
		panel_install.add(buttonUninstalledPromFix());
		panel_install.add(buttonAllPromFix());
		panel_install.add(buttonCheckedPromFix());
		panel_install.add(buttonUncheckedPromFix());
		
		if(!APP_PROM_INSTALLED) {
			BUTTON_INSTALL_UNINSTALLED_PROM_FIX.setEnabled(false);
			BUTTON_INSTALL_ALL_PROM_FIX.setEnabled(false);
			BUTTON_INSTALL_CHECKED_PROM_FIX.setEnabled(false);
			BUTTON_INSTALL_UNCHECKED_PROM_FIX.setEnabled(false);
		}
		
		panel_install.setMinimumSize(new Dimension(270,0));
		
		WIDTH += 270;
		
		return panel_install;
	}

	public static void main(String[] args) {
		LOGGER = getLogger();

		String arg = args[0];

		if (arg.equals("-run"))
			runAppRun();
		if (arg.equals("-app"))
			runAppMain();
		if (arg.equals("-auth"))
			runAppAuth();
	}

	public static void processBuilderStart(String[] commands, boolean exit) throws InterruptedException {
		ProcessBuilder pb = new ProcessBuilder(commands);
		pb.redirectError();
		try {
			Process process = pb.start();
			process.waitFor();
			if(exit) {
				LoadingThread.IS_RUN = false;
				ConfigInstalled.save();
				APP.setVisible(false);
				APP.dispose();
			}
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.log(Level.WARNING, e.getMessage());
		}
		
	}
	
	public static JMenuBar menuBar() {
		JMenuBar menubar = new JMenuBar();
		
		JMenu menu = new JMenu("Файл");
		
		JMenuItem itm = new JMenuItem("Выйти");
		menu.add(itm);
		itm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				APP.setVisible(false);
				APP.dispose();
			}
		});
		
		menubar.add(menu);
		
		menu = new JMenu("Справка");

		itm = new JMenuItem("Помощь");
		menu.add(itm);
		itm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, ""
						+ "При нажатии кнопок:\n"
						+ "    1) <<Установить новые фиксы>> - выполнится установка фиксов, которые не были установлены ранее.\n"
						+ "    2) <<Установить все фиксы>> - выполнится установка всех фиксов.\n"
						+ "    3) <<Установить выбранные фиксы>> - выполнится установка всех выбранных фиксов.\n"
						+ "    4) <<Установить все фиксы кроме выбранных>> - выполнится установка всех фиксов кроме выбранных.\n\n"
						+ "Цвет фиксов:\n:"
						+ "    1) Красный - не установлен.\n"
						+ "    2) Зелёным - установлен.", "Справка", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		   
		itm = new JMenuItem("О программе");
		itm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "УФНС России по Хабаровскому краю.\n"
						+ "Программа для установки фиксов на АИС Налог-3 ПРОМ.", "О программе", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		menu.add(itm);
   
		menubar.add(menu);
		
		MENU_BAR = menubar;
		return menubar;
	}

	private static void runApp() {
		ConfigApp.getConfig();

		APP = new JFrame();
		APP.setTitle("Утилита для АИС Налог 3 ПРОМ (v" + APP_VERSION + ")");
		APP.setSize(WIDTH, HEIGTH);
		WIDTH = 0;
		APP.setVisible(true);
		APP.setLocationRelativeTo(null);
		APP.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		APP.setJMenuBar(menuBar());
		
		SPLIT_PANE_PROM_INSTALL = new JSplitPane();
		SPLIT_PANE_PROM_INSTALL.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		SPLIT_PANE_PROM_INSTALL.setLeftComponent(getPanelPromFixInstall());
		SPLIT_PANE_PROM_INSTALL.setRightComponent(getPanelAppProm());

		SPLIT_PANE_PROM = new JSplitPane();
		SPLIT_PANE_PROM.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		SPLIT_PANE_PROM.setLeftComponent(SPLIT_PANE_PROM_INSTALL);
		SPLIT_PANE_PROM.setRightComponent(getPanelPromFixList());
		
		APP.setSize(WIDTH, HEIGTH);

		APP.add(SPLIT_PANE_PROM);
		
		ConfigInstalled.getConfig();
	}

	public static void runAppAuth() {
		Console terminal = System.console();
		String login = terminal.readLine("Input local admin login: ");
		char[] password = terminal.readPassword("Input local admin password: ");

		Data.CONFIG_ADMIN = new ConfigAdmin(login, password);

		String crypt = new Gson().toJson(Data.CONFIG_ADMIN, ConfigAdmin.class);
		crypt = Crypt.encrypt(crypt);
		try (FileWriter file = new FileWriter("c:\\AisNalogUtility\\config\\auth")) {
			file.write(crypt);
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.log(Level.WARNING, e.getMessage());
		}
	}

	public static void runAppMain() {
		if (!checkPrivileges()) {
			Elevator.executeAsAdministrator("c:\\AisNalogUtility\\java\\bin\\javaw.exe ",
					"-jar c:\\AisNalogUtility\\app\\AisNalogUtility.jar -app");
		} else {
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					runApp();
				}
			});
		}
	}

	public static void runAppRun() {
		ConfigAdmin.getConfig();

		WString nullW = null;
		PROCESS_INFORMATION processInformation = new PROCESS_INFORMATION();
		STARTUPINFO startupInfo = new STARTUPINFO();
		boolean result = false;
		result = AdvApi32.INSTANCE.CreateProcessWithLogonW(new WString(Data.CONFIG_ADMIN.LOGIN), nullW,
				new WString(Data.CONFIG_ADMIN.PASSWORD), AdvApi32.LOGON_WITH_PROFILE, nullW,
				new WString(
						"c:\\AisNalogUtility\\java\\bin\\javaw.exe -jar c:\\AisNalogUtility\\app\\AisNalogUtility.jar -app"),
				AdvApi32.CREATE_NEW_CONSOLE, null, nullW, startupInfo, processInformation);
		if (!result) {
			int error = Kernel32.INSTANCE.GetLastError();
			System.out.println("OS error #" + error);
			LOGGER.log(Level.WARNING, "OS error #" + error);
			System.out.println(Kernel32Util.formatMessageFromLastErrorCode(error));
			LOGGER.log(Level.WARNING, Kernel32Util.formatMessageFromLastErrorCode(error));
		}
	}

	public static void sendMessage(String text) {
		JOptionPane.showMessageDialog(APP, text, "Важно!", JOptionPane.WARNING_MESSAGE);
	}
}
