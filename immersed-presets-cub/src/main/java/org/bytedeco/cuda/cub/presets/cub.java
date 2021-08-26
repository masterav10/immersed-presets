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
        inherit = cudart.class, 
        names = {"windows-x86_64"}, 
        target = "org.bytedeco.cuda.cub", 
        global = "org.bytedeco.cuda.cub.global",
        value = {
            @Platform(
                include = {
                    "<cub/device/device_histogram.cuh>",
                    "<cub/device/device_partition.cuh>",
                    "<cub/device/device_radix_sort.cuh>",
                    "<cub/device/device_reduce.cuh>",
                    "<cub/device/device_run_length_encode.cuh>",
                    "<cub/device/device_scan.cuh>",
                    "<cub/device/device_segmented_radix_sort.cuh>",
                    "<cub/device/device_segmented_reduce.cuh>", 
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
        infoMap.put(new Info("CUB_NS_PREFIX", "CUB_RUNTIME_FUNCTION").cppTypes().annotations());
        infoMap.put(new Info("DoubleBuffer").skip());
        
        device_histogram(infoMap);
        device_partition(infoMap);
        device_radix_sort(infoMap);
        device_reduce(infoMap);
        device_run_length_encode(infoMap);
        device_scan(infoMap);
        device_segmented_radix_sort(infoMap);
        device_segmented_reduce(infoMap);
        device_select(infoMap);
        device_spmv(infoMap);
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
        infoMap.put(new Info("cub::DeviceHistogram::MultiHistogramRange<1,1,float*,unsigned int,float,int>").javaNames("MultiHistogramRange1Channel"));
        infoMap.put(new Info("cub::DeviceHistogram::MultiHistogramRange<2,2,float*,unsigned int,float,int>").javaNames("MultiHistogramRange2Channel"));
        infoMap.put(new Info("cub::DeviceHistogram::MultiHistogramRange<3,3,float*,unsigned int,float,int>").javaNames("MultiHistogramRange3Channel"));
        infoMap.put(new Info("cub::DeviceHistogram::MultiHistogramRange<4,4,float*,unsigned int,float,int>").javaNames("MultiHistogramRange4Channel"));
        infoMap.put(new Info("cub::DeviceHistogram::MultiHistogramRange<1,1,int*,unsigned int,int,int>").javaNames("MultiHistogramRange1Channel"));
        infoMap.put(new Info("cub::DeviceHistogram::MultiHistogramRange<2,2,int*,unsigned int,int,int>").javaNames("MultiHistogramRange2Channel"));
        infoMap.put(new Info("cub::DeviceHistogram::MultiHistogramRange<3,3,int*,unsigned int,int,int>").javaNames("MultiHistogramRange3Channel"));
        infoMap.put(new Info("cub::DeviceHistogram::MultiHistogramRange<4,4,int*,unsigned int,int,int>").javaNames("MultiHistogramRange4Channel"));
    }
    
    private static void device_partition(InfoMap infoMap)
    {
        // cub::DevicePartition::Flagged<InputIteratorT,FlagIterator,OutputIteratorT,NumSelectedIteratorT>
        infoMap.put(new Info("cub::DevicePartition::Flagged<float*,char*,float*,int*>").javaNames("Flagged"));
        infoMap.put(new Info("cub::DevicePartition::Flagged<int*,char*,int*,int*>").javaNames("Flagged"));

        // cub::DevicePartition::If<InputIteratorT,OutputIteratorT,NumSelectedIteratorT,SelectOp>
        // infoMap.put(new Info("cub::DevicePartition::If<float*,float*,int*,SelectOp>").javaNames("If"));
        // infoMap.put(new Info("cub::DevicePartition::If<int*,int*,int*,SelectOp>").javaNames("If"));
    }
    
    private static void device_radix_sort(InfoMap infoMap)
    {
        // cub::DeviceRadixSort::SortPairs<KeyT,ValueT>
        infoMap.put(new Info("cub::DeviceRadixSort::SortPairs<float,float>").javaNames("SortPairs"));
        infoMap.put(new Info("cub::DeviceRadixSort::SortPairs<int,int>").javaNames("SortPairs"));

        // cub::DeviceRadixSort::SortPairsDescending<KeyT,ValueT>
        infoMap.put(new Info("cub::DeviceRadixSort::SortPairsDescending<float,float>").javaNames("SortPairsDescending"));
        infoMap.put(new Info("cub::DeviceRadixSort::SortPairsDescending<int,int>").javaNames("SortPairsDescending"));

        // cub::DeviceRadixSort::SortKeys<KeyT>
        infoMap.put(new Info("cub::DeviceRadixSort::SortKeys<float>").javaNames("SortKeys"));
        infoMap.put(new Info("cub::DeviceRadixSort::SortKeys<int>").javaNames("SortKeys"));

        // cub::DeviceRadixSort::SortKeysDescending<KeyT>
        infoMap.put(new Info("cub::DeviceRadixSort::SortKeysDescending<float>").javaNames("SortKeysDescending"));
        infoMap.put(new Info("cub::DeviceRadixSort::SortKeysDescending<int>").javaNames("SortKeysDescending"));
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
        // infoMap.put(new Info("cub::DeviceReduce::ReduceByKey<KeysInputIteratorT,UniqueOutputIteratorT,ValuesInputIteratorT,AggregatesOutputIteratorT,unsigned int,ReductionOpT>").javaNames("ReduceByKey"));
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

        // cub::DeviceScan::ExclusiveScan<InputIteratorT,OutputIteratorT,ScanOpT,InitValueT>
        // infoMap.put(new Info("cub::DeviceScan::ExclusiveScan<float*,float*,ScanOpT,InitValueT>").javaNames("ExclusiveScan"));
        // infoMap.put(new Info("cub::DeviceScan::ExclusiveScan<int*,int*,ScanOpT,InitValueT>").javaNames("ExclusiveScan"));

        // cub::DeviceScan::InclusiveSum<InputIteratorT,OutputIteratorT>
        infoMap.put(new Info("cub::DeviceScan::InclusiveSum<float*,float*>").javaNames("InclusiveSum"));
        infoMap.put(new Info("cub::DeviceScan::InclusiveSum<int*,int*>").javaNames("InclusiveSum"));

        // cub::DeviceScan::InclusiveScan<InputIteratorT,OutputIteratorT,ScanOpT>
        // infoMap.put(new Info("cub::DeviceScan::InclusiveScan<float*,float*,ScanOpT>").javaNames("InclusiveScan"));
        // infoMap.put(new Info("cub::DeviceScan::InclusiveScan<int*,int*,ScanOpT>").javaNames("InclusiveScan"));
    }
    
    private static void device_segmented_radix_sort(InfoMap infoMap)
    {
        // cub::DeviceSegmentedRadixSort::SortPairs<KeyT,ValueT,OffsetIteratorT>
        infoMap.put(new Info("cub::DeviceSegmentedRadixSort::SortPairs<float,float,unsigned int*>").javaNames("SortPairs"));
        infoMap.put(new Info("cub::DeviceSegmentedRadixSort::SortPairs<int,int,unsigned int*>").javaNames("SortPairs"));

        // cub::DeviceSegmentedRadixSort::SortPairsDescending<KeyT,ValueT,OffsetIteratorT>
        infoMap.put(new Info("cub::DeviceSegmentedRadixSort::SortPairsDescending<float,float,unsigned int*>").javaNames("SortPairsDescending"));
        infoMap.put(new Info("cub::DeviceSegmentedRadixSort::SortPairsDescending<int,int,unsigned int*>").javaNames("SortPairsDescending"));

        // cub::DeviceSegmentedRadixSort::SortKeys<KeyT,OffsetIteratorT>
        infoMap.put(new Info("cub::DeviceSegmentedRadixSort::SortKeys<float,unsigned int*>").javaNames("SortKeys"));
        infoMap.put(new Info("cub::DeviceSegmentedRadixSort::SortKeys<int,unsigned int*>").javaNames("SortKeys"));

        // cub::DeviceSegmentedRadixSort::SortKeysDescending<KeyT,OffsetIteratorT>
        infoMap.put(new Info("cub::DeviceSegmentedRadixSort::SortKeysDescending<float,unsigned int*>").javaNames("SortKeysDescending"));
        infoMap.put(new Info("cub::DeviceSegmentedRadixSort::SortKeysDescending<int,unsigned int*>").javaNames("SortKeysDescending"));
    }
    
    private static void device_segmented_reduce(InfoMap infoMap)
    {
        // cub::DeviceSegmentedReduce::Reduce<InputIteratorT,OutputIteratorT,OffsetIteratorT,ReductionOp,T>
        // infoMap.put(new Info("cub::DeviceSegmentedReduce::Reduce<float*,float*,unsigned int*,ReductionOp,T>").javaNames("Reduce"));
        // infoMap.put(new Info("cub::DeviceSegmentedReduce::Reduce<int*,int*,unsigned int*,ReductionOp,T>").javaNames("Reduce"));

        // cub::DeviceSegmentedReduce::Sum<InputIteratorT,OutputIteratorT,OffsetIteratorT>
        infoMap.put(new Info("cub::DeviceSegmentedReduce::Sum<float*,float*,unsigned int*>").javaNames("Sum"));
        infoMap.put(new Info("cub::DeviceSegmentedReduce::Sum<int*,int*,unsigned int*>").javaNames("Sum"));

        // cub::DeviceSegmentedReduce::Min<InputIteratorT,OutputIteratorT,OffsetIteratorT>
        infoMap.put(new Info("cub::DeviceSegmentedReduce::Min<float*,float*,unsigned int*>").javaNames("Min"));
        infoMap.put(new Info("cub::DeviceSegmentedReduce::Min<int*,int*,unsigned int*>").javaNames("Min"));

        // cub::DeviceSegmentedReduce::ArgMin<InputIteratorT,OutputIteratorT,OffsetIteratorT>
        // infoMap.put(new Info("cub::DeviceSegmentedReduce::ArgMin<float*,float*,unsigned int*>").javaNames("ArgMin"));
        // infoMap.put(new Info("cub::DeviceSegmentedReduce::ArgMin<int*,int*,unsigned int*>").javaNames("ArgMin"));

        // cub::DeviceSegmentedReduce::Max<InputIteratorT,OutputIteratorT,OffsetIteratorT>
        infoMap.put(new Info("cub::DeviceSegmentedReduce::Max<float*,float*,unsigned int*>").javaNames("Max"));
        infoMap.put(new Info("cub::DeviceSegmentedReduce::Max<int*,int*,unsigned int*>").javaNames("Max"));

        // cub::DeviceSegmentedReduce::ArgMax<InputIteratorT,OutputIteratorT,OffsetIteratorT>
        // infoMap.put(new Info("cub::DeviceSegmentedReduce::ArgMax<float*,float*,unsigned int*>").javaNames("ArgMax"));
        // infoMap.put(new Info("cub::DeviceSegmentedReduce::ArgMax<int*,int*,unsigned int*>").javaNames("ArgMax"));
    }
    
    private static void device_select(InfoMap infoMap)
    {
        // cub::DeviceSelect::Flagged<InputIteratorT,FlagIterator,OutputIteratorT,NumSelectedIteratorT>
        infoMap.put(new Info("cub::DeviceSelect::Flagged<float*,char*,float*,int*>").javaNames("Flagged"));
        infoMap.put(new Info("cub::DeviceSelect::Flagged<int*,char*,int*,int*>").javaNames("Flagged"));

        // cub::DeviceSelect::If<InputIteratorT,OutputIteratorT,NumSelectedIteratorT,SelectOp>
        // infoMap.put(new Info("cub::DeviceSelect::If<float*,float*,int*,SelectOp>").javaNames("If"));
        // infoMap.put(new Info("cub::DeviceSelect::If<int*,int*,int*,SelectOp>").javaNames("If"));

        // cub::DeviceSelect::Unique<InputIteratorT,OutputIteratorT,NumSelectedIteratorT>
        infoMap.put(new Info("cub::DeviceSelect::Unique<float*,float*,int*>").javaNames("Unique"));
        infoMap.put(new Info("cub::DeviceSelect::Unique<int*,int*,int*>").javaNames("Unique"));
    }
    
    private static void device_spmv(InfoMap infoMap)
    {
        // cub::DeviceSpmv::CsrMV<ValueT>
        infoMap.put(new Info("cub::DeviceSpmv::CsrMV<float>").javaNames("CsrMV"));
        infoMap.put(new Info("cub::DeviceSpmv::CsrMV<int>").javaNames("CsrMV"));
    }
}
