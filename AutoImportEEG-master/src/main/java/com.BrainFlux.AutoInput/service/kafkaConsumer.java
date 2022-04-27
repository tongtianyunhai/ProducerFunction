//package com.BrainFlux.AutoInput.service;
//
//
//import com.BrainFlux.AutoInput.config.InfluxUtil;
//import com.BrainFlux.AutoInput.domain.RowData;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.influxdb.BatchOptions;
//import org.influxdb.InfluxDB;
//import org.influxdb.dto.BatchPoints;
//import org.influxdb.dto.Point;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.support.Acknowledgment;
//import org.springframework.stereotype.Component;
//
//import java.io.File;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
///**
// * @Author：geliyang
// * @Version：1.0
// * @Date：2022/3/23-6:10
// * @Since:jdk1.8
// * @Description:TODO
// */
//
//@Configuration
//@Component
//@Slf4j
//public class kafkaConsumer {
//    private final static String dbName = "data";
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//    private static BatchPoints records = BatchPoints.database(dbName).build();
//    private static Integer batchSize=0;
//    private static long totalTime=0;
//    private static String path="C:\\Users\\Administrator\\Desktop\\kafkaLog\\log.txt";
//    private static HashMap<String,Integer> checkData=new HashMap<>();
//    @Autowired
//    ObjectMapper objectMapper;
//    @KafkaListener(groupId = "csv-import-listener-group",topics = {"import-CSV-events"})
//    public void processImportCSVEvent(ConsumerRecord<Integer,String> consumerRecord) throws JsonProcessingException {
//        InfluxDB idb = generateIdbClient();
//        RowData rowData = objectMapper.readValue(consumerRecord.value(), RowData.class);
//        System.out.println(consumerRecord.offset());
//        if(rowData.getTimestamp()!=0&&rowData.getTimestamp()!=1){
//            Map<String, Object> rowValues=rowData.getRowValues();
//            long measurementEpoch = rowData.getTimestamp();
//            String pid= rowData.getPid();
//            Point record = Point.measurement(pid).time(measurementEpoch, TimeUnit.MILLISECONDS).fields(rowValues).build();
//            records.point(record);
//            if(!checkData.containsKey(pid)){
//                checkData.put(pid,1);
//            }else {
//                int count=checkData.get(pid);
//                count+=1;
//                checkData.put(pid,count);
//            }
//            batchSize++;
//        }
//        if(rowData.getTimestamp()==1){
//            Map<String, Object> rowValues=rowData.getRowValues();
//            int number= (int) rowValues.get("finishThisFile");
//            String filePath= (String) rowValues.get("filePath");
//            File fileTmp= new File(filePath);
//            AutoStreaming autoStreaming=new AutoStreaming();
//            String pid= rowData.getPid();
//            if(number==checkData.get(pid)){
//                    try {
//                        String path2="C:\\Users\\Administrator\\Desktop\\kafkaTest";
//                        System.out.println(filePath);
//                        autoStreaming.moveFile(path2,fileTmp);
//                    }catch (Exception e){
//                        autoStreaming.setLog2("moveFailure",path);
//                    }
//            }else {
//              StringBuffer str=new StringBuffer();
//              str.append("loseData: ");
//              str.append(filePath);
//              autoStreaming.setLog2(str.toString(),path);
//            }
//        }
//        if(batchSize==100){
//            AutoStreaming autoStreaming=new AutoStreaming();
//            long start = System.currentTimeMillis();
//            idb.write(records);
//            long end = System.currentTimeMillis();
//            totalTime+=(end-start)/1000;
//            records = BatchPoints.database(dbName).build();
//            batchSize=0;
//        }else if(rowData.getTimestamp()==0){
//            long start = System.currentTimeMillis();
//            idb.write(records);
//            long end = System.currentTimeMillis();
//            idb.close();
//            totalTime+=(end-start)/1000;
//            System.out.println(totalTime+ " s");
//        }
//    }
//
//    private InfluxDB generateIdbClient() {
//        // Disable GZip to save CPU
//        InfluxDB idb = InfluxUtil.generateIdbClient(true);
//        BatchOptions bo = BatchOptions.DEFAULTS.consistency(InfluxDB.ConsistencyLevel.ALL)
//                // Flush every 2000 Points, at least every 100ms, buffer for failed oper is 2200
//                .actions(100).flushDuration(1000).bufferLimit(5000).jitterDuration(200)
//                .exceptionHandler((p, t) -> logger.warn("Write point failed", t));
//        idb.enableBatch(bo);
//        idb.query(new org.influxdb.dto.Query(String.format("CREATE DATABASE \"%s\"", dbName), dbName));
//
//        return idb;
//    }
//
//}
