package org.bytedeco.decklink.windows.capturepreview;

import java.util.Iterator;

import org.bytedeco.decklink.IDeckLink;

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
    @FXML
    private Canvas previewCanvas;

    @FXML
    private ComboBox<DeckLinkDevice> inputDeviceDropdown;

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
