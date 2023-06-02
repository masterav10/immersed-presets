package org.bytedeco.cuda.cub.presets;

import org.bytedeco.cuda.presets.cudart;
import org.bytedeco.javacpp.annotation.NoException;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

//@formatter:off
@Properties(
        inherit = {cudart.class}, 
        names = {"windows-x86_64"}, 
        target = "org.bytedeco.cuda.cub", 
        global = "org.bytedeco.cuda.global.cub",
        value = {
            @Platform(
                include = {
                    "<cub/device/device_adjacent_difference.cuh>",
                    "<cub/device/device_histogram.cuh>",
                    "<cub/device/device_merge_sort.cuh>",
                    "<cub/device/device_partition.cuh>",
                    "<cub/device/device_radix_sort.cuh>",
                    "<cub/device/device_reduce.cuh>",
                    "<cub/device/device_run_length_encode.cuh>",
                    "<cub/device/device_scan.cuh>",
                    "<cub/device/device_segmented_radix_sort.cuh>",
                    "<cub/device/device_segmented_reduce.cuh>",
                    "<cub/device/device_segmented_sort.cuh>",
                    "<cub/device/device_select.cuh>",
                    "<cub/device/device_spmv.cuh>"
                }
            )
        }
)
@NoException
@SuppressWarnings({"java:S100", "java:S101", "java:S125", "java:S1192"})
public class cub implements InfoMapper
{
    @Override
    public void map(InfoMap infoMap)
    {   
        infoMap.put(new Info(".*template.*", "CUB_DETAIL_RUNTIME_DEBUG_SYNC_IS_NOT_SUPPORTED",
                             "CUB_DETAIL_RUNTIME_DEBUG_SYNC_IS_NOT_SUPPORTED", "").skip());
        infoMap.put(new Info("CUB_NAMESPACE_BEGIN").cppText("#define CUB_NAMESPACE_BEGIN namespace cub {").cppTypes());
        infoMap.put(new Info("CUB_NAMESPACE_END").cppText("#define CUB_NAMESPACE_END }").cppTypes());
        
        infoMap.put(new Info("CUB_RUNTIME_FUNCTION", 
                             "CUB_DETAIL_RUNTIME_DEBUG_SYNC_IS_NOT_SUPPORTED",
                             "CUB_DETAIL_RUNTIME_DEBUG_SYNC_USAGE_LOG")
                .cppTypes().annotations());
        infoMap.put(new Info("DoubleBuffer").skip());
        
        infoMap.put(new Info("std::size_t").cast().valueTypes("long").pointerTypes("SizeTPointer"));
        
        device_adjacent_difference(infoMap);
        device_histogram(infoMap);
        device_merge_sort(infoMap);
        device_partition(infoMap);
        device_radix_sort(infoMap);
        device_reduce(infoMap);
        device_run_length_encode(infoMap);
        device_scan(infoMap);
        device_segmented_radix_sort(infoMap);
        device_segmented_reduce(infoMap);
        device_segmented_sort(infoMap);
        device_select(infoMap);
        device_spmv(infoMap);
    }
    
    private static void device_adjacent_difference(InfoMap infoMap)
    {
        // cub::DeviceAdjacentDifference::SubtractLeftCopy<InputIteratorT,OutputIteratorT,DifferenceOpT,NumItemsT>
        // infoMap.put(new Info("cub::DeviceAdjacentDifference::SubtractLeftCopy<float*,float*,DifferenceOpT,NumItemsT>").javaNames("SubtractLeftCopy"));
        // infoMap.put(new Info("cub::DeviceAdjacentDifference::SubtractLeftCopy<int*,int*,DifferenceOpT,NumItemsT>").javaNames("SubtractLeftCopy"));

        // cub::DeviceAdjacentDifference::SubtractLeft<RandomAccessIteratorT,DifferenceOpT,NumItemsT>

        // cub::DeviceAdjacentDifference::SubtractRightCopy<InputIteratorT,OutputIteratorT,DifferenceOpT,NumItemsT>
        // infoMap.put(new Info("cub::DeviceAdjacentDifference::SubtractRightCopy<float*,float*,DifferenceOpT,NumItemsT>").javaNames("SubtractRightCopy"));
        // infoMap.put(new Info("cub::DeviceAdjacentDifference::SubtractRightCopy<int*,int*,DifferenceOpT,NumItemsT>").javaNames("SubtractRightCopy"));

        // cub::DeviceAdjacentDifference::SubtractRight<RandomAccessIteratorT,DifferenceOpT,NumItemsT>
    }

