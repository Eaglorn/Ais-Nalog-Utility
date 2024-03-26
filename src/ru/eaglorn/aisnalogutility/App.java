package ru.eaglorn.aisnalogutility;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JSplitPane;

import org.apache.commons.io.FileUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
	private @val @Getter JFrame frame = new JFrame();
	private @Getter @Setter String promPath = "";
	private @Getter @Setter String promVersion = "";
	private @Getter @Setter String oePath = "";
	private @Getter @Setter String oeVersion = "";
	private @val @Getter String appVersion = "8";
	private @Getter @Setter int width = 0;
	private @Getter @Setter int heigth = 600;
	private @Getter LoadingThread loadingThread = new LoadingThread();
	private @val @Getter JMenuBar menuBar = new JMenuBar();
	private @val @Getter @Setter AisPanelApp promPanelApp;
	private @val @Getter @Setter PromPanelFix promPanelFix;
	private @val @Getter @Setter PromPanelFixList promPanelFixList;
	private @val @Getter JSplitPane promSplitPane = new JSplitPane();
	private @val @Getter JSplitPane promSplitPaneInstall = new JSplitPane();
	private @Getter @Setter int promFixHave = 0;
	private @Getter @Setter int promFixInstalled = 0;
	private @Getter @Setter boolean promInstalled = false;
	private @Getter @Setter boolean oeInstalled = false;

	private @val String prom32Path = "c:\\Program Files\\Ais3Prom\\";
	private @val String prom64Path = "c:\\Program Files (x86)\\Ais3Prom\\";

	private @val String oe32Path = "c:\\Program Files\\Ais3FB3OE\\";
	private @val String oe64Path = "c:\\Program Files (x86)\\Ais3FB3OE\\";

	private @val String versionPath = "Client\\CSC.ClientPackage.ver";

	public App() {
		getAisPromVersion();
		getAisOeVersion();
	}

	private void getAisPromVersion() {
		String path = "";
		if (new File(prom64Path + versionPath).exists()) {
			promPath = prom64Path;
			path = promPath + versionPath;
			promInstalled = true;
		} else if (new File(prom32Path + versionPath).exists()) {
			promPath = prom32Path;
			path = promPath + versionPath;
			promInstalled = true;
		} else {
			promVersion = "НЕ УСТАНОВЛЕН!";
			return;
		}
		File file = new File(path);
		if (file.exists()) {
			try {
				promVersion = String.join("", FileUtils.readLines(file, StandardCharsets.UTF_8).get(0));
			} catch (IOException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private void getAisOeVersion() {
		String path = "";
		if (new File(oe64Path + versionPath).exists()) {
			oePath = oe64Path;
			path = oePath + versionPath;
			oeInstalled = true;
		} else if (new File(oe32Path + versionPath).exists()) {
			oePath = oe32Path;
			path = oePath + versionPath;
			oeInstalled = true;
		} else {
			oeVersion = "НЕ УСТАНОВЛЕН!";
			return;
		}
		File file = new File(path);
		if (file.exists()) {
			try {
				oeVersion = String.join("", FileUtils.readLines(file, StandardCharsets.UTF_8).get(0));
			} catch (IOException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public void addWidth(int add) {
		width += add;
	}

	public void processBuilderStart(String[] commands, boolean exit) throws InterruptedException {
		ProcessBuilder pb = new ProcessBuilder(commands);
		pb.redirectError();
		try {
			Process process = pb.start();
			process.waitFor();
			if (exit) {
				loadingThread.setWork(false);
				ConfigFix.save();
				frame.setVisible(false);
				frame.dispose();
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
}
