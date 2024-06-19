package ru.eaglorn.aisnalogutility;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;

public class PromPanelFix {
	private @Getter JPanel panel = new JPanel(new BorderLayout());
	private JScrollPane scrollPane = new JScrollPane();
	private int width = 290;

	public PromPanelFix() {
		scrollPane.setViewportView(getPromFixList());
		scrollPane.getViewport().setViewPosition(new Point(0, 99999));
		panel.add(scrollPane);
		panel.setMinimumSize(new Dimension(width, 0));
		AisNalogUtility.getApp().addWidth(width);
	}
	
	private Integer getNumberNameFile(File file) {
		String name = file.getName();
		int lastNumber = name.lastIndexOf('.');
		int firstNumber = lastNumber - 1;
		boolean whileRunning = true;
		while( whileRunning) {
			if(firstNumber >= 0) {
				if(StringUtils.isNumeric(name.substring(firstNumber, lastNumber))) {
					firstNumber--;
				} else {
					firstNumber++;
					whileRunning = false;
				}
			} else {
				firstNumber = 0;
				whileRunning = false;
			}
		}
		return Integer.valueOf(name.substring(firstNumber, lastNumber));
	}

	private JList<String> getPromFixList() {
		Data data = AisNalogUtility.getData();
		DefaultListModel<String> modelList = new DefaultListModel<>();
		FixListSelectionDocument listSelectionDocument = new FixListSelectionDocument();
		File dir = new File(data.getConfigApp().getNetPath() + "\\promfix");
		File[] arrFiles = dir.listFiles();
		List<File> lst = new ArrayList<File>();
		for(File file : arrFiles) {
			String name = file.getName();
			int lastNumber = name.lastIndexOf('.');
			if(lastNumber > 0 && lastNumber != name.length() - 1) {
				String type = name.substring(lastNumber + 1);
				boolean isArchive = false;
				for (String str : AisNalogUtility.getData().getConfigApp().getArchiveTypes()) {
					if(type.equals(str)) isArchive = true;
				}
				if(isArchive) {
					if(StringUtils.isNumeric(String.valueOf(name.charAt(lastNumber - 1)))) {
						lst.add(file);
					}
				}
			}
		}
		Comparator<File> comp = new Comparator<File>() {
			@Override
			public int compare(File f1, File f2) {
				return getNumberNameFile(f1).compareTo(getNumberNameFile(f2));
			}
		};
		lst.sort(comp);
		int i = 1;
		for (File file : lst) {
			modelList.addElement(file.getName());
			data.getPromFixs().add(new Fix(i, file.getName()));
			i++;
			AisNalogUtility.getApp().setPromFixHave(AisNalogUtility.getApp().getPromFixHave() + 1);
		}
		JList<String> list = new JList<>(modelList);
		list.setCellRenderer(new PromFixCheckboxListCellRenderer<>());
		list.addListSelectionListener(listSelectionDocument);
		list.setSelectionModel(new DefaultListSelectionModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void setSelectionInterval(int index0, int index1) {
				if (super.isSelectedIndex(index0)) {
					if (index0 >= 0)
						data.getPromFixs().get(index0).setChecked(false);
					super.removeSelectionInterval(index0, index1);
				} else {
					if (index0 >= 0)
						data.getPromFixs().get(index0).setChecked(true);
					super.addSelectionInterval(index0, index1);
				}
			}
		});
		list.setLayoutOrientation(JList.VERTICAL);
		return list;
	}
}
