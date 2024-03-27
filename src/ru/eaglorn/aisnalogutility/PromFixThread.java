package ru.eaglorn.aisnalogutility;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

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
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			log.error(stack.toString());
		}
	}

	@Override
	public void run() {
		App app = AisNalogUtility.getApp();
		Data data = AisNalogUtility.getData();

		try {
			String[] commands1 = { "wmic", "process", "where", "name=" + "\"" + app.getProcessName() + "\"", "and",
					"executablePath=" + "\"" + app.getPromPath() + "Client\\" + app.getProcessName() + "\"", "call",
					"terminate" };
			app.processBuilderStart(commands1, false);
		} catch (InterruptedException e) {
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			log.error(stack.toString());
			Thread.currentThread().interrupt();
		}

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
			String[] commands2 = { app.getPromPath() + "Client\\CommonComponents.Catalog.IndexationUtility.exe" };
			app.processBuilderStart(commands2, true);
		} catch (InterruptedException e) {
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			log.error(stack.toString());
		}
	}

	public void unpack(Fix fix) {
		App app = AisNalogUtility.getApp();
		String pathFix = AisNalogUtility.getData().getConfigApp().getNetPath() + "\\promfix\\" + fix.getName();
		try {
			app.getLoadingThread().setProcessText("Статус выполнения: распаковка  фикса " + fix.getName());
			decompress7ZipEmbedded(new File(pathFix), new File(app.getPromPath()));
		} catch (IOException | InterruptedException e) {
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			log.error(stack.toString());
		}
	}
}
