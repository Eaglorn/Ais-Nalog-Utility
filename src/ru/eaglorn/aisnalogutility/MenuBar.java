package ru.eaglorn.aisnalogutility;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class MenuBar {
	public MenuBar() {
		App app = AisNalogUtility.getApp();
		JMenu menu = new JMenu("Файл");
		JMenuItem itm = new JMenuItem("Выйти");
		menu.add(itm);
		itm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				app.getFrame().setVisible(false);
				app.getFrame().dispose();
			}
		});
		app.getMenuBar().add(menu);
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
						+ "По вопросам или ошибкам в работе программы обращаться в ФКУ", "Справка",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		itm = new JMenuItem("О программе");
		itm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"УФНС России по Хабаровскому краю.\n\n"
								+ "Программа для установки фиксов, установки или переустановки АИС Налог-3 ПРОМ.\n\n"
								+ "Версия программы - " + app.getAppVersion() + "\n\n"
								+ "Дата выпуска данной версии - 29.03.2024",
						"О программе", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		menu.add(itm);
		app.getMenuBar().add(menu);
	}
}
