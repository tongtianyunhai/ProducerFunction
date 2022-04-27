package com.BrainFlux.AutoInput.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RowData {
    private String pid;
    private long timestamp;
    private Map<String,Object> rowValues;
}
