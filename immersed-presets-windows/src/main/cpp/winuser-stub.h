#include <winapifamily.h>

#ifndef _WINUSER_
#define _WINUSER_

#pragma once

typedef BOOL (CALLBACK* WNDENUMPROC)(HWND, LPARAM);

WINUSERAPI
BOOL
WINAPI
EnumWindows(
    _In_ WNDENUMPROC lpEnumFunc,
    _In_ LPARAM lParam);

WINUSERAPI
int
WINAPI
GetWindowTextLengthW(
    _In_ HWND hWnd);

WINUSERAPI
int
WINAPI
GetWindowTextW(
    _In_ HWND hWnd,
    _Out_writes_(nMaxCount) LPWSTR lpString,
    _In_ int nMaxCount);

#endif /* !NOUSER */
