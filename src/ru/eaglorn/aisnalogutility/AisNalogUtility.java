package ru.eaglorn.aisnalogutility;

import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AisNalogUtility {
	private static @val @Getter App app = new App();
	private static @val @Getter Data data = new Data();

	public static void cleanUp(Path path) throws IOException {
		Files.delete(path);
	}

	private static boolean checkPrivileges() {
		File testPriv = new File("c:\\Windows\\");
		if (!testPriv.canWrite())
			return false;
		File fileTest = null;
		try {
			fileTest = File.createTempFile("test", ".dll", testPriv);
		} catch (IOException e) {
			log.error(e.getMessage());
			return false;
		} finally {
			if (fileTest != null) {
				try {
					cleanUp(Path.of(fileTest.getAbsolutePath()));
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
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
		JFrame frame = app.getFrame();
		frame.setTitle("Утилита для АИС Налог 3 (v" + app.getVersion() + ")");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		new MenuBar();
		frame.setJMenuBar(app.getMenuBar());
		app.getPromSplitPaneInstall().setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		app.setPromPanelFix(new PromPanelFix());
		app.getPromSplitPaneInstall().setLeftComponent(app.getPromPanelFix().getPanel());
		app.setPromPanelApp(new PromPanelApp());
		app.getPromSplitPaneInstall().setRightComponent(app.getPromPanelApp().getPanel());
		app.getPromSplitPane().setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		app.getPromSplitPane().setLeftComponent(app.getPromSplitPaneInstall());
		app.setPromPanelFixList(new PromPanelFixList());
		app.getPromSplitPane().setRightComponent(app.getPromPanelFixList().getPanel());
		frame.add(app.getPromSplitPane());
		frame.setSize(app.getWidth(), app.getHeigth());
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		ConfigFix.getConfig();
		app.setPromFixInstalled(data.getConfigFix().getPromFixs().size());
		if (app.getPromFixHave() < 1) {
			app.getPromPanelFix().getInfo().setText("Отсутствуют фиксы для установки.");
		} else {
			app.getPromPanelFix().getInfo()
					.setText("Установлено " + app.getPromFixInstalled() + " фиксов из " + app.getPromFixHave() + ".");
		}
	}

	public static void runAppAuth() {
		Console terminal = System.console();
		String login = terminal.readLine("Input local admin login: ");
		char[] password = terminal.readPassword("Input local admin password: ");
		data.setConfigAdmin(new ConfigAdmin(login, password));
		String crypt = new Gson().toJson(data.getConfigAdmin(), ConfigAdmin.class);
		crypt = Crypt.encrypt(crypt);
		try (FileWriter file = new FileWriter("c:\\AisNalogUtility\\config\\auth")) {
			file.write(crypt);
			file.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
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
		ConfigAdmin.getConfig();
		WString nullW = null;
		PROCESS_INFORMATION processInformation = new PROCESS_INFORMATION();
		STARTUPINFO startupInfo = new STARTUPINFO();
		boolean result = false;
		result = AdvApi32.INSTANCE.CreateProcessWithLogonW(new WString(data.getConfigAdmin().getLogin()), nullW,
				new WString(data.getConfigAdmin().getPassword()), AdvApi32.LOGON_WITH_PROFILE, nullW,
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
