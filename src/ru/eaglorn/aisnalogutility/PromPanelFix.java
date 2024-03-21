package ru.eaglorn.aisnalogutility;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.apache.commons.io.FileUtils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PromPanelFix {
	private @Getter JLabel info = new JLabel("", SwingConstants.CENTER);

	private JButton buttonUninstalled = new JButton("Установить новые фиксы");
	private JButton buttonAll = new JButton("Установить все фиксы");
	private JButton buttonChecked = new JButton("Установить только выбранные фиксы");
	private JButton buttonUnchecked = new JButton("Установить все фиксы кроме выбранных");

	private @Getter JPanel panel = null;

	private int width = 320;

	public PromPanelFix() {
		App app = AisNalogUtility.getApp();

		panel = new JPanel();
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		GridLayout layout = new GridLayout(5, 0, 0, 0);
		panel.setLayout(layout);

		String path = "";
		String version = "";
		if (new File("c:\\Program Files (x86)\\Ais3Prom\\Client\\version.txt").exists()) {
			app.setPromPath("c:\\Program Files (x86)\\Ais3Prom\\");
			path = app.getPromPath() + "Client\\version.txt";
			app.setPromInstalled(true);
		} else if (new File("c:\\Program Files\\Ais3Prom\\Client\\version.txt").exists()) {
			app.setPromPath("c:\\Program Files\\Ais3Prom\\");
			path = app.getPromPath() + "Client\\version.txt";
			app.setPromInstalled(true);
		} else {
			version = "НЕ УСТАНОВЛЕН!";
		}

		File file = new File(path);

		if (file.exists()) {
			try {
				version = String.join("", FileUtils.readLines(file, StandardCharsets.UTF_8));
			} catch (IOException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}

		app.setPromVersion(version);

		panel.add(info);
		panel.add(createButtonUninstalled());
		panel.add(createButtonAll());
		panel.add(createButtonChecked());
		panel.add(createButtonUnchecked());

		if (!app.isPromInstalled()) {
			buttonUninstalled.setEnabled(false);
			buttonAll.setEnabled(false);
			buttonChecked.setEnabled(false);
			buttonUnchecked.setEnabled(false);
		}

		panel.setMinimumSize(new Dimension(width, 0));

		app.addWidth(width);
	}

	private JButton createButtonUninstalled() {
		App app = AisNalogUtility.getApp();

		buttonUninstalled.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!((JButton) e.getSource()).isEnabled())
					return;

				LoadingThread loadingThread = app.getLoadingThread();
				loadingThread.start();

				Thread thread = new PromFixThread(0);
				thread.start();
			}
		});
		return buttonUninstalled;
	}

	private JButton createButtonAll() {
		App app = AisNalogUtility.getApp();

		buttonAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!((JButton) e.getSource()).isEnabled())
					return;

				LoadingThread loadingThread = app.getLoadingThread();
				loadingThread.start();

				Thread thread = new PromFixThread(1);
				thread.start();
			}
		});
		return buttonAll;
	}

	private JButton createButtonChecked() {
		App app = AisNalogUtility.getApp();

		buttonChecked.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!((JButton) e.getSource()).isEnabled())
					return;

				LoadingThread loadingThread = app.getLoadingThread();
				loadingThread.start();

				Thread thread = new PromFixThread(2);
				thread.start();
			}
		});
		return buttonChecked;
	}

	private JButton createButtonUnchecked() {
		App app = AisNalogUtility.getApp();

		buttonUnchecked.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!((JButton) e.getSource()).isEnabled())
					return;

				LoadingThread loadingThread = app.getLoadingThread();
				loadingThread.start();

				Thread thread = new PromFixThread(3);
				thread.start();
			}
		});
		return buttonUnchecked;
	}
}
