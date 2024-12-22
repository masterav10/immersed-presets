package org.bytedeco.dng.presets;

import static org.bytedeco.dng.presets.Defines.*;

import java.util.List;

import org.bytedeco.javacpp.ClassProperties;
import org.bytedeco.javacpp.LoadEnabled;
import org.bytedeco.javacpp.annotation.NoException;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;
import org.bytedeco.systems.presets.windows;

//@formatter:off
@Properties(
        inherit = {
            windows.class
        },
        names = {"windows-x86_64"}, 
        target = "org.bytedeco.dng", 
        global = "org.bytedeco.global.dng",
        value = {
            @Platform(
                compiler = "cpp11",
                include = {
                  // LIBJPEG
                  //"transupp.h",
                  //"jversion.h",
                  //"jpegint.h",
                  //"jmorecfg.h",
                  //"jmemsys.h",
                  //"jerror.h",
                  //"jdct.h",
                  //"jconfig.h",
                  //"jpeglib.h",
                  //"jinclude.h",
                  //"cderror.h",
                  //"cdjpeg.h",
                  // DNG SDK
                  "dng_uncopyable.h",
                  "dng_jpeglib.h",
                  "dng_fast_module.h",
                  //"dng_deprecated_flags.h",
                  //"dng_flags.h",
                  "dng_types.h",
                  "dng_tag_values.h",
                  "dng_tag_types.h",
                  "dng_tag_codes.h",
                  //"dng_simd_type.h",
                  "dng_sdk_limits.h",
                  "dng_rational.h",
                  //"dng_pthread.h",
                  "dng_mutex.h",
                  "dng_ref_counted_block.h",
                  "dng_matrix.h",
                  "dng_orientation.h",
                  "dng_jpeg_memory_source.h",
                  "dng_globals.h",
                  "dng_errors.h",
                  "dng_exceptions.h",
                  "dng_safe_arithmetic.h",
                  "dng_classes.h",
                  "dng_xy_coord.h",
                  //"dng_update_meta.h",
                  "dng_temperature.h",
                  "dng_memory.h",
                  //"dng_utils.h",
                  "dng_string.h",
                  "dng_string_list.h",
                  "dng_point.h",
                  "dng_tone_curve.h",
                  "dng_rect.h",
                  "dng_tile_iterator.h",
                  "dng_mosaic_info.h",
                  "dng_lossless_jpeg.h",
                  "dng_local_string.h",
                  "dng_date_time.h",
                  "dng_iptc.h",
                  "dng_color_spec.h",
                  "dng_bottlenecks.h",
                  "dng_reference.h",
                  "dng_auto_ptr.h",
                  "dng_xmp_sdk.h",
                  "dng_xmp.h",
                  "dng_stream.h",
                  "dng_parse_utils.h",
                  "dng_opcodes.h",
                  "dng_opcode_list.h",
                  "dng_misc_opcodes.h",
                  "dng_memory_stream.h",
                  "dng_linearization_info.h",
                  "dng_jpeg_image.h",
                  "dng_host.h",
                  "dng_fingerprint.h",
                  "dng_hue_sat_map.h",
                  "dng_gain_map.h",
                  "dng_file_stream.h",
                  "dng_exif.h",
                  "dng_bad_pixels.h",
                  "dng_assertions.h",
                  "dng_resample.h",
                  "dng_pixel_buffer.h",
                  "dng_image.h",
                  "dng_simple_image.h",
                  "dng_camera_profile.h",
                  "dng_big_table.h",
                  "dng_area_task.h",
                  "dng_read_image.h",
                  "dng_image_writer.h",
                  "dng_filter_task.h",
                  "dng_abort_sniffer.h",
                  "dng_1d_table.h",
                  "dng_1d_function.h",
                  "dng_spline.h",
                  "dng_color_space.h",  // render has indirect dependence on color_spaces through classes
                  "dng_render.h",
                  "dng_negative.h",
                  "dng_shared.h",
                  "dng_lens_correction.h",
                  "dng_ifd.h",
                  "dng_preview.h",
                  "dng_info.h"
                },
                link = {
                    "dng_validate", "XMPCoreStaticRelease", "XMPFilesStaticRelease"
                },
                define = {
                    "qWinOS 1",
                    "qAndroid 0",
                    "qDNGUseLibJPEG 1",
                    qDNGValidateTarget,
                    "SHARED_PTR_NAMESPACE std",
                    "UNIQUE_PTR_NAMESPACE std"
                }
            )
        }
)
//@formatter:on
@NoException
public class dng implements InfoMapper, LoadEnabled
{
    @Override
    public void map(InfoMap infoMap)
    {
        //@formatter:off
        
        // https://github.com/bytedeco/javacpp-presets/issues/1548
        infoMap.put(new Info("basic/containers").cppTypes("dng_std_vector"));
        
        // data types
        infoMap.put(new Info("uint64")
               .cast().valueTypes("long").pointerTypes("LongPointer", "LongBuffer", "long[]"));
        
        infoMap.put(new Info("uint32")
               .cast().valueTypes("int").pointerTypes("IntPointer", "IntBuffer", "int[]"));
        
        infoMap.put(new Info("real64")
               .cast().valueTypes("double").pointerTypes("DoublePointer", "DoubleBuffer", "double[]"));  
        
        infoMap.put(new Info("qDNGXMPDocOps").define(!qDNGValidateTarget()));
        infoMap.put(new Info("qAndroid").define(false));
        
        infoMap.put(new Info("kMetadataSubset_CopyrightOnly", "kMetadataSubset_CopyrightAndContact",
                "kMetadataSubset_All", "kMetadataSubset_AllExceptLocationInfo", "kMetadataSubset_AllExceptCameraInfo",
                "kMetadataSubset_AllExceptCameraAndLocation", "kMetadataSubset_AllExceptCameraRawInfo",
                "kMetadataSubset_AllExceptCameraRawInfoAndLocation")
               .cppTypes("int").translate(false));
      
        infoMap.put(new Info("jpeg_std_error", "jpeg_CreateCompress", "jpeg_CreateDecompress", "jpeg_destroy_compress",
                "jpeg_destroy_decompress", "jpeg_stdio_dest", "jpeg_stdio_src", "jpeg_mem_dest", "jpeg_mem_src",
                "jpeg_set_defaults", "jpeg_set_colorspace", "jpeg_default_colorspace", "jpeg_set_quality",
                "jpeg_set_linear_quality", "jpeg_default_qtables", "jpeg_add_quant_table", "jpeg_quality_scaling",
                "jpeg_simple_progression", "jpeg_suppress_tables", "jpeg_alloc_quant_table", "jpeg_alloc_huff_table",
                "jpeg_start_compress", "jpeg_write_scanlines", "jpeg_finish_compress", "jpeg_calc_jpeg_dimensions",
                "jpeg_calc_", "jpeg_dimensions", "jpeg_write_raw_data", "jpeg_write_marker", "jpeg_write_m_header",
                "jpeg_write_m_byte", "jpeg_write_tables", "jpeg_read_header", "jpeg_start_decompress",
                "jpeg_read_scanlines", "jpeg_finish_decompress", "jpeg_read_raw_data", "jpeg_has_multiple_scans",
                "jpeg_start_output", "jpeg_finish_output", "jpeg_input_complete", "jpeg_new_colormap",
                "jpeg_consume_input", "jpeg_core_output_dimensions", "jpeg_calc_output_dimensions", "jpeg_save_markers",
                "jpeg_set_marker_processor", "jpeg_read_coefficients", "jpeg_write_coefficients",
                "jpeg_copy_critical_parameters", "jpeg_abort_compress", "jpeg_abort_decompress", "jpeg_abort",
                "jpeg_destroy", "jpeg_resync_to_restart")
               .cppTypes("String").translate(false));
        
        // abstract classes
        infoMap.put(new Info("dng_1d_function", "dng_negative")
               .purify(false).virtualize());
        
        // skip because jpeg stuff is complicated
        infoMap.put(new Info("CreateJpegMemorySource")
               .skip());
        
        // don't know why these fail
        infoMap.put(new Info("DirtyPixel", "ConstPixel", "OptimizeOrder", "FILE")
               .skip());
        
        // skip because link error
        infoMap.put(new Info("dng_function_gamma_encode")
               .skip());
        
        // don't need these
        infoMap.put(new Info("dng_image_table::ShareImage", "dng_jpeg_image::fJPEGData")
                .skip());
        
        constTemplate(infoMap, "std::shared_ptr<const dng_memory_block>",
                               "std::shared_ptr<const dng_masked_rgb_tables>",
                               "std::shared_ptr<const dng_gain_table_map>",
                               "std::shared_ptr<const dng_image>");
        
        // XXX: These fail for reasons I'm not sure
        infoMap.put(new Info("dng_stream::AsMemoryBlock") 
               .skip());
        
        // explicit dng_noise_profile (const dng_std_vector<dng_noise_function> &functions
        infoMap.put(new Info("dng_noise_profile::dng_noise_profile",
                             "dng_vignette_radial_params::dng_vignette_radial_params")
                .skip());
        
        // remappings
        infoMap.put(new Info("std::map<dng_fingerprint,dng_ref_counted_block>").pointerTypes("dng_big_table_dictionary_map").define());
        infoMap.put(new Info("std::map<dng_fingerprint,dng_big_table_index::IndexEntry>").pointerTypes("dng_big_table_index_map").define());
        infoMap.put(new Info("std::vector<dng_ifd*>").pointerTypes("dng_info_vector").define());
        
        infoMap.put(new Info("std::shared_ptr<dng_masked_rgb_table>").annotations("@SharedPtr").pointerTypes("dng_masked_rgb_table"));
        infoMap.put(new Info("std::vector<std::shared_ptr<dng_masked_rgb_table> >").pointerTypes("dng_masked_rgb_table_ptr_vector").define());
        
        // this might be cheating
        defineTemplate(infoMap, "AutoPtr<dng_memory_block>", 
                                "AutoPtr<dng_camera_profile>", 
                                "AutoPtr<dng_jpeg_image>", 
                                "AutoPtr<dng_image>",
                                "AutoPtr<dng_exif>",
                                "AutoPtr<dng_shared>",
                                "AutoArray<dng_jpeg_image_tile_ptr>",
                                "AutoPtr<dng_gain_table_map>",
                                "AutoPtr<dng_masked_rgb_tables>",
                                "AutoPtr<dng_bad_pixel_list>",
                                "AutoPtr<dng_gain_map>",
                                "AutoPtr<dng_image>",
                                "AutoPtr<dng_opcode>",
                                "AutoPtr<dng_preview>",
                                "AutoPtr<dng_image>",
                                "AutoArray<AutoPtr<dng_memory_block> >",
                                "AutoPtr<dng_1d_table>",
                                "dng_std_vector<dng_fingerprint>",
                                "dng_std_vector<dng_camera_profile_info>",
                                "dng_std_vector<dng_point_real64>",
                                "dng_std_vector<uint32>",
                                "dng_std_vector<uint64>",
                                "dng_std_vector<real64>");
        
        // workaround for uncopyable being read-only
        // error C2280: attempting to reference a deleted function
        readOnly(infoMap, "dng_info::fExif", "autoptr_dng_exif");
        readOnly(infoMap, "dng_depth_preview::fImage", "autoptr_dng_image");
        readOnly(infoMap, "dng_mask_preview::fImage", "autoptr_dng_image");
        readOnly(infoMap, "dng_raw_preview::fImage", "autoptr_dng_image");
        readOnly(infoMap, "dng_jpeg_image::fJPEGTables", "autoptr_dng_memory_block");   
        readOnly(infoMap, "dng_jpeg_preview::fCompressedData", "autoptr_dng_memory_block");   
        readOnly(infoMap, "dng_linearization_info::fBlackDeltaH", "autoptr_dng_memory_block");   
        readOnly(infoMap, "dng_linearization_info::fBlackDeltaV", "autoptr_dng_memory_block");   
        readOnly(infoMap, "dng_linearization_info::fLinearizationTable", "autoptr_dng_memory_block");   
        readOnly(infoMap, "dng_raw_preview::fOpcodeList2Data", "autoptr_dng_memory_block");   
        readOnly(infoMap, "dng_info::fShared", "autoptr_dng_shared");
        
        infoMap.put(new Info("dng_jpeg_image_tile_ptr")
                .cast().valueTypes("autoptr_dng_memory_block").pointerTypes("PointerPointer<autoptr_dng_memory_block>"));
        
        infoMap.put(new Info("jpeg_saved_marker_ptr")
               .cast().valueTypes("jpeg_marker_struct").pointerTypes("PointerPointer<jpeg_marker_struct>"));
        
        infoMap.put(new Info("jvirt_sarray_ptr")
               .cast().valueTypes("jvirt_sarray_control").pointerTypes("PointerPointer<jvirt_sarray_control>"));
        infoMap.put(new Info("jvirt_barray_ptr")
                .cast().valueTypes("jvirt_barray_control").pointerTypes("PointerPointer<jvirt_barray_control>"));        
        
        infoMap.put(new Info("hypot", "DNG_ALWAYS_INLINE", "MULUH", "MULSH", "CHECK_SAFE_UINT32",
                             "CHECK_SAFE_INT32", "__USE_BUILTIN_SMULL_OVERFLOW",
                             "SafeInt64MultByClang", "DNG_NO_RETURN")
               .cppTypes().annotations());
        
        //@formatter:on
    }