    private static void device_histogram(InfoMap infoMap)
    {
        // cub::DeviceHistogram::HistogramEven<SampleIteratorT,CounterT,LevelT,OffsetT>
        infoMap.put(new Info("cub::DeviceHistogram::HistogramEven<float*,unsigned int,float,int>").javaNames("HistogramEven"));
        infoMap.put(new Info("cub::DeviceHistogram::HistogramEven<int*,unsigned int,int,int>").javaNames("HistogramEven"));

        // cub::DeviceHistogram::MultiHistogramEven<NUM_CHANNELS,NUM_ACTIVE_CHANNELS,SampleIteratorT,CounterT,LevelT,OffsetT>
        infoMap.put(new Info("cub::DeviceHistogram::MultiHistogramEven<1,1,float*,unsigned int,float,int>").javaNames("MultiHistogramEven1Channel"));
        infoMap.put(new Info("cub::DeviceHistogram::MultiHistogramEven<2,2,float*,unsigned int,float,int>").javaNames("MultiHistogramEven2Channel"));
        infoMap.put(new Info("cub::DeviceHistogram::MultiHistogramEven<3,3,float*,unsigned int,float,int>").javaNames("MultiHistogramEven3Channel"));
        infoMap.put(new Info("cub::DeviceHistogram::MultiHistogramEven<4,4,float*,unsigned int,float,int>").javaNames("MultiHistogramEven4Channel"));
        infoMap.put(new Info("cub::DeviceHistogram::MultiHistogramEven<1,1,int*,unsigned int,int,int>").javaNames("MultiHistogramEven1Channel"));
        infoMap.put(new Info("cub::DeviceHistogram::MultiHistogramEven<2,2,int*,unsigned int,int,int>").javaNames("MultiHistogramEven2Channel"));
        infoMap.put(new Info("cub::DeviceHistogram::MultiHistogramEven<3,3,int*,unsigned int,int,int>").javaNames("MultiHistogramEven3Channel"));
        infoMap.put(new Info("cub::DeviceHistogram::MultiHistogramEven<4,4,int*,unsigned int,int,int>").javaNames("MultiHistogramEven4Channel"));

        // cub::DeviceHistogram::HistogramRange<SampleIteratorT,CounterT,LevelT,OffsetT>
        infoMap.put(new Info("cub::DeviceHistogram::HistogramRange<float*,unsigned int,float,int>").javaNames("HistogramRange"));
        infoMap.put(new Info("cub::DeviceHistogram::HistogramRange<int*,unsigned int,int,int>").javaNames("HistogramRange"));

        // cub::DeviceHistogram::MultiHistogramRange<NUM_CHANNELS,NUM_ACTIVE_CHANNELS,SampleIteratorT,CounterT,LevelT,OffsetT>
        // infoMap.put(new Info("cub::DeviceHistogram::MultiHistogramRange<1,1,float*,unsigned int,float,int>").javaNames("MultiHistogramRange1Channel"));
        // infoMap.put(new Info("cub::DeviceHistogram::MultiHistogramRange<2,2,float*,unsigned int,float,int>").javaNames("MultiHistogramRange2Channel"));
        // infoMap.put(new Info("cub::DeviceHistogram::MultiHistogramRange<3,3,float*,unsigned int,float,int>").javaNames("MultiHistogramRange3Channel"));
        // infoMap.put(new Info("cub::DeviceHistogram::MultiHistogramRange<4,4,float*,unsigned int,float,int>").javaNames("MultiHistogramRange4Channel"));
        // infoMap.put(new Info("cub::DeviceHistogram::MultiHistogramRange<1,1,int*,unsigned int,int,int>").javaNames("MultiHistogramRange1Channel"));
        // infoMap.put(new Info("cub::DeviceHistogram::MultiHistogramRange<2,2,int*,unsigned int,int,int>").javaNames("MultiHistogramRange2Channel"));
        // infoMap.put(new Info("cub::DeviceHistogram::MultiHistogramRange<3,3,int*,unsigned int,int,int>").javaNames("MultiHistogramRange3Channel"));
        // infoMap.put(new Info("cub::DeviceHistogram::MultiHistogramRange<4,4,int*,unsigned int,int,int>").javaNames("MultiHistogramRange4Channel"));
    }

