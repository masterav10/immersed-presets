/*
 ** The header provided a clean interface to access root objects from
 ** the Black Magic SDK. Normally, this process varies depending on the
 ** operating system.
*/

#pragma once
#include "DeckLinkAPI_h.h"

HRESULT GetDeckLinkVideoFrameAncillaryPackets(IDeckLinkVideoFrameAncillaryPackets** deckLinkVideoFrameAncillaryPackets);

HRESULT GetDeckLinkVideoConversion(IDeckLinkVideoConversion** deckLinkVideoConversion);

HRESULT GetDeckLinkDX9ScreenPreviewHelper(IDeckLinkDX9ScreenPreviewHelper** deckLinkDX9ScreenPreviewHelper);

HRESULT GetBMDStreamingDiscovery(IBMDStreamingDiscovery** bmdStreamingDiscovery);

HRESULT GetDeckLinkDiscovery(IDeckLinkDiscovery** deckLinkDiscovery);

HRESULT GetDeckLinkGLScreenPreviewHelper(IDeckLinkGLScreenPreviewHelper** deckLinkGLScreenPreviewHelper);

HRESULT GetBMDStreamingH264NALParser(IBMDStreamingH264NALParser** bmdStreamingH264NALParser);

HRESULT GetDeckLinkIterator(IDeckLinkIterator** deckLinkIterator);

HRESULT GetDeckLinkAPIInformation(IDeckLinkAPIInformation** deckLinkAPIInformation);
