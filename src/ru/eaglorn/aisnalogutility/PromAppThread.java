package ru.eaglorn.aisnalogutility;

import java.util.logging.Level;

public class PromAppThread extends Thread {

	public static int installMode = 0;

	@Override
	public void run() {
		try {
			LoadingThread.LOAD_PROCESS_TEXT = "Статус выполнения: установка АИС Налог-3 ПРОМ.";
			
			String[] commands1 = { " /x ", " /s", " /xc ", " /xo ",
					"\""+ Data.CONFIG_APP.NET_PATH + "//aisprom//" +"\"",
					"\"" + "c://AisNalogUtility//aisprom//" +"\"" };
			AisNalogUtility.processBuilderStart("robocopy ", commands1);
			
			String[] commands2 = { "run-silentmode.cmd" };
			AisNalogUtility.processBuilderStart("c://AisNalogUtility//aisprom//", commands2);
		} catch (InterruptedException e) {
			e.printStackTrace();
			AisNalogUtility.LOGGER.log(Level.WARNING, e.getMessage());
		}
	}
}
