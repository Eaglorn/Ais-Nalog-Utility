package ru.eaglorn.aisnalogutility;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JSplitPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;
import lombok.val;

public class App {
	private static @val Logger logger = LoggerFactory.getLogger(App.class);
	
	private @val @Getter JFrame frame = new JFrame();
	
	private @Getter @Setter String promPath = "";
	
	private @Getter @Setter String promVersion = "";
	
	private @val @Getter String version = "7";
	
	private @Getter @Setter int width = 0;
	private @Getter @Setter int heigth = 600;
	
	private @Getter @Setter LoadingThread loadingThread;
	
	private @val @Getter JMenuBar menuBar = new JMenuBar();
	
	private @val @Getter PromPanelApp promPanelApp = new PromPanelApp();
	private @val @Getter PromPanelFix promPanelFix = new PromPanelFix();
	private @val @Getter PromPanelFixList promPanelFixList = new PromPanelFixList();
	
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
			if(exit) {
				loadingThread.setWork(false);
				ConfigFix.save();
				frame.setVisible(false);
				frame.dispose();
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
}
