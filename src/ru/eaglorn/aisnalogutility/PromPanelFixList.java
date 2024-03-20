package ru.eaglorn.aisnalogutility;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import lombok.Getter;

public class PromPanelFixList {
	
	private JScrollPane scrollPane = new JScrollPane();
	private @Getter JPanel panel = new JPanel(new BorderLayout());

	public PromPanelFixList() {
		scrollPane.setViewportView(getPromFixList());
		
		scrollPane.getViewport().setViewPosition(new Point(0,99999));

		panel.add(scrollPane);
		
		panel.setMinimumSize(new Dimension(265,0));
		
		AisNalogUtility.app.addWidth(265);
	}
	
	public static JList<String> getPromFixList() {
		DefaultListModel<String> modelList = new DefaultListModel<>();

		FixListSelectionDocument listSelectionDocument = new FixListSelectionDocument();
		File dir = new File(Data.CONFIG_APP.NET_PATH + "\\promfix");
		File[] arrFiles = dir.listFiles();
		
		Comparator<File> comp = new Comparator<File>() {
			@Override
			public int compare(File f1, File f2) {
				String name1 = f1.getName();
				Integer numb1 = Integer.valueOf(name1.substring(name1.indexOf("№") + 1, name1.lastIndexOf(".")));
				
				String name2 = f2.getName();
				Integer numb2 = Integer.valueOf(name2.substring(name2.indexOf("№") + 1, name2.lastIndexOf(".")));
				
				return numb1.compareTo(numb2);
			}
		};
		
		Arrays.sort(arrFiles, comp);
		
		
		List<File> lst = Arrays.asList(arrFiles);

		int i = 1;
		for (File file : lst) {
			modelList.addElement(file.getName());
			Data.PROM_FIXS.add(new Fix(i, file.getName()));
			i++;
			PROM_FIX_HAVE++;
		}

		JList<String> list = new JList<>(modelList);
		list.setCellRenderer(new PromFixCheckboxListCellRenderer<String>());
		list.addListSelectionListener(listSelectionDocument);
		list.setSelectionModel(new DefaultListSelectionModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void setSelectionInterval(int index0, int index1) {
				if (super.isSelectedIndex(index0)) {
					if (index0 >= 0)
						Data.PROM_FIXS.get(index0).CHECKED = false;
					super.removeSelectionInterval(index0, index1);
				} else {
					if (index0 >= 0)
						Data.PROM_FIXS.get(index0).CHECKED = true;
					super.addSelectionInterval(index0, index1);
				}
			}
		});
		list.setLayoutOrientation(JList.VERTICAL);
	    
		return list;
	}
}
