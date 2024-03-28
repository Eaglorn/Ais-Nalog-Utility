package ru.eaglorn.aisnalogutility;

import java.io.PrintWriter;
import java.io.StringWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PromAppThread extends Thread {
	@Override
	public void run() {
		App app = AisNalogUtility.getApp();
		Data data = AisNalogUtility.getData();
		try {
			LoadingThread thread = app.getLoadingThread();
			thread.setProcessText("Статус выполнения: загрузка установочных файлов АИС Налог-3 ПРОМ.");
			String[] commands1 = { "robocopy", "/XC", "/XO", "/NP", "/NS", "/NC", "/NFL", "/NDL", "/R:3", "/W:1", "/Z",
					data.getConfigApp().getNetPath() + "\\\\aisprom\\\\", "c:\\AisNalogUtility\\aisprom\\" };
			app.processBuilderStart(commands1, false);
			String path = "";
			if(app.isWinArch()) {
				path = "\"c:\\AisNalogUtility\\scripts\\killprom64.bat\"";
			} else {
				path = "\"c:\\AisNalogUtility\\scripts\\killprom32.bat\"";
			}
			String[] commands2 = { path };
			app.processBuilderStart(commands2, false);
			thread.setProcessText("Статус выполнения: установка АИС Налог-3 ПРОМ.");
			String[] commands3 = { "\"c:\\AisNalogUtility\\aisprom\\run-silentmode.cmd\"" };
			app.processBuilderStart(commands3, true);
		} catch (InterruptedException e) {
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			log.error(stack.toString());
			Thread.currentThread().interrupt();
		}
	}
}
