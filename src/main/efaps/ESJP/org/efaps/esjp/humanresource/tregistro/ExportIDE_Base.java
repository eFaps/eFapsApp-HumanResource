/*
 * Copyright 2003 - 2013 The eFaps Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Revision:        $Rev$
 * Last Changed:    $Date$
 * Last Changed By: $Author$
 */


package org.efaps.esjp.humanresource.tregistro;

import org.joda.time.DateTime;

import com.brsanthu.dataexporter.DataExporter;
import com.brsanthu.dataexporter.LineSeparatorType;
import com.brsanthu.dataexporter.model.AlignType;
import com.brsanthu.dataexporter.model.CellDetails;
import com.brsanthu.dataexporter.model.RowDetails;
import com.brsanthu.dataexporter.model.StringColumn;
import com.brsanthu.dataexporter.output.csv.CsvExportOptions;
import com.brsanthu.dataexporter.output.csv.CsvWriter;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
public abstract class ExportIDE_Base
{

    public static void main(final String[] args)
    {
        ExportIDE_Base.test();
    }

    public static void test()
    {
        final CsvExportOptions exportOption = new CsvExportOptions();
        exportOption.setLineSeparator(LineSeparatorType.WINDOWS);
        exportOption.setQuote("");
        exportOption.setDelimiter("|");
        exportOption.setPrintHeaders(false);

        final Exporter exporter = new Exporter(exportOption);
        exporter.addColumns(new StringColumn("DOIType", 2, AlignType.TOP_RIGHT));
        exporter.addColumns(new FrmtColumn("Number").setMaxWidth(15));
        exporter.addColumns(new StringColumn("EmitterCountry", 3));
        exporter.addColumns(new StringColumn("BirthDate", 3));


        exporter.addColumns(new StringColumn("LastName", 40));
        exporter.addColumns(new StringColumn("SecondLastName", 40));
        exporter.addColumns(new StringColumn("FirstName", 40));
        exporter.addColumns(new StringColumn("Sex", 1));
        exporter.addColumns(new StringColumn("Nationality", 1));
        exporter.addColumns(new StringColumn("10", 3));
        exporter.addColumns(new StringColumn("11", 9));
        exporter.addColumns(new StringColumn("12", 50));
        exporter.addColumns(new StringColumn("13", 2));
        exporter.addColumns(new StringColumn("14", 20));
        exporter.addColumns(new StringColumn("15", 4));
        exporter.addColumns(new StringColumn("16", 4));
        exporter.addColumns(new StringColumn("17", 4));
        exporter.addColumns(new StringColumn("18", 4));
        exporter.addColumns(new StringColumn("19", 4));
        exporter.addColumns(new StringColumn("20", 4));
        exporter.addColumns(new StringColumn("21", 4));
        exporter.addColumns(new StringColumn("22", 4));
        exporter.addColumns(new StringColumn("23", 2));
        exporter.addColumns(new StringColumn("24", 20));
        exporter.addColumns(new StringColumn("25", 40));
        exporter.addColumns(new StringColumn("26", 6));
        exporter.addColumns(new StringColumn("27", 2));
        exporter.addColumns(new StringColumn("28", 20));
        exporter.addColumns(new StringColumn("29", 4));
        exporter.addColumns(new StringColumn("30", 4));
        exporter.addColumns(new StringColumn("31", 4));
        exporter.addColumns(new StringColumn("32", 4));
        exporter.addColumns(new StringColumn("33", 4));
        exporter.addColumns(new StringColumn("34", 4));
        exporter.addColumns(new StringColumn("35", 4));
        exporter.addColumns(new StringColumn("36", 4));
        exporter.addColumns(new StringColumn("37", 2));
        exporter.addColumns(new StringColumn("38", 20));
        exporter.addColumns(new StringColumn("39", 40));
        exporter.addColumns(new StringColumn("40", 6));
        exporter.addColumns(new StringColumn("1", 6));





        exporter.addRow("04","000447393","104", new DateTime(),"Moxter", "", "Jan","1","103");
        exporter.addRow("02","98247393","333");
        exporter.addRow("01","448595","");
        exporter.addRow("1","8595156984562135815121","");
    }


    public static class Exporter
        extends DataExporter
    {

        /**
         * @param _dataWriter
         */
        protected Exporter(final CsvExportOptions _options)
        {
            super(new CsvWriter(_options, System.out)
            {
                @Override
                public void beforeRow(final RowDetails rowDetails)
                {
                    if (rowDetails.getRowIndex() > 0) {
                        println();
                    }
                }
            });
        }
    }

    public static class FrmtColumn
        extends StringColumn
    {

        /**
         * @param _name
         * @param _width
         * @param _align
         */
        public FrmtColumn(final String _name)
        {
            super(_name);
        }

        public FrmtColumn(final String _name,
                          final int _maxWidth)
        {
            super(_name, _maxWidth);
        }

        public FrmtColumn setMaxWidth(final int _maxWidth) {
            super.setWidth(_maxWidth);
            return this;
        }

        @Override
        public String format(final CellDetails _cellDetails) {
            String ret;
            if (_cellDetails.getCellValue() == null) {
                ret = "";
            } else {
                String tmp = _cellDetails.getCellValue().toString();
                if (tmp.length() > getWidth()) {
                    //LOG
                    tmp = tmp.substring(0, getWidth());
                }
                ret = tmp;
            }
            return ret;
        }
    }
}
