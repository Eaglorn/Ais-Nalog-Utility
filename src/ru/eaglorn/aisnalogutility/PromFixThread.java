package ru.eaglorn.aisnalogutility;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PromFixThread extends Thread {
	private static final Logger LOGGER = LoggerFactory.getLogger(PromFixThread.class);

	public static int installMode = 0;

	public static void decompress7ZipEmbedded(File source, File destination) throws IOException, InterruptedException {
		ProcessBuilder pb = new ProcessBuilder().inheritIO().command("c://AisNalogUtility//7zip/7z.exe", "x",
				source.getAbsolutePath(), "-o" + destination.getAbsolutePath(), "-aoa");
		try {
			Process process = pb.start();
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
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
			String[] commands = { AisNalogUtility.APP_PROM_PATH + "Client\\CommonComponents.Catalog.IndexationUtility.exe" };
			AisNalogUtility.processBuilderStart(commands, true);
		} catch (InterruptedException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}
	}
	
	public void unpack(Fix fix) {
		String pathFix = Data.CONFIG_APP.NET_PATH + "\\promfix\\" + fix.NAME;
		try {
			LoadingThread.LOAD_PROCESS_TEXT = "Статус выполнения: распаковка  фикса " + fix.NAME;
			decompress7ZipEmbedded(new File(pathFix), new File(AisNalogUtility.APP_PROM_PATH));
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		} 
	}
}
