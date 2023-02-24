#include "com.h"

static thread_local int isInitialized = -1;

HRESULT InitializeCom()
{
	HRESULT result = S_OK;

	if (isInitialized < 0)
	{
		HRESULT result = CoInitializeEx(NULL, COINIT_MULTITHREADED);

		if (result == S_OK)
		{
			isInitialized = 0;
		}
	}

	return result;
}

void CloseCom()
{
	CoUninitialize();
}

HRESULT GetDeckLinkVideoFrameAncillaryPackets(IDeckLinkVideoFrameAncillaryPackets** deckLinkVideoFrameAncillaryPackets)
{
	InitializeCom();
	return CoCreateInstance(CLSID_CDeckLinkVideoFrameAncillaryPackets, NULL, CLSCTX_ALL, IID_IDeckLinkVideoFrameAncillaryPackets, (void**)deckLinkVideoFrameAncillaryPackets);
}

HRESULT GetDeckLinkVideoConversion(IDeckLinkVideoConversion** deckLinkVideoConversion)
{
	InitializeCom();
	return CoCreateInstance(CLSID_CDeckLinkVideoConversion, NULL, CLSCTX_ALL, IID_IDeckLinkVideoConversion, (void**)deckLinkVideoConversion);
}

HRESULT GetDeckLinkDX9ScreenPreviewHelper(IDeckLinkDX9ScreenPreviewHelper** deckLinkDX9ScreenPreviewHelper)
{
	InitializeCom();
	return CoCreateInstance(CLSID_CDeckLinkDX9ScreenPreviewHelper, NULL, CLSCTX_ALL, IID_IDeckLinkDX9ScreenPreviewHelper, (void**)deckLinkDX9ScreenPreviewHelper);
}

HRESULT GetBMDStreamingDiscovery(IBMDStreamingDiscovery** bmdStreamingDiscovery)
{
	InitializeCom();
	return CoCreateInstance(CLSID_CBMDStreamingDiscovery, NULL, CLSCTX_ALL, IID_IBMDStreamingDiscovery, (void**)bmdStreamingDiscovery);
}

HRESULT GetDeckLinkDiscovery(IDeckLinkDiscovery** deckLinkDiscovery)
{
	InitializeCom();
	return CoCreateInstance(CLSID_CDeckLinkDiscovery, NULL, CLSCTX_ALL, IID_IDeckLinkDiscovery, (void**)deckLinkDiscovery);
}

HRESULT GetDeckLinkGLScreenPreviewHelper(IDeckLinkGLScreenPreviewHelper** deckLinkGLScreenPreviewHelper)
{
	InitializeCom();
	return CoCreateInstance(CLSID_CDeckLinkGLScreenPreviewHelper, NULL, CLSCTX_ALL, IID_IDeckLinkGLScreenPreviewHelper, (void**)deckLinkGLScreenPreviewHelper);
}

HRESULT GetBMDStreamingH264NALParser(IBMDStreamingH264NALParser** bmdStreamingH264NALParser)
{
	InitializeCom();
	return CoCreateInstance(CLSID_CBMDStreamingH264NALParser, NULL, CLSCTX_ALL, IID_IBMDStreamingH264NALParser, (void**)bmdStreamingH264NALParser);
}

HRESULT GetDeckLinkIterator(IDeckLinkIterator** deckLinkIterator)
{
	InitializeCom();
	return CoCreateInstance(CLSID_CDeckLinkIterator, NULL, CLSCTX_ALL, IID_IDeckLinkIterator, (void**)deckLinkIterator);
}

HRESULT GetDeckLinkAPIInformation(IDeckLinkAPIInformation** deckLinkAPIInformation)
{
	InitializeCom();
	return CoCreateInstance(CLSID_CDeckLinkAPIInformation, NULL, CLSCTX_ALL, IID_IDeckLinkAPIInformation, (void**)deckLinkAPIInformation);
}

HRESULT GetDeckLinkWPFDX9ScreenPreviewHelper(IDeckLinkWPFDX9ScreenPreviewHelper** deckLinkWPFDX9ScreenPreviewHelper)
{
	InitializeCom();
	return CoCreateInstance(CLSID_CDeckLinkWPFDX9ScreenPreviewHelper, NULL, CLSCTX_ALL, IID_IDeckLinkWPFDX9ScreenPreviewHelper, (void**)deckLinkWPFDX9ScreenPreviewHelper);
}
