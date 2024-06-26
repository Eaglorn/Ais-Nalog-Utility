package ru.eaglorn.aisnalogutility;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import lombok.Getter;
import lombok.Setter;

public class PromPanelFixAndApp {
	private @Getter JLabel info = new JLabel("", SwingConstants.CENTER);
	private @Getter @Setter boolean editInfo = true;
	private @Getter JButton buttonUninstalled = new JButton("Установить новые фиксы");
	private @Getter JButton buttonAll = new JButton("Установить все фиксы");
	private @Getter JButton buttonChecked = new JButton("Установить только выбранные фиксы");
	private @Getter JButton buttonUnchecked = new JButton("Установить все фиксы кроме выбранных");
	private @Getter JPanel panel = new JPanel();
	private int width = 340;

	public PromPanelFixAndApp() {
		App app = AisNalogUtility.getApp();
		ConfigApp configApp = AisNalogUtility.getData().getConfigApp(); 
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setLayout(new GridLayout(9, 0, 0, 0));
		panel.add(info);
		panel.add(createButtonUninstalled());
		panel.add(createButtonAll());
		panel.add(createButtonChecked());
		panel.add(createButtonUnchecked());
		panel.add(new JSeparator());
		String appPromVersion = app.getPromVersion();
		String configPromVersion = configApp.getPromVersion();
		panel.add(new JLabel("Установленная версия: " + appPromVersion, SwingConstants.CENTER));
		panel.add(buttonInstallProm());
		panel.add(new JLabel("Актуальная версия: " + configPromVersion,
				SwingConstants.CENTER));
		if (!app.isPromInstalled()) {
			disableButtonFixs();
		}
		if (app.getPromFixHave() != 0 && !appPromVersion.equals(configPromVersion)) {
			info.setText("Доступные фиксы относятся к другой версии");
			editInfo = false;
			disableButtonFixs();
		}
		panel.setMinimumSize(new Dimension(width, 0));
		app.addWidth(width);
	}

	private JButton buttonInstallProm() {
		App app = AisNalogUtility.getApp();
		String text;
		if (app.isPromInstalled()) {
			if (app.getPromVersion().equals( AisNalogUtility.getData().getConfigApp().getPromVersion())) {
				text = "Переустановить АИС-Налог 3 ПРОМ";
			} else {
				text = "Установить новую версию АИС-Налог 3 ПРОМ";
			}
		} else {
			text = "Установить АИС-Налог 3 ПРОМ";
		}
		JButton button = new JButton(text);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!((JButton) e.getSource()).isEnabled()) {
					return;
				}
				LoadingThread loadingThread = app.getLoadingThread();
				loadingThread.setType(1);
				loadingThread.start();
				Thread thread = new PromAppThread();
				thread.start();
			}
		});
		return button;
	}

	private JButton createButtonAll() {
		buttonAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!((JButton) e.getSource()).isEnabled()) {
					return;
				}
				AisNalogUtility.getApp().getLoadingThread().start();
				new PromFixThread(1).start();
			}
		});
		return buttonAll;
	}

	private JButton createButtonChecked() {
		buttonChecked.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!((JButton) e.getSource()).isEnabled()) {
					return;
				}
				AisNalogUtility.getApp().getLoadingThread().start();
				new PromFixThread(2).start();
			}
		});
		return buttonChecked;
	}

	private JButton createButtonUnchecked() {
		buttonUnchecked.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!((JButton) e.getSource()).isEnabled()) {
					return;
				}
				AisNalogUtility.getApp().getLoadingThread().start();
				new PromFixThread(3).start();
			}
		});
		return buttonUnchecked;
	}

	private JButton createButtonUninstalled() {
		buttonUninstalled.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!((JButton) e.getSource()).isEnabled()) {
					return;
				}
				AisNalogUtility.getApp().getLoadingThread().start();
				new PromFixThread(0).start();
			}
		});
		return buttonUninstalled;
	}
	
	public void disableButtonFixs() {
		buttonUninstalled.setEnabled(false);
		buttonAll.setEnabled(false);
		buttonChecked.setEnabled(false);
		buttonUnchecked.setEnabled(false);
	}
}