    private static void device_merge_sort(InfoMap infoMap)
    {
        // cub::DeviceMergeSort::SortPairs<KeyIteratorT,ValueIteratorT,OffsetT,CompareOpT>
        // infoMap.put(new Info("cub::DeviceMergeSort::SortPairs<KeyIteratorT,ValueIteratorT,int,CompareOpT>").javaNames("SortPairs"));

        // cub::DeviceMergeSort::SortPairsCopy<KeyInputIteratorT,ValueInputIteratorT,KeyIteratorT,ValueIteratorT,OffsetT,CompareOpT>
        // infoMap.put(new Info("cub::DeviceMergeSort::SortPairsCopy<KeyInputIteratorT,ValueInputIteratorT,KeyIteratorT,ValueIteratorT,int,CompareOpT>").javaNames("SortPairsCopy"));

        // cub::DeviceMergeSort::SortKeys<KeyIteratorT,OffsetT,CompareOpT>
        // infoMap.put(new Info("cub::DeviceMergeSort::SortKeys<KeyIteratorT,int,CompareOpT>").javaNames("SortKeys"));

        // cub::DeviceMergeSort::SortKeysCopy<KeyInputIteratorT,KeyIteratorT,OffsetT,CompareOpT>
        // infoMap.put(new Info("cub::DeviceMergeSort::SortKeysCopy<KeyInputIteratorT,KeyIteratorT,int,CompareOpT>").javaNames("SortKeysCopy"));

        // cub::DeviceMergeSort::StableSortPairs<KeyIteratorT,ValueIteratorT,OffsetT,CompareOpT>
        // infoMap.put(new Info("cub::DeviceMergeSort::StableSortPairs<KeyIteratorT,ValueIteratorT,int,CompareOpT>").javaNames("StableSortPairs"));

        // cub::DeviceMergeSort::StableSortKeys<KeyIteratorT,OffsetT,CompareOpT>
        // infoMap.put(new Info("cub::DeviceMergeSort::StableSortKeys<KeyIteratorT,int,CompareOpT>").javaNames("StableSortKeys"));
    }

    private static void device_partition(InfoMap infoMap)
    {
        // cub::DevicePartition::Flagged<InputIteratorT,FlagIterator,OutputIteratorT,NumSelectedIteratorT>
        infoMap.put(new Info("cub::DevicePartition::Flagged<float*,char*,float*,int*>").javaNames("Flagged"));
        infoMap.put(new Info("cub::DevicePartition::Flagged<int*,char*,int*,int*>").javaNames("Flagged"));

        // cub::DevicePartition::If<InputIteratorT,OutputIteratorT,NumSelectedIteratorT,SelectOp>
        // infoMap.put(new Info("cub::DevicePartition::If<float*,float*,int*,SelectOp>").javaNames("If"));
        // infoMap.put(new Info("cub::DevicePartition::If<int*,int*,int*,SelectOp>").javaNames("If"));

        // cub::DevicePartition::If<InputIteratorT,FirstOutputIteratorT,SecondOutputIteratorT,UnselectedOutputIteratorT,NumSelectedIteratorT,SelectFirstPartOp,SelectSecondPartOp>
        // infoMap.put(new Info("cub::DevicePartition::If<float*,FirstOutputIteratorT,SecondOutputIteratorT,UnselectedOutputIteratorT,int*,SelectFirstPartOp,SelectSecondPartOp>").javaNames("If"));
        // infoMap.put(new Info("cub::DevicePartition::If<int*,FirstOutputIteratorT,SecondOutputIteratorT,UnselectedOutputIteratorT,int*,SelectFirstPartOp,SelectSecondPartOp>").javaNames("If"));
    }

