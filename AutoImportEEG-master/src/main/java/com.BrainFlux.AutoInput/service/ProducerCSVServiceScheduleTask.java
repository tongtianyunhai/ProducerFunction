package com.BrainFlux.AutoInput.service;


import com.BrainFlux.AutoInput.domain.ImportCSVEvent;
import com.BrainFlux.AutoInput.domain.RowData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import utils.TimeUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author：geliyang
 * @Version：1.0
 * @Date：2022/3/30-15:16
 * @Since:jdk1.8
 * @Description:TODO
 */
@Service
@EnableKafka
public class ProducerCSVServiceScheduleTask {

    @Autowired(required = false)
    private KafkaTemplate<Integer,String> kafkaTemplate;
    private String infoHead[];
    private int eachRowLength=0;

    @Autowired(required = false)
    private AutoStreaming autoStreaming;

    public void importCSVEvent(String dir,String scheduleTaskID) throws IOException, InterruptedException {
        ObjectMapper objectMapper=new ObjectMapper();

        List<File> fileList= autoStreaming.getAllCSV(dir);


        //startTime
        Map<String, String> CSVHeaderInitial = new HashMap<>();
        CSVHeaderInitial.put("fileName", "PUH-2015-015_09ar.csv");
        CSVHeaderInitial.put("arType", "ar");
        CSVHeaderInitial.put("fileUUID", "0");

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime=localDateTime.format(formatter);

        RowData rowDataInitital=new RowData("start",2,null);
        rowDataInitital.setPid(formatDateTime);
        ImportCSVEvent importCSVEventInitial = new ImportCSVEvent(1,CSVHeaderInitial,rowDataInitital);
        String valueInitial = objectMapper.writeValueAsString(importCSVEventInitial.getRowData());
        Map<String,String> headerInitial = importCSVEventInitial.getHeader();
        ProducerRecord<Integer,String> producerRecordInitial = buildProducerRecord(null, valueInitial, headerInitial,0);
        try {
            kafkaTemplate.send(producerRecordInitial);
        }catch (Exception e){
            AutoStreaming autoStreaming=new AutoStreaming();
            StringBuffer str2=new StringBuffer();
            str2.append("Last file failure");
            String path="C:\\Users\\Administrator\\Desktop\\kafkaLog";
            autoStreaming.setLog2(str2.toString(),path);
        }

        for (int k=0;k<fileList.size();k++) {
            int eventId=k%3;
           String fileName = fileList.get(k).toString();
            //invoke file import
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            CSVReader csvReader = new CSVReader(bufferedReader);
            //get fileName from first line
            String firstLine = bufferedReader.readLine();
            String pid = "";
            if (firstLine.contains("PUH")) {
                String fileTile[];
                fileTile = firstLine.split("\\\\");
                pid = fileTile[2].split(" ")[0].split("_")[0];
            }

            //kafka header
            Map<String, String> CSVHeader = new HashMap<>();
            CSVHeader.put("fileName", "PUH-2015-015_09ar.csv");
            CSVHeader.put("arType", "ar");
            CSVHeader.put("fileUUID", "0");

            //PatientName
            bufferedReader.readLine();
            //PatientBirthDate
            bufferedReader.readLine();
            bufferedReader.readLine();
            //get Date
            String[] Date;
            StringBuffer stringBuffer = new StringBuffer();
            for (int n = 0; n < 2; n++) {
                Date = bufferedReader.readLine().split(",");
                stringBuffer.append(Date[1]);
            }
            bufferedReader.readLine();

            //get InformationHead
            infoHead = bufferedReader.readLine().split(",");
            eachRowLength = infoHead.length;
            //get DateInfomation
            String str = "";
            long count=0;
            while ((str = bufferedReader.readLine()) != null) {
                String dataInfo[];
                dataInfo = str.split(",");
                Map<String, Object> rowDateInfo = new HashMap<>();
                for (int m = 1; m < dataInfo.length; m++) {
                    rowDateInfo.put(infoHead[m], Double.valueOf(dataInfo[m]));
                }
                double sTime = Double.parseDouble(dataInfo[0]);
                java.util.Date measurementDate = TimeUtil.serialTimeToDate(sTime, null);
                long timestamp = measurementDate.getTime();
                RowData rowData = new RowData(pid, timestamp, rowDateInfo);
                // Send kafka event
                ImportCSVEvent importCSVEvent = new ImportCSVEvent(eventId, CSVHeader, rowData);
                Integer partition= importCSVEvent.getEventId();
                String value = objectMapper.writeValueAsString(importCSVEvent.getRowData());
                Map<String, String> header = importCSVEvent.getHeader();
                ProducerRecord<Integer, String> producerRecord = buildProducerRecord(null, value, header,partition);
                try {
                    kafkaTemplate.send(producerRecord);
                    count++;
                }catch (Exception e){
                    AutoStreaming autoStreaming=new AutoStreaming();
                    StringBuffer str2=new StringBuffer();
                    str2.append(pid);
                    str2.append("producer failure");
                    String path="C:\\Users\\Administrator\\Desktop\\kafkaLog";
                    autoStreaming.setLog2(str2.toString(),path);
                }
            }
            Map<String, Object> rowDateInfo = new HashMap<>();
            rowDateInfo.put("finishThisFile",count);
            rowDateInfo.put("filePath",fileName);
            RowData rowData=new RowData(pid,1,rowDateInfo);
            ImportCSVEvent importCSVEvent = new ImportCSVEvent(eventId,CSVHeader,rowData);
            Integer partition= importCSVEvent.getEventId();
            String value = objectMapper.writeValueAsString(importCSVEvent.getRowData());
            Map<String,String> header = importCSVEvent.getHeader();
            ProducerRecord<Integer,String> producerRecord = buildProducerRecord(null, value, header,partition);
            try {
                kafkaTemplate.send(producerRecord);
            }catch (Exception e){
                AutoStreaming autoStreaming=new AutoStreaming();
                StringBuffer str2=new StringBuffer();
                str2.append(pid);
                str2.append("checkFile failure");
                String path="C:\\Users\\Administrator\\Desktop\\kafkaLog";
                autoStreaming.setLog2(str2.toString(),path);
            }
            bufferedReader.close();
            csvReader.close();
//            try {
//                Thread.sleep(5000);
//                System.out.println("wait 5s");
//            }catch (Exception e){
//
//            }
            System.out.println("eventId"+eventId);
        }
        //last line
        Map<String, String> CSVHeader = new HashMap<>();
        CSVHeader.put("fileName", "PUH-2015-015_09ar.csv");
        CSVHeader.put("arType", "ar");
        CSVHeader.put("fileUUID", "0");
        RowData rowData=new RowData(scheduleTaskID,0,null);
        ImportCSVEvent importCSVEvent = new ImportCSVEvent(1,CSVHeader,rowData);
        Integer partition= importCSVEvent.getEventId();
        String value = objectMapper.writeValueAsString(importCSVEvent.getRowData());
        Map<String,String> header = importCSVEvent.getHeader();
        ProducerRecord<Integer,String> producerRecord = buildProducerRecord(null, value, header,0);
        try {
            kafkaTemplate.send(producerRecord);
        }catch (Exception e){
            AutoStreaming autoStreaming=new AutoStreaming();
            StringBuffer str2=new StringBuffer();
            str2.append("Last file failure");
            String path="C:\\Users\\Administrator\\Desktop\\kafkaLog";
            autoStreaming.setLog2(str2.toString(),path);
        }
    }

    public ProducerRecord<Integer,String> buildProducerRecord(Integer key, String value, Map<String, String> header,Integer partition) {
        List<Header> recordHeaders = new ArrayList<>();
        header.forEach((k,v) -> {
            recordHeaders.add(new RecordHeader(k, v.getBytes()));
        });
        return new ProducerRecord<>("import-CSV-events", partition, key, value, recordHeaders);
    }

}
