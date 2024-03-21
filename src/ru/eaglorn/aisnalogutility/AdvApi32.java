package ru.eaglorn.aisnalogutility;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.WinBase.PROCESS_INFORMATION;
import com.sun.jna.platform.win32.WinBase.STARTUPINFO;

import lombok.val;

public interface AdvApi32 extends Advapi32 {
	@val int CREATE_NEW_CONSOLE = 0x00000010;

	@val int CREATE_NO_WINDOW = 0x08000000;

	@val int CREATE_UNICODE_ENVIRONMENT = 0x00000400;
	@val int DETACHED_PROCESS = 0x00000008;

	AdvApi32 INSTANCE = Native.load("AdvApi32", AdvApi32.class);
	@val int LOGON_NETCREDENTIALS_ONLY = 0x00000002;
	@val int LOGON_WITH_PROFILE = 0x00000001;

	boolean CreateProcessWithLogonW(WString lpUsername, WString lpDomain, WString lpPassword, int dwLogonFlags,
			WString lpApplicationName, WString lpCommandLine, int dwCreationFlags, Pointer lpEnvironment,
			WString lpCurrentDirectory, STARTUPINFO lpStartupInfo, PROCESS_INFORMATION lpProcessInfo);
}