    private static void device_radix_sort(InfoMap infoMap)
    {
        // cub::DeviceRadixSort::SortPairs<KeyT,ValueT,NumItemsT>
        // infoMap.put(new Info("cub::DeviceRadixSort::SortPairs<float,float,NumItemsT>").javaNames("SortPairs"));
        // infoMap.put(new Info("cub::DeviceRadixSort::SortPairs<int,int,NumItemsT>").javaNames("SortPairs"));

        // cub::DeviceRadixSort::SortPairsDescending<KeyT,ValueT,NumItemsT>
        // infoMap.put(new Info("cub::DeviceRadixSort::SortPairsDescending<float,float,NumItemsT>").javaNames("SortPairsDescending"));
        // infoMap.put(new Info("cub::DeviceRadixSort::SortPairsDescending<int,int,NumItemsT>").javaNames("SortPairsDescending"));

        // cub::DeviceRadixSort::SortKeys<KeyT,NumItemsT>
        // infoMap.put(new Info("cub::DeviceRadixSort::SortKeys<float,NumItemsT>").javaNames("SortKeys"));
        // infoMap.put(new Info("cub::DeviceRadixSort::SortKeys<int,NumItemsT>").javaNames("SortKeys"));

        // cub::DeviceRadixSort::SortKeysDescending<KeyT,NumItemsT>
        // infoMap.put(new Info("cub::DeviceRadixSort::SortKeysDescending<float,NumItemsT>").javaNames("SortKeysDescending"));
        // infoMap.put(new Info("cub::DeviceRadixSort::SortKeysDescending<int,NumItemsT>").javaNames("SortKeysDescending"));
    }

    private static void device_reduce(InfoMap infoMap)
    {
        // cub::DeviceReduce::Reduce<InputIteratorT,OutputIteratorT,ReductionOpT,T>
        // infoMap.put(new Info("cub::DeviceReduce::Reduce<float*,float*,ReductionOpT,T>").javaNames("Reduce"));
        // infoMap.put(new Info("cub::DeviceReduce::Reduce<int*,int*,ReductionOpT,T>").javaNames("Reduce"));

        // cub::DeviceReduce::Sum<InputIteratorT,OutputIteratorT>
        infoMap.put(new Info("cub::DeviceReduce::Sum<float*,float*>").javaNames("Sum"));
        infoMap.put(new Info("cub::DeviceReduce::Sum<int*,int*>").javaNames("Sum"));

        // cub::DeviceReduce::Min<InputIteratorT,OutputIteratorT>
        infoMap.put(new Info("cub::DeviceReduce::Min<float*,float*>").javaNames("Min"));
        infoMap.put(new Info("cub::DeviceReduce::Min<int*,int*>").javaNames("Min"));

        // cub::DeviceReduce::ArgMin<InputIteratorT,OutputIteratorT>
        // infoMap.put(new Info("cub::DeviceReduce::ArgMin<float*,float*>").javaNames("ArgMin"));
        // infoMap.put(new Info("cub::DeviceReduce::ArgMin<int*,int*>").javaNames("ArgMin"));

        // cub::DeviceReduce::Max<InputIteratorT,OutputIteratorT>
        infoMap.put(new Info("cub::DeviceReduce::Max<float*,float*>").javaNames("Max"));
        infoMap.put(new Info("cub::DeviceReduce::Max<int*,int*>").javaNames("Max"));

        // cub::DeviceReduce::ArgMax<InputIteratorT,OutputIteratorT>
        // infoMap.put(new Info("cub::DeviceReduce::ArgMax<float*,float*>").javaNames("ArgMax"));
        // infoMap.put(new Info("cub::DeviceReduce::ArgMax<int*,int*>").javaNames("ArgMax"));

        // cub::DeviceReduce::ReduceByKey<KeysInputIteratorT,UniqueOutputIteratorT,ValuesInputIteratorT,AggregatesOutputIteratorT,NumRunsOutputIteratorT,ReductionOpT>
        // infoMap.put(new Info("cub::DeviceReduce::ReduceByKey<KeysInputIteratorT,UniqueOutputIteratorT,ValuesInputIteratorT,AggregatesOutputIteratorT,unsigned int*,ReductionOpT>").javaNames("ReduceByKey"));
    }

