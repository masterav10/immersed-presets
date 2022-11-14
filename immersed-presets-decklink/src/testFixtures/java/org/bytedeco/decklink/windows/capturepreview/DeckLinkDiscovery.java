package org.bytedeco.decklink.windows.capturepreview;

import static org.bytedeco.decklink.windows.ComSupport.*;
import static org.bytedeco.global.windef.*;

import java.util.function.Consumer;

import org.bytedeco.decklink.IDeckLink;
import org.bytedeco.decklink.IDeckLinkDeviceNotificationCallback;
import org.bytedeco.decklink.IDeckLinkDiscovery;

/**
 * This class will automatically listen for decklink devices and broadcast them.
 * 
 * @author Dan Avila
 *
 */
public class DeckLinkDiscovery extends IDeckLinkDeviceNotificationCallback
{
    private final IDeckLinkDiscovery m_deckLinkDiscovery;
    private Consumer<IDeckLink> m_deckLinkArrivedCallback;
    private Consumer<IDeckLink> m_deckLinkRemovedCallback;

    public DeckLinkDiscovery()
    {
        this.m_deckLinkDiscovery = create(IDeckLinkDiscovery.class);
    }

    public void onDeviceArrival(Consumer<IDeckLink> callback)
    {
        this.m_deckLinkArrivedCallback = callback;
    }

    public void onDeviceRemoval(Consumer<IDeckLink> callback)
    {
        this.m_deckLinkRemovedCallback = callback;
    }

    public boolean enable()
    {
        long result = E_FAIL;

        // Install device arrival notifications
        if (m_deckLinkDiscovery != null && !m_deckLinkDiscovery.isNull())
            result = m_deckLinkDiscovery.InstallDeviceNotifications(this);

        return result == S_OK;
    }

    @Override
    public int DeckLinkDeviceArrived(IDeckLink deckLinkDevice)
    {
        if (m_deckLinkArrivedCallback != null)
        {
            m_deckLinkArrivedCallback.accept(deckLinkDevice);
        }

        return (int) S_OK;
    }

    @Override
    public int DeckLinkDeviceRemoved(IDeckLink deckLinkDevice)
    {
        if (m_deckLinkRemovedCallback != null)
        {
            m_deckLinkRemovedCallback.accept(deckLinkDevice);
        }

        return (int) S_OK;
    }

    public void disable()
    {
        // Uninstall device arrival notifications
        if (m_deckLinkDiscovery != null && !m_deckLinkDiscovery.isNull())
            m_deckLinkDiscovery.UninstallDeviceNotifications();
    }
}
