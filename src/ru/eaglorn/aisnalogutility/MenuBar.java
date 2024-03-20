package ru.eaglorn.aisnalogutility;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class MenuBar {
	
	
	public MenuBar() {
		JMenu menu = new JMenu("Файл");
		
		JMenuItem itm = new JMenuItem("Выйти");
		menu.add(itm);
		itm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AisNalogUtility.app.getFrame().setVisible(false);
				AisNalogUtility.app.getFrame().dispose();
			}
		});
		
		AisNalogUtility.app.getMenuBar().add(menu);
		
		menu = new JMenu("Справка");

		itm = new JMenuItem("Помощь");
		menu.add(itm);
		itm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,"При нажатии кнопок:\n"
						+ "    1) <<Установить новые фиксы>> - выполнится установка фиксов, которые не были установлены ранее.\n"
						+ "    2) <<Установить все фиксы>> - выполнится установка всех фиксов.\n"
						+ "    3) <<Установить выбранные фиксы>> - выполнится установка всех выбранных фиксов.\n"
						+ "    4) <<Установить все фиксы кроме выбранных>> - выполнится установка всех фиксов кроме выбранных.\n"
						+ "    5) <<Установить АИС-Налог 3 ПРОМ>> - Если АСИ Налог-3 ПРОМ не установлен, то выполнится установка АИС\n"
						+ "                                         Налог-3 ПРОМ, иначе выполнится переустановка АС Налог-3 ПРОМ с\n"
						+ "                                         удалением всех установленных фиксов.\n\n"
						+ "Цвет фиксов:\n"
						+ "    1) Красный - не установлен.\n"
						+ "    2) Зелёным - установлен.\n\n" 
						+ "По вопросам или ошибками в работе программы обращаться в ФКУ", "Справка", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		   
		itm = new JMenuItem("О программе");
		itm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "УФНС России по Хабаровскому краю.\n\n"
						+ "Программа для установки фиксов на АИС Налог-3 ПРОМ.\n\n"
						+ "Версия программы - 7\n\n"
						+ "Дата выпуска данной версии - 20.03.2024", "О программе", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		menu.add(itm);
   
		AisNalogUtility.app.getMenuBar().add(menu);
	}
}
