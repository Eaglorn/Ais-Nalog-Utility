package ru.eaglorn.aisnalogutility;

import com.sun.jna.WString;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Elevator {
	public static void executeAsAdministrator(String command, String args) throws RuntimeException {
		Shell32X.SHELLEXECUTEINFO execInfo = new Shell32X.SHELLEXECUTEINFO();
		execInfo.lpFile = new WString(command);
		if (args != null) {
			execInfo.lpParameters = new WString(args);
		}
		execInfo.nShow = Shell32X.SW_SHOWDEFAULT;
		execInfo.fMask = Shell32X.SEE_MASK_NOCLOSEPROCESS;
		execInfo.lpVerb = new WString("runas");
		boolean result = Shell32X.INSTANCE.ShellExecuteEx(execInfo);
		if (!result) {
			int lastError = Kernel32.INSTANCE.GetLastError();
			String errorMessage = Kernel32Util.formatMessageFromLastErrorCode(lastError);
			log.error("Error performing elevation: {}: {} (apperror={})", lastError, errorMessage, execInfo.hInstApp);
		}
	}

	private Elevator() {
		throw new IllegalStateException("Utility class");
	}
}
