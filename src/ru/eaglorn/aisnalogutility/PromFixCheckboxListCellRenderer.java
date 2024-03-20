package ru.eaglorn.aisnalogutility;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

class PromFixCheckboxListCellRenderer<E> extends JCheckBox implements ListCellRenderer<E> {
	private static final long serialVersionUID = 5422800800423430296L;

	public PromFixCheckboxListCellRenderer() {
		setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected, boolean cellHasFocus) {
		setComponentOrientation(list.getComponentOrientation());

		setFont(list.getFont());
		
		String str = String.valueOf(value);
		setText(String.valueOf(value));

		if(AisNalogUtility.getData().getConfigFix().getPromFixs().contains(str)) {
			setBackground(new Color(185,255,185));
		} else {
			setBackground(new Color(255,185,185));
		}
		
		setForeground(list.getForeground());

		setSelected(isSelected);
		setEnabled(list.isEnabled());

		return this;
	}
}
