package org.bytedeco.javacpp.tools;

import static org.bytedeco.global.dng.*;

import org.bytedeco.dng.autoptr_dng_camera_profile;
import org.bytedeco.dng.autoptr_dng_image;
import org.bytedeco.dng.dng_camera_profile;
import org.bytedeco.dng.dng_exif;
import org.bytedeco.dng.dng_file_stream;
import org.bytedeco.dng.dng_host;
import org.bytedeco.dng.dng_image;
import org.bytedeco.dng.dng_image_writer;
import org.bytedeco.dng.dng_negative;
import org.bytedeco.dng.dng_orientation;
import org.bytedeco.dng.dng_pixel_buffer;
import org.bytedeco.dng.dng_rect;
import org.bytedeco.dng.dng_srational;
import org.bytedeco.dng.dng_urational;
import org.bytedeco.dng.dng_vector;
import org.bytedeco.javacpp.BytePointer;

/**
 * This 
 */
public class PgmToDngExample
{
    public static void main(String... args)
    {
        String outputFile = "C:\\Tools\\adobe\\pgm2dng-master\\Samples\\BMPCC-4K-StdA-D65.dng";
        String dcpFile = "C:\\Tools\\adobe\\pgm2dng-master\\Samples\\BMPCC-4K-StdA-D65.dcp";
        int[] wp = new int[]
        { 0, 0, 0 };
        double blackLevel = 0.0;
        int whiteLevel = 0;

        // items
        boolean isMonochrome = false;
        int width = 100;
        int height = 100;
        int bitsPerChannel = 16;
        int bayerType = 0;

        // SETTINGS: BAYER PATTERN
        int colorPlanes = 1;
        int colorChannels = isMonochrome ? 1 : 3;

        // SETTINGS: Whitebalance, Orientation "normal"
        dng_orientation orient = dng_orientation.Normal();

        // SETTINGS: Names
        String makeerStr = "Fastvideo";
        String cameraModelStr = "PGM to DNG";
        String profileName = cameraModelStr;
        String profileCopyrightStr = makeerStr;

        // Calculate bit limit
        int bitLimit = 0x01 << bitsPerChannel;

        BytePointer pgmData = new BytePointer(width * height * bitsPerChannel / 8);
        // unsigned short* imageData = (unsigned short*)pgmData.data() ;

        // Create DNG
        dng_host DNGHost = new dng_host();

        DNGHost.SetSaveDNGVersion(dngVersion_SaveDefault);
        DNGHost.SetSaveLinearDNG(false);

        dng_rect imageBounds = new dng_rect(height, width);
        dng_image image = DNGHost.Make_dng_image(imageBounds, colorPlanes, bitsPerChannel == 8 ? ttByte : ttShort);
        
        dng_pixel_buffer buffer = new dng_pixel_buffer();
        buffer.fArea(imageBounds);
        buffer.fPlane(0);
        buffer.fPlanes(1);
        buffer.fRowStep(buffer.fPlanes() * width);
        buffer.fColStep(buffer.fPlanes());
        buffer.fPlaneStep(1);
        buffer.fPixelType(bitsPerChannel == 8 ? ttByte : ttShort);
        buffer.fPixelSize(bitsPerChannel == 8 ? TagTypeSize(ttByte) : TagTypeSize(ttShort));
        buffer.fData(pgmData);

        image.Put(buffer);

        dng_negative negative = DNGHost.Make_dng_negative();

        negative.SetModelName(cameraModelStr);
        negative.SetLocalName(cameraModelStr);

        if (!isMonochrome)
        {
            negative.SetColorKeys(colorKeyRed, colorKeyGreen, colorKeyBlue);
            negative.SetBayerMosaic(bayerType);

            dng_vector cameraNeutral = new dng_vector(3);
            cameraNeutral.get(0)
                         .put(wp[0]);
            cameraNeutral.get(1)
                         .put(wp[1]);
            cameraNeutral.get(2)
                         .put(wp[2]);
            negative.SetCameraNeutral(cameraNeutral);

            // Add camera profile to negative
            autoptr_dng_camera_profile profile = new autoptr_dng_camera_profile(new dng_camera_profile());
            dng_file_stream profileStream = new dng_file_stream(dcpFile);

            if (profile.Get()
                       .ParseExtended(profileStream))
            {
                negative.AddProfile(profile);
            }
        }

        negative.SetColorChannels(colorChannels);

        negative.SetBlackLevel(blackLevel);
        negative.SetWhiteLevel(whiteLevel <= 0 ? (1 << bitsPerChannel) - 1 : whiteLevel);

        negative.SetDefaultScale(new dng_urational(1, 1), new dng_urational(1, 1));
        negative.SetBestQualityScale(new dng_urational(1, 1));
        negative.SetDefaultCropOrigin(0, 0);
        negative.SetDefaultCropSize(width, height);
        negative.SetBaseOrientation(orient);

        negative.SetBaselineExposure(0);
        negative.SetNoiseReductionApplied(new dng_urational(0, 1));
        negative.SetBaselineSharpness(1);

        // DNG EXIF
        dng_exif poExif = negative.GetExif();
        poExif.fMake()
              .Set_ASCII(makeerStr);
        poExif.fModel()
              .Set_ASCII(cameraModelStr);
        poExif.fMeteringMode(2);
        poExif.fExposureBiasValue(new dng_srational(0, 0));

        // Write DNG file
        negative.SetStage1Image(new autoptr_dng_image(image));
        negative.SynchronizeMetadata();
        negative.RebuildIPTC(true);

        dng_file_stream DNGStream = new dng_file_stream(outputFile, true, 0);
        dng_image_writer imgWriter = new dng_image_writer();

        imgWriter.WriteDNG(DNGHost, DNGStream, negative);
    }
}
