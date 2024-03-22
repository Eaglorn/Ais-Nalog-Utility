package ru.eaglorn.aisnalogutility;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JSplitPane;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
	private @val @Getter JFrame frame = new JFrame();
	private @Getter @Setter String promPath = "";
	private @Getter @Setter String promVersion = "";
	private @Getter @Setter String oeVersion = "";
	private @val @Getter String version = "7";
	private @Getter @Setter int width = 0;
	private @Getter @Setter int heigth = 600;
	private @Getter LoadingThread loadingThread = new LoadingThread();
	private @val @Getter JMenuBar menuBar = new JMenuBar();
	private @val @Getter @Setter PromPanelApp promPanelApp;
	private @val @Getter @Setter PromPanelFix promPanelFix;
	private @val @Getter @Setter PromPanelFixList promPanelFixList;
	private @val @Getter JSplitPane promSplitPane = new JSplitPane();
	private @val @Getter JSplitPane promSplitPaneInstall = new JSplitPane();
	private @Getter @Setter int promFixHave = 0;
	private @Getter @Setter int promFixInstalled = 0;
	private @Getter @Setter boolean promInstalled = false;

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
