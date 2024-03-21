package ru.eaglorn.aisnalogutility;

import java.io.File;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PromFixThread extends Thread {
	private int installMode = 0;

	public PromFixThread(int installMode) {
		this.installMode = installMode;
	}

	public static void decompress7ZipEmbedded(File source, File destination) throws IOException, InterruptedException {
		ProcessBuilder pb = new ProcessBuilder().inheritIO().command("c://AisNalogUtility//7zip/7z.exe", "x",
				source.getAbsolutePath(), "-o" + destination.getAbsolutePath(), "-aoa");
		try {
			Process process = pb.start();
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	@Override
	public void run() {
		App app = AisNalogUtility.getApp();
		Data data = AisNalogUtility.getData();

		switch (installMode) {
		case 1: { // All
			for (Fix fix : data.getPromFixs()) {
				if (!data.getConfigFix().getPromFixs().contains(fix.getName())) {
					data.getConfigFix().getPromFixs().add(fix.getName());
				}
				unpack(fix);
			}

			break;
		}
		case 2: { // Checked
			for (Fix fix : data.getPromFixs()) {
				if (fix.isChecked()) {
					if (!data.getConfigFix().getPromFixs().contains(fix.getName())) {
						data.getConfigFix().getPromFixs().add(fix.getName());
					}
					unpack(fix);
				}
			}

			break;
		}
		case 3: { // Unchecked
			for (Fix fix : data.getPromFixs()) {
				if (!fix.isChecked()) {
					if (!data.getConfigFix().getPromFixs().contains(fix.getName())) {
						data.getConfigFix().getPromFixs().add(fix.getName());
					}
					unpack(fix);
				}
			}

			break;
		}

		default: { // UnInstalled
			for (Fix fix : data.getPromFixs()) {
				if (!data.getConfigFix().getPromFixs().contains(fix.getName())) {
					unpack(fix);
					data.getConfigFix().getPromFixs().add(fix.getName());
				}
			}
		}
		}

		try {
			app.getLoadingThread().setProcessText("Статус выполнения: индексация распакованных фиксов.");
			String[] commands = { app.getPromPath() + "Client\\CommonComponents.Catalog.IndexationUtility.exe" };
			app.processBuilderStart(commands, true);
		} catch (InterruptedException e) {
			log.error(e.getMessage());
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
			log.error(e.getMessage());
			Thread.currentThread().interrupt();
		}
	}
}
