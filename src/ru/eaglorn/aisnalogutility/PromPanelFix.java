package ru.eaglorn.aisnalogutility;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.commons.io.FileUtils;

import lombok.Getter;

public class PromPanelFix {
	
	private @Getter JPanel panel = null;
	
	private JButton buttonUninstalled = new JButton("Установить новые фиксы");
	private JButton buttonAll = new JButton("Установить все фиксы");
	private JButton buttonChecked = new JButton("Установить только выбранные фиксы");
	private JButton buttonUnchecked = new JButton("Установить все фиксы кроме выбранных");
	
	public PromPanelFix() {
		panel = new JPanel();
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		GridLayout layout = new GridLayout(5, 0, 0, 0);
		panel.setLayout(layout);
		
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
				LOGGER.error(e.getMessage());
				e.printStackTrace();
			}
		}
		
		APP_PROM_VERSION = version;
		
		panel.add(PROM_FIX_INFO);
		panel.add(createButtonUninstalled());
		panel.add(createButtonAll());
		panel.add(createButtonChecked());
		panel.add(createButtonUnchecked());
		
		if(!APP_PROM_INSTALLED) {
			buttonUninstalled.setEnabled(false);
			buttonAll.setEnabled(false);
			buttonChecked.setEnabled(false);
			buttonUnchecked.setEnabled(false);
		}
		
		panel.setMinimumSize(new Dimension(320,0));
		
		WIDTH += 320;
	}
	
	public JButton createButtonUninstalled() {
		buttonUninstalled.addActionListener(new ActionListener() {
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
		return buttonUninstalled;
	}

	public JButton createButtonAll() {
		buttonAll.addActionListener(new ActionListener() {
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
		return buttonAll;
	}

	public JButton createButtonChecked() {
		buttonChecked.addActionListener(new ActionListener() {
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
		return buttonChecked;
	}
	
	public JButton createButtonUnchecked() {
		buttonUnchecked.addActionListener(new ActionListener() {
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
		return buttonUnchecked;
	}
}