    private static void device_run_length_encode(InfoMap infoMap)
    {
        // cub::DeviceRunLengthEncode::Encode<InputIteratorT,UniqueOutputIteratorT,LengthsOutputIteratorT,NumRunsOutputIteratorT>
        infoMap.put(new Info("cub::DeviceRunLengthEncode::Encode<float*,float*,unsigned int*,unsigned int*>").javaNames("Encode"));
        infoMap.put(new Info("cub::DeviceRunLengthEncode::Encode<int*,int*,unsigned int*,unsigned int*>").javaNames("Encode"));

        // cub::DeviceRunLengthEncode::NonTrivialRuns<InputIteratorT,OffsetsOutputIteratorT,LengthsOutputIteratorT,NumRunsOutputIteratorT>
        infoMap.put(new Info("cub::DeviceRunLengthEncode::NonTrivialRuns<float*,unsigned int*,unsigned int*,unsigned int*>").javaNames("NonTrivialRuns"));
        infoMap.put(new Info("cub::DeviceRunLengthEncode::NonTrivialRuns<int*,unsigned int*,unsigned int*,unsigned int*>").javaNames("NonTrivialRuns"));
    }

    private static void device_scan(InfoMap infoMap)
    {
        // cub::DeviceScan::ExclusiveSum<InputIteratorT,OutputIteratorT>
        infoMap.put(new Info("cub::DeviceScan::ExclusiveSum<float*,float*>").javaNames("ExclusiveSum"));
        infoMap.put(new Info("cub::DeviceScan::ExclusiveSum<int*,int*>").javaNames("ExclusiveSum"));

        // cub::DeviceScan::ExclusiveSum<IteratorT>
        //infoMap.put(new Info("cub::DeviceScan::ExclusiveSum<float*>").javaNames("ExclusiveSum"));
        //infoMap.put(new Info("cub::DeviceScan::ExclusiveSum<int*>").javaNames("ExclusiveSum"));

        // cub::DeviceScan::ExclusiveScan<InputIteratorT,OutputIteratorT,ScanOpT,InitValueT>
        // infoMap.put(new Info("cub::DeviceScan::ExclusiveScan<float*,float*,ScanOpT,InitValueT>").javaNames("ExclusiveScan"));
        // infoMap.put(new Info("cub::DeviceScan::ExclusiveScan<int*,int*,ScanOpT,InitValueT>").javaNames("ExclusiveScan"));

        // cub::DeviceScan::ExclusiveScan<IteratorT,ScanOpT,InitValueT>
        // infoMap.put(new Info("cub::DeviceScan::ExclusiveScan<float*,ScanOpT,InitValueT>").javaNames("ExclusiveScan"));
        // infoMap.put(new Info("cub::DeviceScan::ExclusiveScan<int*,ScanOpT,InitValueT>").javaNames("ExclusiveScan"));

        // cub::DeviceScan::ExclusiveScan<InputIteratorT,OutputIteratorT,ScanOpT,InitValueT,InitValueIterT>
        // infoMap.put(new Info("cub::DeviceScan::ExclusiveScan<float*,float*,ScanOpT,InitValueT,InitValueIterT>").javaNames("ExclusiveScan"));
        // infoMap.put(new Info("cub::DeviceScan::ExclusiveScan<int*,int*,ScanOpT,InitValueT,InitValueIterT>").javaNames("ExclusiveScan"));

        // cub::DeviceScan::ExclusiveScan<IteratorT,ScanOpT,InitValueT,InitValueIterT>
        // infoMap.put(new Info("cub::DeviceScan::ExclusiveScan<float*,ScanOpT,InitValueT,InitValueIterT>").javaNames("ExclusiveScan"));
        // infoMap.put(new Info("cub::DeviceScan::ExclusiveScan<int*,ScanOpT,InitValueT,InitValueIterT>").javaNames("ExclusiveScan"));

        // cub::DeviceScan::InclusiveSum<InputIteratorT,OutputIteratorT>
        infoMap.put(new Info("cub::DeviceScan::InclusiveSum<float*,float*>").javaNames("InclusiveSum"));
        infoMap.put(new Info("cub::DeviceScan::InclusiveSum<int*,int*>").javaNames("InclusiveSum"));

        // cub::DeviceScan::InclusiveSum<IteratorT>
        //infoMap.put(new Info("cub::DeviceScan::InclusiveSum<float*>").javaNames("InclusiveSum"));
        //infoMap.put(new Info("cub::DeviceScan::InclusiveSum<int*>").javaNames("InclusiveSum"));

        // cub::DeviceScan::InclusiveScan<InputIteratorT,OutputIteratorT,ScanOpT>
        // infoMap.put(new Info("cub::DeviceScan::InclusiveScan<float*,float*,ScanOpT>").javaNames("InclusiveScan"));
        // infoMap.put(new Info("cub::DeviceScan::InclusiveScan<int*,int*,ScanOpT>").javaNames("InclusiveScan"));

        // cub::DeviceScan::InclusiveScan<IteratorT,ScanOpT>
        // infoMap.put(new Info("cub::DeviceScan::InclusiveScan<float*,ScanOpT>").javaNames("InclusiveScan"));
        // infoMap.put(new Info("cub::DeviceScan::InclusiveScan<int*,ScanOpT>").javaNames("InclusiveScan"));

        // cub::DeviceScan::ExclusiveSumByKey<KeysInputIteratorT,ValuesInputIteratorT,ValuesOutputIteratorT,EqualityOpT>

        // cub::DeviceScan::ExclusiveScanByKey<KeysInputIteratorT,ValuesInputIteratorT,ValuesOutputIteratorT,ScanOpT,InitValueT,EqualityOpT>

        // cub::DeviceScan::InclusiveSumByKey<KeysInputIteratorT,ValuesInputIteratorT,ValuesOutputIteratorT,EqualityOpT>

        // cub::DeviceScan::InclusiveScanByKey<KeysInputIteratorT,ValuesInputIteratorT,ValuesOutputIteratorT,ScanOpT,EqualityOpT>
    }

