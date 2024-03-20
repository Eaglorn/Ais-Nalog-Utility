package ru.eaglorn.aisnalogutility;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import lombok.Getter;

public class PromPanelApp {
	
	private @Getter JPanel panel = new JPanel();
	
	public PromPanelApp() {
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		GridLayout layout = new GridLayout(3, 0, 0, 0);
		panel.setLayout(layout);
		
		JLabel old_version = new JLabel("Установленная версия: " + AisNalogUtility.APP_PROM_VERSION, SwingConstants.CENTER);
		JLabel new_version = new JLabel("Актуальная версия: " + Data.CONFIG_APP.PROM_VERSION, SwingConstants.CENTER);

		panel.add(old_version);
		panel.add(buttonInstall());
		panel.add(new_version);
		
		panel.setMinimumSize(new Dimension(235,0));
		
		AisNalogUtility.WIDTH += 235;
	}
	
	public static JButton buttonInstall() {
		JButton buttonInstall = new JButton("Установить АИС-Налог 3 ПРОМ");
		buttonInstall.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!((JButton) e.getSource()).isEnabled())
					return;

				LoadingThread.IS_RUN = true;
				AisNalogUtility.LOAD_THREAD = new LoadingThread();
				LoadingThread.TYPE_INSTALL = 1;
				AisNalogUtility.LOAD_THREAD.start();
				Thread app_prom_thread = new PromAppThread();
				app_prom_thread.start();
			}
		});
		return buttonInstall;
	}
	
	
}
