package ru.eaglorn.aisnalogutility;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PromFixThread extends Thread {
	private int installMode = 0;

	public static void decompress7ZipEmbedded(File source, File destination) throws IOException, InterruptedException {
		ProcessBuilder pb = new ProcessBuilder().inheritIO().command("c://AisNalogUtility//7zip/7z.exe", "x",
				source.getAbsolutePath(), "-o" + destination.getAbsolutePath(), "-aoa");
		try {
			pb.start().waitFor();
		} catch (IOException e) {
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			log.error(stack.toString());
		}
	}

	public PromFixThread(int installMode) {
		this.installMode = installMode;
	}

	@Override
	public void run() {
		App app = AisNalogUtility.getApp();
		Data data = AisNalogUtility.getData();
		ConfigFix configFix = data.getConfigFix();

		try {
			String path = "";
			if (app.isWinArch()) {
				path = "\"c:\\AisNalogUtility\\scripts\\killprom64.bat\"";
			} else {
				path = "\"c:\\AisNalogUtility\\scripts\\killprom32.bat\"";
			}
			String[] commands1 = { path };
			app.processBuilderStart(commands1, false);
		} catch (InterruptedException e) {
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			log.error(stack.toString());
			Thread.currentThread().interrupt();
		}
		
		List<Fix> promFixs = data.getPromFixs();

		switch (installMode) {
			case 1: { // All
				for (Fix fix : promFixs) {
					if (!configFix.getPromFixs().contains(fix.getName())) {
						configFix.getPromFixs().add(fix.getName());
					}
					unpack(fix);
				}
				break;
			}
			case 2: { // Checked
				for (Fix fix : promFixs) {
					if (fix.isChecked()) {
						if (!configFix.getPromFixs().contains(fix.getName())) {
							configFix.getPromFixs().add(fix.getName());
						}
						unpack(fix);
					}
				}
				break;
			}
			case 3: { // Unchecked
				for (Fix fix : promFixs) {
					if (!fix.isChecked()) {
						if (!configFix.getPromFixs().contains(fix.getName())) {
							configFix.getPromFixs().add(fix.getName());
						}
						unpack(fix);
					}
				}
				break;
			}
			default: { // UnInstalled
				for (Fix fix : promFixs) {
					if (!configFix.getPromFixs().contains(fix.getName())) {
						unpack(fix);
						configFix.getPromFixs().add(fix.getName());
					}
				}
			}
		}
		try {
			app.getLoadingThread().setProcessText("Статус выполнения: индексация распакованных фиксов.");
			String[] commands2 = { app.getPromPath() + "Client\\CommonComponents.Catalog.IndexationUtility.exe" };
			app.processBuilderStart(commands2, true);
		} catch (InterruptedException e) {
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			log.error(stack.toString());
			Thread.currentThread().interrupt();
		}
	}

	public void unpack(Fix fix) {
		App app = AisNalogUtility.getApp();
		try {
			app.getLoadingThread().setProcessText("Статус выполнения: распаковка  фикса " + fix.getName());
			decompress7ZipEmbedded(new File(AisNalogUtility.getData().getConfigApp().getNetPath() + "\\promfix\\" + fix.getName()), new File(app.getPromPath()));
		} catch (IOException | InterruptedException e) {
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			log.error(stack.toString());
			Thread.currentThread().interrupt();
		}
	}
}
