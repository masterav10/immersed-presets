package org.bytedeco.decklink.windows.capturepreview;

import static org.bytedeco.global.com.*;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CapturePreview extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    private DeckLinkDiscovery discovery;
    private CapturePreviewController controller;

    @Override
    public void init() throws Exception
    {
        this.controller = new CapturePreviewController();

        CoInitializeEx(null, COINIT_MULTITHREADED);

        discovery = new DeckLinkDiscovery();
        discovery.onDeviceArrival(this.controller::onDeviceAdded);
        discovery.onDeviceRemoval(this.controller::onDeviceRemoved);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        discovery.enable();
        
        final URL resource = CapturePreview.class.getResource("CapturePreview.fxml");
        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(resource);
        loader.setController(controller);

        Scene scene = new Scene(loader.load());

        primaryStage.setTitle("CapturePreview");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception
    {
        discovery.disable();
        discovery.Release();

        CoUninitialize();
    }

}