    private static void device_segmented_radix_sort(InfoMap infoMap)
    {
        // cub::DeviceSegmentedRadixSort::SortPairs<KeyT,ValueT,BeginOffsetIteratorT,EndOffsetIteratorT>
        infoMap.put(new Info("cub::DeviceSegmentedRadixSort::SortPairs<float,float,int*,int*>").javaNames("SortPairs"));
        infoMap.put(new Info("cub::DeviceSegmentedRadixSort::SortPairs<int,int,int*,int*>").javaNames("SortPairs"));

        // cub::DeviceSegmentedRadixSort::SortPairsDescending<KeyT,ValueT,BeginOffsetIteratorT,EndOffsetIteratorT>
        infoMap.put(new Info("cub::DeviceSegmentedRadixSort::SortPairsDescending<float,float,int*,int*>").javaNames("SortPairsDescending"));
        infoMap.put(new Info("cub::DeviceSegmentedRadixSort::SortPairsDescending<int,int,int*,int*>").javaNames("SortPairsDescending"));

        // cub::DeviceSegmentedRadixSort::SortKeys<KeyT,BeginOffsetIteratorT,EndOffsetIteratorT>
        infoMap.put(new Info("cub::DeviceSegmentedRadixSort::SortKeys<float,int*,int*>").javaNames("SortKeys"));
        infoMap.put(new Info("cub::DeviceSegmentedRadixSort::SortKeys<int,int*,int*>").javaNames("SortKeys"));

        // cub::DeviceSegmentedRadixSort::SortKeysDescending<KeyT,BeginOffsetIteratorT,EndOffsetIteratorT>
        infoMap.put(new Info("cub::DeviceSegmentedRadixSort::SortKeysDescending<float,int*,int*>").javaNames("SortKeysDescending"));
        infoMap.put(new Info("cub::DeviceSegmentedRadixSort::SortKeysDescending<int,int*,int*>").javaNames("SortKeysDescending"));
    }

