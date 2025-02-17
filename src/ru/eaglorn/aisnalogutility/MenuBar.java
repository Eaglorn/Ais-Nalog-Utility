package ru.eaglorn.aisnalogutility;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class MenuBar {
	public MenuBar() {
		App app = AisNalogUtility.getApp();
		JFrame frame = app.getFrame();
		JMenu menu = new JMenu("Файл");
		JMenuItem itm = new JMenuItem("Выйти");
		menu.add(itm);
		itm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				frame.dispose();
			}
		});
		JMenuBar menuBar = app.getMenuBar();
		menuBar.add(menu);
		menu = new JMenu("Справка");
		itm = new JMenuItem("Помощь");
		menu.add(itm);
		itm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "При нажатии кнопок:\n"
						+ "    1)  <<Установить новые фиксы>> - выполнится установка фиксов, которые не были установлены ранее.\n"
						+ "    2)  <<Установить все фиксы>> - выполнится установка всех фиксов.\n"
						+ "    3)  <<Установить выбранные фиксы>> - выполнится установка всех выбранных фиксов.\n"
						+ "    4)  <<Установить все фиксы кроме выбранных>> - выполнится установка всех фиксов кроме выбранных.\n"
						+ "    5)  <<Установить АИС-Налог 3 ПРОМ>> - Выполнится установка АИС Налог-3 ПРОМ\n"
						+ "    6)  <<Установить новую версию АИС-Налог 3 ПРОМ>> - Выполнится установка новой версии АИС Налог-3 ПРОМ\n"
						+ "    7)  <<Переустановить АИС-Налог 3 ПРОМ>> - Выполнится переустановка текущей версии АИС Налог-3 ПРОМ\n"
						+ "    8)  <<Установить АИС-Налог 3 ОЭ>> - Выполнится установка АИС Налог-3 ОЭ\n"
						+ "    9)  <<Установить новую версию АИС-Налог 3 ОЭ>> - Выполнится установка новой версии АИС Налог-3 ОЭ\n"
						+ "    10) <<Переустановить АИС-Налог 3 ОЭ>> - Выполнится переустановка текущей версии АИС Налог-3 ОЭ\n"
						+ "Цвет фиксов:\n" + "    1) Красный - не установлен.\n" + "    2) Зелёным - установлен.\n\n"
						+ "По вопросам или ошибкам в работе программы обращаться в ФКУ", "Помощь",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		menu.add(itm);
		itm = new JMenuItem("О программе");
		itm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"УФНС России по Хабаровскому краю.\n\n"
								+ "Утилита для АИС Налог-3 ПРОМ(Установка фиксов, установка или переустановка программы), АИС Налог-3 ОЭ (Установка или переустановка программы).\n\n"
								+ "Версия программы - " + app.getAppVersion() + "\n\n"
								+ "Дата выпуска данной версии - 27.06.2024",
						"О программе", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		menu.add(itm);
		menuBar.add(menu);
	}
}
