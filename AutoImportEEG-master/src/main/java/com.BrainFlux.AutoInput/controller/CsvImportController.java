package com.BrainFlux.AutoInput.controller;


import com.BrainFlux.AutoInput.service.AutoStreaming;
import com.BrainFlux.AutoInput.service.ProducerCSVService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/kafkaProducer_api")
@Api(value="kafkaProducerController",description = "kafkaProducerController")
public class CsvImportController {

    @Autowired
    private ProducerCSVService producerCSVService;


    @GetMapping("/importCSV")
    // url for import new csv
    public ResponseEntity<Map<String, Object>> importCSVEvent(String dir) throws IOException, InterruptedException {
        Map<String, Object> map = new HashMap<>();
        producerCSVService.importCSVEvent(dir);


        map.put("msg","success");
//        producerCSVService.importCSVEvent(dir);
        return ResponseEntity.status(HttpStatus.CREATED).body(map);
    }
}
