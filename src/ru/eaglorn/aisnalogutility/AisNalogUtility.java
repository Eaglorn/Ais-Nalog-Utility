package ru.eaglorn.aisnalogutility;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.WinBase.PROCESS_INFORMATION;
import com.sun.jna.platform.win32.WinBase.STARTUPINFO;

import lombok.Getter;
import lombok.Setter;
import lombok.val;

public class AisNalogUtility {
	private static final Logger logger = LoggerFactory.getLogger(AisNalogUtility.class);
	
	public static App app = new App();
	
	public static Data data = new Data();

	private static boolean checkPrivileges() {
		File testPriv = new File("c:\\Windows\\");
		if (!testPriv.canWrite())
			return false;
		File fileTest = null;
		try {
			fileTest = File.createTempFile("test", ".dll", testPriv);
		} catch (IOException e) {
			logger.error(e.getMessage());
			return false;
		} finally {
			if (fileTest != null)
				fileTest.delete();
		}
		return true;
	}


	public static void main(String[] args) {
		String arg = args[0];

		if (arg.equals("-run"))
			runAppRun();
		if (arg.equals("-app"))
			runAppMain();
		if (arg.equals("-auth"))
			runAppAuth();
	}

	private static void runApp() {
		ConfigApp.getConfig();

		app.getFrame().setTitle("Утилита для АИС Налог 3 ПРОМ (v" + app.getVersion() + ")");
		app.getFrame().setSize(0, app.getHeigth());
		app.getFrame().setVisible(true);
		app.getFrame().setLocationRelativeTo(null);
		app.getFrame().setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		app.getFrame().setJMenuBar(app.getMenuBar());
		
		app.getPromSplitPaneInstall().setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		app.getPromSplitPaneInstall().setLeftComponent(app.getPromPanelFix().getPanel());
		app.getPromSplitPaneInstall().setRightComponent(app.getPromPanelApp().getPanel());

		app.getPromSplitPane().setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		app.getPromSplitPane().setLeftComponent(app.getPromSplitPaneInstall());
		app.getPromSplitPane().setRightComponent(app.getPromPanelFixList().getPanel());
		
		app.getFrame().add(app.getPromSplitPane());
		
		app.getFrame().setSize(app.getWidth(), app.getHeigth());
		
		ConfigFix.getConfig();
		
		PROM_FIX_INSTALLED = Data.CONFIG_INSTALLED.PROM_INSTALLED.size();
		
		if(PROM_FIX_HAVE < 1) {
			PROM_FIX_INFO.setText("Отсутствуют фиксы для установки.");
		} else {
			PROM_FIX_INFO.setText("Установлено " + PROM_FIX_INSTALLED + " фиксов из " + PROM_FIX_HAVE + ".");
		}
		
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
			logger.error(e.getMessage());
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
			logger.error("OS error #" + error);
			logger.error(Kernel32Util.formatMessageFromLastErrorCode(error));
		}
	}

	public static void sendMessage(String text) {
		JOptionPane.showMessageDialog(app.getFrame(), text, "Важно!", JOptionPane.WARNING_MESSAGE);
	}
}
