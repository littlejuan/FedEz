package org.littlejuan.fedez.database;

import android.provider.BaseColumns;

public class FedEzContract {
    private FedEzContract() { }

    public static class FedEzEntry implements BaseColumns {
        public static final String TABLE_NAME = "envios";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NOMBRE = "nombre";
        public static final String COLUMN_PESO = "peso";
        public static final String COLUMN_CONTINENTE = "continente";
        public static final String COLUMN_PAIS = "pais";
        public static final String COLUMN_COSTO = "costo";

    }
}