    /**
     * This is how you define template-based classes. They get generated into custom
     * types.
     * 
     * @param infoMap
     * @param types
     */
    private static void defineTemplate(InfoMap infoMap, String... types)
    {
        for (String type : types)
        {
            String pointerType = type.toLowerCase()
                                     .replaceAll("::", "_")
                                     .replaceAll("<", "_")
                                     .replaceAll(" ", "_")
                                     .replaceAll(">", "")
                                     .trim();

            infoMap.put(new Info(type).pointerTypes(pointerType)
                                      .define());

            if (type.contains("AutoPtr"))
            {
                // Reason we need to skip
                // error C2512: '<template_type>': no appropriate default constructor available
                infoMap.put(new Info(type + "::Alloc").skip());
            }
        }
    }

    /**
     * Creates a proper method for template types that include const in it.
     * 
     * @param infoMap
     * @param type
     */
    private static void constTemplate(InfoMap infoMap, String... types)
    {
        for (String type : types)
        {
            Info info = new Info(type);

            if (type.startsWith("std::shared_ptr"))
            {
                info.annotations("@SharedPtr");
            }

            String template = type.split("[<>]")[1];
            String variable = template.replace("const ", "");
            String valueType = String.format("@Cast({\"%s*\", \"%s\"}) %s", template, type, variable);
            info.valueTypes(valueType);
            
            infoMap.put(info);
        }
    }

