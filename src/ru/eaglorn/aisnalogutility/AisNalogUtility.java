package ru.eaglorn.aisnalogutility;

import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JFrame;
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
		if (!testPriv.canWrite())
			return false;
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
			if (arg.equals("-run"))
				runAppRun();
			if (arg.equals("-app"))
				runAppMain();
			if (arg.equals("-auth"))
				runAppAuth();
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
		app.getFrame().setSize(0, app.getHeigth());
		app.getFrame().setVisible(true);
		app.getFrame().setLocationRelativeTo(null);
		app.getFrame().setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		new MenuBar();
		frame.setJMenuBar(app.getMenuBar());
		app.getSplitPaneProm().setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		app.setPromPanelFixAndApp(new PromPanelFixAndApp());
		app.getSplitPaneProm().setLeftComponent(app.getPromPanelFixAndApp().getPanel());
		app.setPromPanelFix(new PromPanelFix());
		app.getSplitPaneProm().setRightComponent(app.getPromPanelFix().getPanel());
		app.getSplitPane().setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		app.getSplitPane().setLeftComponent(app.getSplitPaneProm());
		app.setOePanelFixAndApp(new OePanelFixAndApp());
		app.getSplitPane().setRightComponent(app.getOePanelFixAndApp().getPanel());
		frame.add(app.getSplitPane());
		frame.setSize(app.getWidth(), app.getHeigth());
		app.setPromFixInstalled(data.getConfigFix().getPromFixs().size());
		if (app.getPromFixHave() < 1) {
			app.getPromPanelFixAndApp().getInfo().setText("Отсутствуют фиксы для установки.");
			app.getPromPanelFixAndApp().disableButtonFixs();
		} else {
			app.getPromPanelFixAndApp().getInfo()
					.setText("Установлено " + app.getPromFixInstalled() + " фиксов из " + app.getPromFixHave() + ".");
		}
		frame.setSize(app.getWidth(), app.getHeigth());
		frame.setLocationRelativeTo(null);
	}

	public static void runAppAuth() {
		Console terminal = System.console();
		String login = terminal.readLine("Input local admin login: ");
		char[] password = terminal.readPassword("Input local admin password: ");
		
		ConfigAdmin configAdmin = new ConfigAdmin(login, password);
		
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
		PROCESS_INFORMATION processInformation = new PROCESS_INFORMATION();
		STARTUPINFO startupInfo = new STARTUPINFO();
		boolean result = false;
		result = AdvApi32.INSTANCE.CreateProcessWithLogonW(new WString(configAdmin.getLogin()), nullW,
				new WString(configAdmin.getPassword()), AdvApi32.LOGON_WITH_PROFILE, nullW,
				new WString(
						"c:\\AisNalogUtility\\java\\bin\\javaw.exe -Dlog4j.configurationFile=log4j2.xml -jar c:\\AisNalogUtility\\app\\AisNalogUtility.jar -app"),
				AdvApi32.CREATE_NEW_CONSOLE, null, nullW, startupInfo, processInformation);
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
