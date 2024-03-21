package ru.eaglorn.aisnalogutility;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Setter;
import lombok.val;

public class PromFixThread extends Thread {
	private static @val Logger logger = LoggerFactory.getLogger(PromFixThread.class);

	public static void decompress7ZipEmbedded(File source, File destination) throws IOException, InterruptedException {
		ProcessBuilder pb = new ProcessBuilder().inheritIO().command("c://AisNalogUtility//7zip/7z.exe", "x", source.getAbsolutePath(), "-o" + destination.getAbsolutePath(), "-aoa");
		try {
			Process process = pb.start();
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	private @Setter int installMode = 0;

	@Override
	public void run() {
		App app = AisNalogUtility.getApp();
		Data data = AisNalogUtility.getData();
		
		switch (installMode) {
			case 1: { // All
				for (Fix fix : data.getPromFixs()) {					
					if(!data.getConfigFix().getPromFixs().contains(fix.getName())) {
						data.getConfigFix().getPromFixs().add(getName());
					}
					unpack(fix);
				}
				
				break;
			}
			case 2: { // Checked
				for (Fix fix : data.getPromFixs()) {					
					if (fix.isChecked()) {
						if(!data.getConfigFix().getPromFixs().contains(fix.getName())) {
							data.getConfigFix().getPromFixs().add(getName());
						}
						unpack(fix);
					}
				}
				
				break;
			}
			case 3: { // Unchecked
				for (Fix fix : data.getPromFixs()) {					
					if (!fix.isChecked()) {
						if(!data.getConfigFix().getPromFixs().contains(fix.getName())) {
							data.getConfigFix().getPromFixs().add(getName());
						}
						unpack(fix);
					}
				}
				
				break;
			}
			
			default: { // UnInstalled
				for (Fix fix : data.getPromFixs()) {
					if(!data.getConfigFix().getPromFixs().contains(fix.getName())) {
						unpack(fix);
						data.getConfigFix().getPromFixs().add(getName());
					}
				}
			}
		}

		try {
			app.getLoadingThread().setProcessText("Статус выполнения: индексация распакованных фиксов.");
			String[] commands = { app.getPromPath() + "Client\\CommonComponents.Catalog.IndexationUtility.exe" };
			app.processBuilderStart(commands, true);
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
			Thread.currentThread().interrupt();
		}
	}
	
	public void unpack(Fix fix) {
		App app = AisNalogUtility.getApp();
		
		String pathFix = AisNalogUtility.getData().getConfigApp().getNetPath() + "\\promfix\\" + fix.getName();
		try {
			app.getLoadingThread().setProcessText("Статус выполнения: распаковка  фикса " + fix.getName());
			decompress7ZipEmbedded(new File(pathFix), new File(app.getPromPath()));
		} catch (IOException | InterruptedException e) {
			logger.error(e.getMessage());
			Thread.currentThread().interrupt();
		} 
	}
}