    /**
     * Ensures that fields with read-only types are read-only in java. For instance
     * will deleted destructors.
     * 
     * <p>
     * https://github.com/bytedeco/javacpp/wiki/Mapping-Recipes#defining-wrappers-for-basic-c-containers
     * </p>
     * 
     * @param infoMap
     * @param classAndField
     * @param type
     */
    private static void readOnly(InfoMap infoMap, String classAndField, String type)
    {
        String[] definition = classAndField.split("::");

        final String field = definition[definition.length - 1];
        final String text = String.format("public native @MemberGetter @Const @ByRef %s %s();", type, field);

        infoMap.put(new Info(classAndField).javaText(text));
    }

    @Override
    public void init(ClassProperties properties)
    {
        copyFromBuild(properties, "platform.includepath", "dngsdk.src");
        copyFromBuild(properties, "platform.linkpath", "dngsdk.bin");
    }

    private static void copyFromBuild(ClassProperties properties, String javacppProperty, String buildPrefix)
    {
        final List<String> paths = properties.get(javacppProperty);

        System.getProperties()
              .forEach((k, v) ->
              {
                  final String key = k.toString();
                  final String value = v.toString();

                  if (key.startsWith(buildPrefix) && !paths.contains(value))
                  {
                      paths.add(value);
                  }
              });
    }
}