    private static void device_segmented_reduce(InfoMap infoMap)
    {
        // cub::DeviceSegmentedReduce::Reduce<InputIteratorT,OutputIteratorT,BeginOffsetIteratorT,EndOffsetIteratorT,ReductionOp,T>
        // infoMap.put(new Info("cub::DeviceSegmentedReduce::Reduce<float*,float*,int*,int*,ReductionOp,T>").javaNames("Reduce"));
        // infoMap.put(new Info("cub::DeviceSegmentedReduce::Reduce<int*,int*,int*,int*,ReductionOp,T>").javaNames("Reduce"));

        // cub::DeviceSegmentedReduce::Sum<InputIteratorT,OutputIteratorT,BeginOffsetIteratorT,EndOffsetIteratorT>
        infoMap.put(new Info("cub::DeviceSegmentedReduce::Sum<float*,float*,int*,int*>").javaNames("Sum"));
        infoMap.put(new Info("cub::DeviceSegmentedReduce::Sum<int*,int*,int*,int*>").javaNames("Sum"));

        // cub::DeviceSegmentedReduce::Min<InputIteratorT,OutputIteratorT,BeginOffsetIteratorT,EndOffsetIteratorT>
        infoMap.put(new Info("cub::DeviceSegmentedReduce::Min<float*,float*,int*,int*>").javaNames("Min"));
        infoMap.put(new Info("cub::DeviceSegmentedReduce::Min<int*,int*,int*,int*>").javaNames("Min"));

        // cub::DeviceSegmentedReduce::ArgMin<InputIteratorT,OutputIteratorT,BeginOffsetIteratorT,EndOffsetIteratorT>
        // infoMap.put(new Info("cub::DeviceSegmentedReduce::ArgMin<float*,float*,int*,int*>").javaNames("ArgMin"));
        // infoMap.put(new Info("cub::DeviceSegmentedReduce::ArgMin<int*,int*,int*,int*>").javaNames("ArgMin"));

        // cub::DeviceSegmentedReduce::Max<InputIteratorT,OutputIteratorT,BeginOffsetIteratorT,EndOffsetIteratorT>
        infoMap.put(new Info("cub::DeviceSegmentedReduce::Max<float*,float*,int*,int*>").javaNames("Max"));
        infoMap.put(new Info("cub::DeviceSegmentedReduce::Max<int*,int*,int*,int*>").javaNames("Max"));

        // cub::DeviceSegmentedReduce::ArgMax<InputIteratorT,OutputIteratorT,BeginOffsetIteratorT,EndOffsetIteratorT>
        // infoMap.put(new Info("cub::DeviceSegmentedReduce::ArgMax<float*,float*,int*,int*>").javaNames("ArgMax"));
        // infoMap.put(new Info("cub::DeviceSegmentedReduce::ArgMax<int*,int*,int*,int*>").javaNames("ArgMax"));
    }

    private static void device_segmented_sort(InfoMap infoMap)
    {
        // cub::DeviceSegmentedSort::SortKeys<KeyT,BeginOffsetIteratorT,EndOffsetIteratorT>
        infoMap.put(new Info("cub::DeviceSegmentedSort::SortKeys<float,int*,int*>").javaNames("SortKeys"));
        infoMap.put(new Info("cub::DeviceSegmentedSort::SortKeys<int,int*,int*>").javaNames("SortKeys"));

        // cub::DeviceSegmentedSort::SortKeysDescending<KeyT,BeginOffsetIteratorT,EndOffsetIteratorT>
        infoMap.put(new Info("cub::DeviceSegmentedSort::SortKeysDescending<float,int*,int*>").javaNames("SortKeysDescending"));
        infoMap.put(new Info("cub::DeviceSegmentedSort::SortKeysDescending<int,int*,int*>").javaNames("SortKeysDescending"));

        // cub::DeviceSegmentedSort::StableSortKeys<KeyT,BeginOffsetIteratorT,EndOffsetIteratorT>
        infoMap.put(new Info("cub::DeviceSegmentedSort::StableSortKeys<float,int*,int*>").javaNames("StableSortKeys"));
        infoMap.put(new Info("cub::DeviceSegmentedSort::StableSortKeys<int,int*,int*>").javaNames("StableSortKeys"));

        // cub::DeviceSegmentedSort::StableSortKeysDescending<KeyT,BeginOffsetIteratorT,EndOffsetIteratorT>
        infoMap.put(new Info("cub::DeviceSegmentedSort::StableSortKeysDescending<float,int*,int*>").javaNames("StableSortKeysDescending"));
        infoMap.put(new Info("cub::DeviceSegmentedSort::StableSortKeysDescending<int,int*,int*>").javaNames("StableSortKeysDescending"));

        // cub::DeviceSegmentedSort::SortPairs<KeyT,ValueT,BeginOffsetIteratorT,EndOffsetIteratorT>
        infoMap.put(new Info("cub::DeviceSegmentedSort::SortPairs<float,float,int*,int*>").javaNames("SortPairs"));
        infoMap.put(new Info("cub::DeviceSegmentedSort::SortPairs<int,int,int*,int*>").javaNames("SortPairs"));

        // cub::DeviceSegmentedSort::SortPairsDescending<KeyT,ValueT,BeginOffsetIteratorT,EndOffsetIteratorT>
        infoMap.put(new Info("cub::DeviceSegmentedSort::SortPairsDescending<float,float,int*,int*>").javaNames("SortPairsDescending"));
        infoMap.put(new Info("cub::DeviceSegmentedSort::SortPairsDescending<int,int,int*,int*>").javaNames("SortPairsDescending"));

        // cub::DeviceSegmentedSort::StableSortPairs<KeyT,ValueT,BeginOffsetIteratorT,EndOffsetIteratorT>
        infoMap.put(new Info("cub::DeviceSegmentedSort::StableSortPairs<float,float,int*,int*>").javaNames("StableSortPairs"));
        infoMap.put(new Info("cub::DeviceSegmentedSort::StableSortPairs<int,int,int*,int*>").javaNames("StableSortPairs"));

        // cub::DeviceSegmentedSort::StableSortPairsDescending<KeyT,ValueT,BeginOffsetIteratorT,EndOffsetIteratorT>
        infoMap.put(new Info("cub::DeviceSegmentedSort::StableSortPairsDescending<float,float,int*,int*>").javaNames("StableSortPairsDescending"));
        infoMap.put(new Info("cub::DeviceSegmentedSort::StableSortPairsDescending<int,int,int*,int*>").javaNames("StableSortPairsDescending"));
    }

