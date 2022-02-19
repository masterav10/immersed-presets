package org.bytedeco.decklink.windows.capturepreview;

import static org.bytedeco.decklink.windows.ComSupport.*;
import static org.bytedeco.global.decklink.*;
import static org.bytedeco.global.windef.*;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.bytedeco.decklink.IDeckLink;
import org.bytedeco.decklink.IDeckLinkConfiguration;
import org.bytedeco.decklink.IDeckLinkDisplayMode;
import org.bytedeco.decklink.IDeckLinkInput;
import org.bytedeco.decklink.IDeckLinkProfileAttributes;
import org.bytedeco.javacpp.CharPointer;
import org.bytedeco.javacpp.PointerPointer;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.scene.layout.Region;
import javafx.util.Callback;

public class CapturePreviewController
{
    private static final Map<Integer, String> kInputConnections;

    private static final class DisplayMode
    {
        private final int constant;
        private final String name;

        DisplayMode(IDeckLinkDisplayMode mode)
        {
            this.constant = mode.GetDisplayMode();

            try (PointerPointer<CharPointer> ptr = new PointerPointer<>(1L))
            {
                mode.GetName(ptr);

                this.name = ptr.get(CharPointer.class)
                               .getString();
            }
        }

        @Override
        public String toString()
        {
            return name;
        }
    }

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

    private static <T> void configure(ComboBox<T> combo, Consumer<T> onUpdate,
            Callback<ListView<T>, ListCell<T>> factory)
    {
        combo.setCellFactory(factory);
        combo.setButtonCell(combo.getCellFactory()
                                 .call(null));
        combo.getSelectionModel()
             .selectedItemProperty()
             .addListener((obs, ov, nv) -> onUpdate.accept(nv));
    }

    @FXML
    private Canvas previewCanvas;

    @FXML
    private ComboBox<DeckLinkDevice> inputDeviceDropdown;

    @FXML
    private ComboBox<Integer> inputConnectionDropdown;

    @FXML
    private ComboBox<DisplayMode> videoFormatDropdown;

    @FXML
    public void initialize()
    {
        Region parent = (Region) previewCanvas.getParent();

        previewCanvas.widthProperty()
                     .bind(parent.widthProperty());
        previewCanvas.heightProperty()
                     .bind(parent.heightProperty());

        configure(inputDeviceDropdown, this::onDeviceSelected, view ->
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

        configure(inputConnectionDropdown, this::onInputConnectionSelected, view ->
        {
            return new ListCell<Integer>()
            {
                @Override
                protected void updateItem(Integer item, boolean empty)
                {
                    super.updateItem(item, empty);
                    setText(item == null || empty ? "" : kInputConnections.get(item));
                }
            };
        });
    }

    private void onInputConnectionSelected(Integer connectionId)
    {
        SelectionModel<Integer> model = inputConnectionDropdown.getSelectionModel();

        if (model.getSelectedIndex() < 0)
            return;

        DeckLinkDevice device = inputDeviceDropdown.getSelectionModel()
                                                   .getSelectedItem();
        IDeckLinkConfiguration config = device.getDeckLinkConfiguration();

        int inputConnection = model.getSelectedItem();

        if (config.SetInt(bmdDeckLinkConfigVideoInputConnection, inputConnection) != S_OK)
            return;

        final List<DisplayMode> list = this.videoFormatDropdown.getItems();

        device.queryDisplayModes(deckLinkDisplayMode ->
        {
            final IDeckLinkInput input = device.getDeckLinkInput();
            final int mode = deckLinkDisplayMode.GetDisplayMode();
            final int[] supported = new int[1];

            if (input.DoesSupportVideoMode(inputConnection, mode, bmdFormatUnspecified, bmdNoVideoInputConversion,
                    bmdSupportedVideoModeDefault, null, supported) != S_OK)
            {
                return;
            }

            list.add(new DisplayMode(deckLinkDisplayMode));
        });

        if (!list.isEmpty())
        {
            this.videoFormatDropdown.getSelectionModel()
                                    .select(0);
        }
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

        final SelectionModel<Integer> model = this.inputConnectionDropdown.getSelectionModel();
        final List<Integer> inputConnections = this.inputConnectionDropdown.getItems();
        inputConnections.clear();

        for (Integer connectionId : kInputConnections.keySet())
        {
            if ((availableInputConnections[0] & connectionId) == 1)
            {
                inputConnections.add(connectionId);

                if (currentInputConnection[0] == connectionId)
                {
                    model.select(connectionId);
                }
            }
        }

        if (!inputConnections.isEmpty() && model.getSelectedIndex() < 0)
        {
            model.select(0);
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
