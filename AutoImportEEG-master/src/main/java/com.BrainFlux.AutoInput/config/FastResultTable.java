package com.BrainFlux.AutoInput.config;

import org.influxdb.dto.QueryResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Fast dictionary-like result table, ALL returns are reference
 */
public class FastResultTable extends ResultTable {

    private List<List<Object>> values;

    /**
     * Generate an empty table
     */
    public FastResultTable() {
    }

    public FastResultTable(QueryResult.Series series) {
        if (series == null) return;

        super.extractResultColRowNumber(series);
        super.dataColumns = series.getColumns();
        this.values = series.getValues();
    }

    @Override
    public List<Object> getDatalistByColumnName(String columnName) {
        int colIdx = super.getColumnIndexByName(columnName);
        return getDatalistByColumnId(colIdx);
    }

    public List<Object> getDatalistByColumnId(int columnId) {
        if (super.rowCount == 0 || super.colCount == 0) return new ArrayList<>(0);

        List<Object> res = new ArrayList<>(super.rowCount);
        for (int i = 0; i < super.rowCount; i++) {
            res.add(this.values.get(i).get(columnId));
        }

        return res;
    }

    @Override
    public List<Object> getDatalistByRow(int rowNum) {
        if (super.rowCount == 0 || super.colCount == 0) return new ArrayList<>(0);
        return this.values.get(rowNum);
    }

    @Override
    public Object getDataByColAndRow(int colNum, int rowNum) {
        if (super.rowCount == 0 || super.colCount == 0) return null;
        return this.values.get(rowNum).get(colNum);
    }

    @Override
    public Object getDataByColumnNameAndRow(String columnName, int rowNum) {
        if (super.rowCount == 0 || super.colCount == 0) return null;
        int colIndex = super.dataColumns.indexOf(columnName);
        return getDataByColAndRow(colIndex, rowNum);
    }


}
