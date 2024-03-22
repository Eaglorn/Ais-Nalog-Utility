package ru.eaglorn.aisnalogutility;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PromAppThread extends Thread {
	private @Setter int installMode = 0;

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
			thread.setProcessText("Статус выполнения: установка АИС Налог-3 ПРОМ.");
			String[] commands2 = { "\"c:\\AisNalogUtility\\aisprom\\run-silentmode.cmd\"" };
			app.processBuilderStart(commands2, true);
		} catch (InterruptedException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			Thread.currentThread().interrupt();
		}
	}
}
