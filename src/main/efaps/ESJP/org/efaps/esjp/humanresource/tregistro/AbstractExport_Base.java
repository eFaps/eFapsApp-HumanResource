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

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.efaps.admin.event.Parameter;
import org.efaps.admin.event.Return;
import org.efaps.admin.event.Return.ReturnValues;
import org.efaps.esjp.common.file.FileUtil;
import org.efaps.esjp.erp.util.ERP;
import org.efaps.util.EFapsException;

import com.brsanthu.dataexporter.DataExporter;
import com.brsanthu.dataexporter.LineSeparatorType;
import com.brsanthu.dataexporter.model.RowDetails;
import com.brsanthu.dataexporter.output.csv.CsvExportOptions;
import com.brsanthu.dataexporter.output.csv.CsvWriter;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
public abstract class AbstractExport_Base
{

    /**
     * Windows-1252/WinLatin 1
     */
    public static String CHARSET = "Cp1252";

    public Return execute(final Parameter _parameter)
        throws EFapsException
    {
        final Return ret = new Return();
        final CsvExportOptions exportOption = new CsvExportOptions();
        configureReport(_parameter, exportOption);
        try {
            final File file = new FileUtil().getFile(getFileName(_parameter, getPrefix(_parameter),
                            getSuffix(_parameter)));
            final FileWriterWithEncoding writer = new FileWriterWithEncoding(file, getCharSet(_parameter));
            final Exporter exporter = getExporter(_parameter, exportOption, writer);
            addColumnDefinition(_parameter, exporter);
            buildDataSource(_parameter, exporter);
            ret.put(ReturnValues.VALUES, file);
            ret.put(ReturnValues.TRUE, true);
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ret;
    }

    /**
     * @param _parameter
     * @return
     */
    protected Charset getCharSet(final Parameter _parameter)
    {
        return Charset.forName(AbstractExport_Base.CHARSET);
    }

    /**
     * @param _parameter
     * @return
     */
    protected abstract String getPrefix(final Parameter _parameter)
        throws EFapsException;

    protected abstract String getSuffix(final Parameter _parameter)
        throws EFapsException;

    /**
     * @param _parameter
     * @param _exporter
     */
    public abstract void addColumnDefinition(final Parameter _parameter,
                                             final Exporter _exporter)
        throws EFapsException;

    public abstract void buildDataSource(final Parameter _parameter,
                                         final Exporter _exporter)
        throws EFapsException;

    protected String getFileName(final Parameter _parameter,
                                 final String _prefix,
                                 final String _suffix)
        throws EFapsException
    {
        final String taxnumber = ERP.COMPANYTAX.get();
        return _prefix + taxnumber + "." + _suffix;
    }

    protected void configureReport(final Parameter _parameter,
                                   final CsvExportOptions _exportOption)
    {
        _exportOption.setLineSeparator(LineSeparatorType.WINDOWS);
        _exportOption.setQuote("");
        _exportOption.setDelimiter("|");
        _exportOption.setPrintHeaders(false);
    }

    protected Exporter getExporter(final Parameter _parameter,
                                   final CsvExportOptions _options,
                                   final Writer _output)
    {
        return new Exporter(_parameter, _options, _output);
    }

    public static class Exporter
        extends DataExporter
    {

        /**
         * @param _dataWriter
         */
        protected Exporter(final Parameter _parameter,
                           final CsvExportOptions _options,
                           final Writer _output)
        {
            super(new CsvWriter(_options, _output)
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
}
