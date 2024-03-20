package ru.eaglorn.aisnalogutility;

import java.util.logging.Level;

public class PromAppThread extends Thread {

	public static int installMode = 0;

	@Override
	public void run() {
		try {
			LoadingThread.LOAD_PROCESS_TEXT = "Статус выполнения: загрузка установочных файлов АИС Налог-3 ПРОМ.";
			
			String[] commands1 = { "robocopy", "/XC", "/XO", "/NP", "/NS", "/NC", "/NFL", "/NDL", "/R:3", "/W:1", "/Z",
					Data.CONFIG_APP.NET_PATH + "\\\\aisprom\\\\",
					"c:\\AisNalogUtility\\aisprom\\"};
			AisNalogUtility.processBuilderStart(commands1, false);
			
			LoadingThread.LOAD_PROCESS_TEXT = "Статус выполнения: установка АИС Налог-3 ПРОМ.";
			
			String[] commands2 = { "\"c:\\AisNalogUtility\\aisprom\\run-silentmode.cmd\"" };
			AisNalogUtility.processBuilderStart(commands2, true);
		} catch (InterruptedException e) {
			e.printStackTrace();
			AisNalogUtility.LOGGER.log(Level.WARNING, e.getMessage());
		}
	}
}
