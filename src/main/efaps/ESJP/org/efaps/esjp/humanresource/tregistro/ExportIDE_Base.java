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

import org.efaps.admin.event.Parameter;
import org.efaps.admin.event.Return;
import org.efaps.db.MultiPrintQuery;
import org.efaps.db.QueryBuilder;
import org.efaps.db.SelectBuilder;
import org.efaps.esjp.ci.CIHumanResource;
import org.efaps.esjp.data.columns.export.FrmtColumn;
import org.efaps.esjp.data.columns.export.FrmtDateTimeColumn;
import org.efaps.util.EFapsException;
import org.joda.time.DateTime;

import com.brsanthu.dataexporter.DataExporter;
import com.brsanthu.dataexporter.LineSeparatorType;
import com.brsanthu.dataexporter.model.AlignType;
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
    public Return execute(final Parameter _parameter)
        throws EFapsException
    {
        final CsvExportOptions exportOption = new CsvExportOptions();
        configureReport(_parameter, exportOption);
        final Exporter exporter = new Exporter(exportOption);
        addColumnDefinition(_parameter, exporter);
        buildDataSource(_parameter, exporter);

        return new Return();
    }

    protected void configureReport(final Parameter _parameter,
                                   final CsvExportOptions _exportOption) {
        _exportOption.setLineSeparator(LineSeparatorType.WINDOWS);
        _exportOption.setQuote("");
        _exportOption.setDelimiter("|");
        _exportOption.setPrintHeaders(false);
    }

    protected void addColumnDefinition(final Parameter _parameter,
                                       final Exporter _exporter)
    {
        _exporter.addColumns(new StringColumn("DOIType", 2, AlignType.TOP_RIGHT));
        _exporter.addColumns(new FrmtColumn("Number").setMaxWidth(15));
        _exporter.addColumns(new StringColumn("EmitterCountry", 3));
        _exporter.addColumns(new FrmtDateTimeColumn("BirthDate", 10, "dd/MM/yyyy"));

        _exporter.addColumns(new FrmtColumn("LastName", 40));
        _exporter.addColumns(new FrmtColumn("SecondLastName", 40));
        _exporter.addColumns(new FrmtColumn("FirstName", 40));
        _exporter.addColumns(new StringColumn("Sex", 1));
        _exporter.addColumns(new StringColumn("Nationality", 1));
        _exporter.addColumns(new StringColumn("10", 3));
        _exporter.addColumns(new StringColumn("11", 9));
        _exporter.addColumns(new StringColumn("12", 50));
        _exporter.addColumns(new StringColumn("13", 2));
        _exporter.addColumns(new StringColumn("14", 20));
        _exporter.addColumns(new StringColumn("15", 4));
        _exporter.addColumns(new StringColumn("16", 4));
        _exporter.addColumns(new StringColumn("17", 4));
        _exporter.addColumns(new StringColumn("18", 4));
        _exporter.addColumns(new StringColumn("19", 4));
        _exporter.addColumns(new StringColumn("20", 4));
        _exporter.addColumns(new StringColumn("21", 4));
        _exporter.addColumns(new StringColumn("22", 4));
        _exporter.addColumns(new StringColumn("23", 2));
        _exporter.addColumns(new StringColumn("24", 20));
        _exporter.addColumns(new StringColumn("25", 40));
        _exporter.addColumns(new StringColumn("26", 6));
        _exporter.addColumns(new StringColumn("27", 2));
        _exporter.addColumns(new StringColumn("28", 20));
        _exporter.addColumns(new StringColumn("29", 4));
        _exporter.addColumns(new StringColumn("30", 4));
        _exporter.addColumns(new StringColumn("31", 4));
        _exporter.addColumns(new StringColumn("32", 4));
        _exporter.addColumns(new StringColumn("33", 4));
        _exporter.addColumns(new StringColumn("34", 4));
        _exporter.addColumns(new StringColumn("35", 4));
        _exporter.addColumns(new StringColumn("36", 4));
        _exporter.addColumns(new StringColumn("37", 2));
        _exporter.addColumns(new StringColumn("38", 20));
        _exporter.addColumns(new StringColumn("39", 40));
        _exporter.addColumns(new StringColumn("40", 6));
        _exporter.addColumns(new StringColumn("1", 6));
    }

    public void buildDataSource(final Parameter _parameter,
                                final Exporter _exporter)
        throws EFapsException
    {
        final QueryBuilder queryBldr = new QueryBuilder(CIHumanResource.Employee);
        final MultiPrintQuery multi = queryBldr.getPrint();
        multi.addAttribute(CIHumanResource.Employee.FirstName,
                        CIHumanResource.Employee.LastName,
                        CIHumanResource.Employee.SecondLastName,
                        CIHumanResource.Employee.BirthDate,
                        CIHumanResource.Employee.Number);
        final SelectBuilder selNumberType = new SelectBuilder().linkto(CIHumanResource.Employee.NumberTypeLink)
                        .attribute(CIHumanResource.AttributeDefinitionDOIType.MappingKey);
        final SelectBuilder selEmitterCount = new SelectBuilder().linkto(CIHumanResource.Employee.EmitterCountryLink)
                        .attribute(CIHumanResource.AttributeDefinitionEmitterCountry.MappingKey);
        final SelectBuilder selSex = new SelectBuilder().linkto(CIHumanResource.Employee.SexLink)
                        .attribute(CIHumanResource.AttributeDefinitionSex.MappingKey);
        final SelectBuilder selNationality = new SelectBuilder().linkto(CIHumanResource.Employee.NationalityLink)
                        .attribute(CIHumanResource.AttributeDefinitionNationality.MappingKey);
        multi.addSelect(selEmitterCount, selNationality, selNumberType, selSex);
        multi.execute();
        while (multi.next()) {
            final String number = multi.<String>getAttribute(CIHumanResource.Employee.Number);
            final String firstName = multi.<String>getAttribute(CIHumanResource.Employee.FirstName);
            final String lastName = multi.<String>getAttribute(CIHumanResource.Employee.LastName);
            final String secLastName = multi.<String>getAttribute(CIHumanResource.Employee.SecondLastName);
            final DateTime birthDate = multi.<DateTime>getAttribute(CIHumanResource.Employee.BirthDate);
            final String numberType = multi.<String>getSelect(selNumberType);
            final String emitterCount = multi.<String>getSelect(selEmitterCount);
            final String sex = multi.<String>getSelect(selSex);
            final String nation = multi.<String>getSelect(selNationality);

            _exporter.addRow(numberType, number, emitterCount, birthDate, lastName, secLastName, firstName, sex, nation);
        }
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
}
