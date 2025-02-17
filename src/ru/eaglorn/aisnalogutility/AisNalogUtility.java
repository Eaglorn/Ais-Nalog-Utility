package ru.eaglorn.aisnalogutility;

import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import com.google.gson.Gson;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.WinBase.PROCESS_INFORMATION;
import com.sun.jna.platform.win32.WinBase.STARTUPINFO;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AisNalogUtility {
	private static @Getter App app = null;
	private static @Getter Data data = null;

	private static boolean checkPrivileges() {
		File testPriv = new File("c:\\Windows\\");
		if (!testPriv.canWrite()) {
			return false;
		}
		File fileTest = null;
		try {
			fileTest = File.createTempFile("test", ".dll", testPriv);
		} catch (IOException e) {
		}
		if (fileTest != null) {
			if (fileTest.delete()) {
				return true;
			} else {
				log.info("File Test Not Delete: " + fileTest.getName());
				return true;
			}
		} else {
			return false;
		}
	}

	public static void main(String[] args) {
		try {
			String arg = args[0];
			if (arg.equals("-run")) {
				runAppRun();
			}
			if (arg.equals("-app")) {
				runAppMain();
			}
			if (arg.equals("-auth")) {
				runAppAuth();
			}
		} catch (Exception e) {
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			log.error(stack.toString());
		}
	}

	private static void runApp() {
		data = new Data();
		app = new App();
		ConfigApp.getConfig();
		ConfigFix.getConfig();
		String osArch = System.getProperty("os.arch").toLowerCase();
		if (osArch.contains("64")) {
			app.setWinArch(true);
		}
		JFrame frame = app.getFrame();
		frame.setTitle("Утилита для АИС Налог 3 (v" + app.getAppVersion() + ")");
		frame.setSize(0, app.getHeigth());
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		new MenuBar();
		frame.setJMenuBar(app.getMenuBar());
		JSplitPane splitPaneProm = app.getSplitPaneProm();
		splitPaneProm.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		app.setPromPanelFix(new PromPanelFix());
		splitPaneProm.setRightComponent(app.getPromPanelFix().getPanel());
		app.setPromPanelFixAndApp(new PromPanelFixAndApp());
		splitPaneProm.setLeftComponent(app.getPromPanelFixAndApp().getPanel());
		JSplitPane splitPane = app.getSplitPane();
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setLeftComponent(splitPaneProm);
		app.setOePanelFixAndApp(new OePanelFixAndApp());
		splitPane.setRightComponent(app.getOePanelFixAndApp().getPanel());
		frame.add(splitPane);
		frame.setSize(app.getWidth(), app.getHeigth());
		app.setPromFixInstalled(data.getConfigFix().getPromFixs().size());
		int promFixHave = app.getPromFixHave();
		PromPanelFixAndApp promPanelFixAndApp = app.getPromPanelFixAndApp();
		boolean editInfo = promPanelFixAndApp.isEditInfo();
		JLabel info = promPanelFixAndApp.getInfo();
		if (promFixHave < 1) {
			if(editInfo) {
				info.setText("Отсутствуют фиксы для установки.");
			}
			promPanelFixAndApp.disableButtonFixs();
		} else {
			if(app.getPromFixInstalled() == promFixHave) {
				promPanelFixAndApp.getButtonUninstalled().setEnabled(false);
			}
			if(editInfo) {
				info.setText("Установлено " + app.getPromFixInstalled() + " фиксов из " + promFixHave + ".");
			}
		} 
		frame.setSize(app.getWidth(), app.getHeigth());
		frame.setLocationRelativeTo(null);
	}

	public static void runAppAuth() {
		Console terminal = System.console();
		ConfigAdmin configAdmin = new ConfigAdmin(terminal.readLine("Input local admin login: "), terminal.readPassword("Input local admin password: "));
		String crypt = new Gson().toJson(configAdmin, ConfigAdmin.class);
		crypt = Crypt.encrypt(crypt);
		try (FileWriter file = new FileWriter("c:\\AisNalogUtility\\config\\auth")) {
			file.write(crypt);
			file.flush();
		} catch (IOException e) {
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			log.error(stack.toString());
		}
	}

	public static void runAppMain() {
		if (!checkPrivileges()) {
			Elevator.executeAsAdministrator("c:\\AisNalogUtility\\java\\bin\\javaw.exe ",
					"-Dlog4j.configurationFile=log4j2.xml -jar c:\\AisNalogUtility\\app\\AisNalogUtility.jar -app");
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
		ConfigAdmin configAdmin = ConfigAdmin.getConfig();
		WString nullW = null;
		STARTUPINFO startupInfo = new STARTUPINFO();
		boolean result = false;
		result = AdvApi32.INSTANCE.CreateProcessWithLogonW(new WString(configAdmin.getLogin()), nullW,
				new WString(configAdmin.getPassword()), AdvApi32.LOGON_WITH_PROFILE, nullW,
				new WString(
						"c:\\AisNalogUtility\\java\\bin\\javaw.exe -Dlog4j.configurationFile=log4j2.xml -jar c:\\AisNalogUtility\\app\\AisNalogUtility.jar -app"),
				AdvApi32.CREATE_NEW_CONSOLE, null, nullW, startupInfo, new PROCESS_INFORMATION());
		if (!result) {
			int error = Kernel32.INSTANCE.GetLastError();
			log.error("OS error # {}", error);
			String messageError = Kernel32Util.formatMessageFromLastErrorCode(error);
			log.error("OS detail error # {}", messageError);
		}
	}

	public static void sendMessage(String text) {
		JOptionPane.showMessageDialog(app.getFrame(), text, "Важно!", JOptionPane.WARNING_MESSAGE);
	}
}
