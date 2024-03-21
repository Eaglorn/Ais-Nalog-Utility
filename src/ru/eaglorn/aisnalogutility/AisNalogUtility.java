package ru.eaglorn.aisnalogutility;

import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.WinBase.PROCESS_INFORMATION;
import com.sun.jna.platform.win32.WinBase.STARTUPINFO;

import lombok.Getter;
import lombok.val;

public class AisNalogUtility {
	private static @val Logger logger = LoggerFactory.getLogger(AisNalogUtility.class);
	
	private static @Getter App app;
	private static @val @Getter Data data = new Data();
	
	private static boolean checkPrivileges() {
		try {
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
				if (fileTest != null) fileTest.delete();
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	public static void main(String[] args) {
		try {
			String arg = args[0];
	
			if (arg.equals("-run")) runAppRun();
			if (arg.equals("-app")) runAppMain();
			if (arg.equals("-auth")) runAppAuth();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	private static void runApp() {
		try {
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
			
			app.setPromFixInstalled(data.getConfigFix().getPromFixs().size());
			
			if(app.getPromFixHave() < 1) {
				app.getPromPanelApp().getInfo().setText("Отсутствуют фиксы для установки.");
			} else {
				app.getPromPanelApp().getInfo().setText("Установлено " + app.getPromFixInstalled() + " фиксов из " + app.getPromFixHave() + ".");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
	}

	public static void runAppAuth() {
		try {
			Console terminal = System.console();
			String login = terminal.readLine("Input local admin login: ");
			char[] password = terminal.readPassword("Input local admin password: ");
	
			data.setConfigAdmin(new ConfigAdmin(login, password));
	
			String crypt = new Gson().toJson(data.getConfigAdmin(), ConfigAdmin.class);
			crypt = Crypt.encrypt(crypt);
			try (FileWriter file = new FileWriter("c:\\AisNalogUtility\\config\\auth")) {
				file.write(crypt);
				file.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		} catch (Exception e){
			logger.error(e.getMessage());
		}
	}

	public static void runAppMain() {
		try {
			if (!checkPrivileges()) {
				Elevator.executeAsAdministrator("c:\\AisNalogUtility\\java\\bin\\javaw.exe ",
						"-jar c:\\AisNalogUtility\\app\\AisNalogUtility.jar -app");
			} else {
				javax.swing.SwingUtilities.invokeLater(new Runnable()
				{
					@Override
					public void run() {
						runApp();
					}
				});
			}
		} catch (Exception e){
			logger.error(e.getMessage());
		}
	}

	public static void runAppRun() {
		try {
			ConfigAdmin.getConfig();
	
			WString nullW = null;
			PROCESS_INFORMATION processInformation = new PROCESS_INFORMATION();
			STARTUPINFO startupInfo = new STARTUPINFO();
			boolean result = false;
			result = AdvApi32.INSTANCE.CreateProcessWithLogonW(new WString(data.getConfigAdmin().getLogin()), nullW,
					new WString(data.getConfigAdmin().getPassword()), AdvApi32.LOGON_WITH_PROFILE, nullW,
					new WString("c:\\AisNalogUtility\\java\\bin\\javaw.exe -jar c:\\AisNalogUtility\\app\\AisNalogUtility.jar -app"),
					AdvApi32.CREATE_NEW_CONSOLE, null, nullW, startupInfo, processInformation);
			if (!result) {
				int error = Kernel32.INSTANCE.GetLastError();
				logger.error("OS error # {}", error);
				String messageError = Kernel32Util.formatMessageFromLastErrorCode(error);
				logger.error("OS detail error # {}", messageError);
			}
		} catch (Exception e){
			logger.error(e.getMessage());
		}
	}

	public static void sendMessage(String text) {
		try {
			JOptionPane.showMessageDialog(app.getFrame(), text, "Важно!", JOptionPane.WARNING_MESSAGE);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
