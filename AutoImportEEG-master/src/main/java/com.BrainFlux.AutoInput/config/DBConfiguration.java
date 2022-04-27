package com.BrainFlux.AutoInput.config;

/**
 * DB Names and measurement names
 */
public class DBConfiguration {

    private DBConfiguration() {
    }

    public class RelationalData {
        private RelationalData() {
        }

        public static final String DBNAME = "upmc";

        /**
         * Feature <---> Column mapping
         */
        public static final String HEADERMAPING = "feature_mapping_tmp";
    }

    /**
     * Measurements in data table
     */
    public class Data {
        private Data() {
        }

        public static final String DBNAME = "data";

        /**
         * Table name for patients
         */
        public static final String PATIENT = "Patient";
    }

    /**
     * Measurements in application table
     */
    public class Sys {
        private Sys() {
        }

        public static final String DBNAME = "sys";

        /**
         * Table name for files
         */
        public static final String FILE = "File";
    }

}