    private static void device_select(InfoMap infoMap)
    {
        // cub::DeviceSelect::Flagged<InputIteratorT,FlagIterator,OutputIteratorT,NumSelectedIteratorT>
        infoMap.put(new Info("cub::DeviceSelect::Flagged<float*,char*,float*,int*>").javaNames("Flagged"));
        infoMap.put(new Info("cub::DeviceSelect::Flagged<int*,char*,int*,int*>").javaNames("Flagged"));

        // cub::DeviceSelect::Flagged<IteratorT,FlagIterator,NumSelectedIteratorT>
        //infoMap.put(new Info("cub::DeviceSelect::Flagged<float*,char*,int*>").javaNames("Flagged"));
        //infoMap.put(new Info("cub::DeviceSelect::Flagged<int*,char*,int*>").javaNames("Flagged"));

        // cub::DeviceSelect::If<InputIteratorT,OutputIteratorT,NumSelectedIteratorT,SelectOp>
        // infoMap.put(new Info("cub::DeviceSelect::If<float*,float*,int*,SelectOp>").javaNames("If"));
        // infoMap.put(new Info("cub::DeviceSelect::If<int*,int*,int*,SelectOp>").javaNames("If"));

        // cub::DeviceSelect::If<IteratorT,NumSelectedIteratorT,SelectOp>
        // infoMap.put(new Info("cub::DeviceSelect::If<float*,int*,SelectOp>").javaNames("If"));
        // infoMap.put(new Info("cub::DeviceSelect::If<int*,int*,SelectOp>").javaNames("If"));

        // cub::DeviceSelect::Unique<InputIteratorT,OutputIteratorT,NumSelectedIteratorT>
        infoMap.put(new Info("cub::DeviceSelect::Unique<float*,float*,int*>").javaNames("Unique"));
        infoMap.put(new Info("cub::DeviceSelect::Unique<int*,int*,int*>").javaNames("Unique"));

        // cub::DeviceSelect::UniqueByKey<KeyInputIteratorT,ValueInputIteratorT,KeyOutputIteratorT,ValueOutputIteratorT,NumSelectedIteratorT>
        // infoMap.put(new Info("cub::DeviceSelect::UniqueByKey<KeyInputIteratorT,ValueInputIteratorT,KeyOutputIteratorT,ValueOutputIteratorT,int*>").javaNames("UniqueByKey"));
    }

    private static void device_spmv(InfoMap infoMap)
    {
        // cub::DeviceSpmv::CsrMV<ValueT>
        infoMap.put(new Info("cub::DeviceSpmv::CsrMV<float>").javaNames("CsrMV"));
        infoMap.put(new Info("cub::DeviceSpmv::CsrMV<int>").javaNames("CsrMV"));
    }
}
