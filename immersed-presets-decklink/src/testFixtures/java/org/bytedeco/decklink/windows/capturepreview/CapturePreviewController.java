package org.bytedeco.decklink.windows.capturepreview;

import static org.bytedeco.decklink.windows.ComSupport.*;
import static org.bytedeco.global.decklink.*;
import static org.bytedeco.global.windef.*;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bytedeco.decklink.IDeckLink;
import org.bytedeco.decklink.IDeckLinkProfileAttributes;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.SelectionModel;
import javafx.scene.layout.Region;

public class CapturePreviewController
{
    private static final Map<Integer, String> kInputConnections;

    static
    {
        kInputConnections = new LinkedHashMap<>();
        kInputConnections.put(bmdVideoConnectionSDI, "SDI");
        kInputConnections.put(bmdVideoConnectionHDMI, "HDMI");
        kInputConnections.put(bmdVideoConnectionOpticalSDI, "Optical SDI");
        kInputConnections.put(bmdVideoConnectionComponent, "Component");
        kInputConnections.put(bmdVideoConnectionComposite, "Composite");
        kInputConnections.put(bmdVideoConnectionSVideo, "S-Video");
    }

    @FXML
    private Canvas previewCanvas;

    @FXML
    private ComboBox<DeckLinkDevice> inputDeviceDropdown;

    @FXML
    private ComboBox<String> inputConnectionDropdown;

    @FXML
    public void initialize()
    {
        Region parent = (Region) previewCanvas.getParent();

        previewCanvas.widthProperty()
                     .bind(parent.widthProperty());
        previewCanvas.heightProperty()
                     .bind(parent.heightProperty());

        inputDeviceDropdown.setCellFactory(view ->
        {
            return new ListCell<DeckLinkDevice>()
            {
                @Override
                protected void updateItem(DeckLinkDevice item, boolean empty)
                {
                    super.updateItem(item, empty);
                    setText(item == null || empty ? "" : item.getDeviceName());
                }
            };
        });
        inputDeviceDropdown.setButtonCell(inputDeviceDropdown.getCellFactory()
                                                             .call(null));

        inputDeviceDropdown.getSelectionModel()
                           .selectedItemProperty()
                           .addListener((obs, ov, nv) -> onDeviceSelected(nv));
    }

    private void onDeviceSelected(DeckLinkDevice device)
    {
        IDeckLink deckLink = device.getDeckLinkInstance();
        IDeckLinkProfileAttributes deckLinkAttributes = find(deckLink, IDeckLinkProfileAttributes.class);

        if (deckLinkAttributes == null || deckLinkAttributes.isNull())
            return;

        long[] availableInputConnections = new long[1];

        if (deckLinkAttributes.GetInt(BMDDeckLinkVideoInputConnections, availableInputConnections) != S_OK)
            return;

        long[] currentInputConnection = new long[1];
        if (device.getDeckLinkConfiguration()
                  .GetInt(bmdDeckLinkConfigVideoInputConnection, currentInputConnection) != S_OK)
        {
            currentInputConnection[0] = bmdVideoConnectionUnspecified;
        }

        final SelectionModel<String> model = this.inputConnectionDropdown.getSelectionModel();
        final List<String> inputConnections = this.inputConnectionDropdown.getItems();
        inputConnections.clear();

        for (Entry<Integer, String> connection : kInputConnections.entrySet())
        {
            final int key = connection.getKey();
            final String value = connection.getValue();

            if ((availableInputConnections[0] & key) == 1)
            {
                inputConnections.add(value);

                if (currentInputConnection[0] == key)
                {
                    model.select(value);
                }
            }
        }
    }

    public void onDeviceAdded(IDeckLink device)
    {
        final DeckLinkDevice deckLinkDevice = new DeckLinkDevice(device);

        if (!deckLinkDevice.init())
        {
            return;
        }

        Platform.runLater(() ->
        {
            ObservableList<DeckLinkDevice> items = inputDeviceDropdown.getItems();
            items.add(deckLinkDevice);

            SelectionModel<DeckLinkDevice> model = inputDeviceDropdown.getSelectionModel();
            if (model.getSelectedIndex() < 0)
            {
                model.select(0);
            }
        });
    }

    public void onDeviceRemoved(IDeckLink device)
    {
        Platform.runLater(() ->
        {
            ObservableList<DeckLinkDevice> items = inputDeviceDropdown.getItems();

            Iterator<DeckLinkDevice> iter = items.iterator();

            while (iter.hasNext())
            {
                DeckLinkDevice deckLinkDevice = iter.next();

                if (device.equals(deckLinkDevice.getDeckLinkInstance()))
                {
                    iter.remove();

                    if (deckLinkDevice.isCapturing())
                    {
                        deckLinkDevice.stopCapture();
                    }

                    deckLinkDevice.Release();
                }
            }
        });
    }
}
