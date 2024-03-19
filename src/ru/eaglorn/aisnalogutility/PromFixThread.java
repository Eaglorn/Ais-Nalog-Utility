package ru.eaglorn.aisnalogutility;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class PromFixThread extends Thread {

	public static int installMode = 0;

	public static void decompress7ZipEmbedded(File source, File destination) throws IOException, InterruptedException {
		ProcessBuilder pb = new ProcessBuilder().inheritIO().command("c://AisNalogUtility//7zip/7z.exe", "x",
				source.getAbsolutePath(), "-o" + destination.getAbsolutePath(), "-aoa");
		try {
			Process process = pb.start();
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
			AisNalogUtility.LOGGER.log(Level.WARNING, e.getMessage());
		}
	}

	@Override
	public void run() {	
		switch (installMode) {
			case 1: { // All
				for (Fix fix : Data.PROM_FIXS) {					
					if(!Data.CONFIG_INSTALLED.PROM_INSTALLED.contains(fix.NAME)) {
						Data.CONFIG_INSTALLED.PROM_INSTALLED.add(fix.NAME);
					}
					unpack(fix);
				}
				
				break;
			}
			case 2: { // Checked
				for (Fix fix : Data.PROM_FIXS) {					
					if (fix.CHECKED) {
						if(!Data.CONFIG_INSTALLED.PROM_INSTALLED.contains(fix.NAME)) {
							Data.CONFIG_INSTALLED.PROM_INSTALLED.add(fix.NAME);
						}
						unpack(fix);
					}
				}
				
				break;
			}
			case 3: { // Unchecked
				for (Fix fix : Data.PROM_FIXS) {					
					if (!fix.CHECKED) {
						if(!Data.CONFIG_INSTALLED.PROM_INSTALLED.contains(fix.NAME)) {
							Data.CONFIG_INSTALLED.PROM_INSTALLED.add(fix.NAME);
						}
						unpack(fix);
					}
				}
				
				break;
			}
			
			default: { // UnInstalled
				for (Fix fix : Data.PROM_FIXS) {
					if(!Data.CONFIG_INSTALLED.PROM_INSTALLED.contains(fix.NAME)) {
						unpack(fix);
						Data.CONFIG_INSTALLED.PROM_INSTALLED.add(fix.NAME);
					}
				}
			}
		}

		try {
			LoadingThread.LOAD_PROCESS_TEXT = "Статус выполнения: индексация распакованных фиксов.";
			String[] commands = { "CommonComponents.Catalog.IndexationUtility.exe" };
			AisNalogUtility.processBuilderStart(AisNalogUtility.APP_PROM_PATH + "Client\\", commands);
		} catch (InterruptedException e) {
			e.printStackTrace();
			AisNalogUtility.LOGGER.log(Level.WARNING, e.getMessage());
		}
	}
	
	public void unpack(Fix fix) {
		String pathFix = Data.CONFIG_APP.NET_PATH + "\\promfix\\" + fix.NAME;
		try {
			LoadingThread.LOAD_PROCESS_TEXT = "Статус выполнения: распаковка  фикса " + fix.NAME;
			decompress7ZipEmbedded(new File(pathFix), new File(AisNalogUtility.APP_PROM_PATH));
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			AisNalogUtility.LOGGER.log(Level.WARNING, e.getMessage());
		} 
	}
}
