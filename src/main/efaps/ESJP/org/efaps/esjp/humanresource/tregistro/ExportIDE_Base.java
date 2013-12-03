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
import org.efaps.db.MultiPrintQuery;
import org.efaps.db.QueryBuilder;
import org.efaps.db.SelectBuilder;
import org.efaps.esjp.ci.CIHumanResource;
import org.efaps.esjp.data.columns.export.FrmtColumn;
import org.efaps.esjp.data.columns.export.FrmtDateTimeColumn;
import org.efaps.util.EFapsException;
import org.joda.time.DateTime;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
public abstract class ExportIDE_Base
    extends AbstractExport
{

    public static final String PREFIX = "RP_";

    public static final String SUFFIX = "ide";


    @Override
    protected String getPrefix(final Parameter _parameter)
        throws EFapsException
    {
        return ExportIDE_Base.PREFIX;
    }

    @Override
    protected String getSuffix(final Parameter _parameter)
        throws EFapsException
    {
        return ExportIDE_Base.SUFFIX;
    }


    @Override
    public void addColumnDefinition(final Parameter _parameter,
                                    final Exporter _exporter)
    {
        _exporter.addColumns(new FrmtColumn("DOIType", 2));
        _exporter.addColumns(new FrmtColumn("Number").setMaxWidth(15));
        _exporter.addColumns(new FrmtColumn("EmitterCountry", 3));
        _exporter.addColumns(new FrmtDateTimeColumn("BirthDate", 10, "dd/MM/yyyy"));
        _exporter.addColumns(new FrmtColumn("LastName", 40));
        _exporter.addColumns(new FrmtColumn("SecondLastName", 40));
        _exporter.addColumns(new FrmtColumn("FirstName", 40));
        _exporter.addColumns(new FrmtColumn("Sex", 1));
        _exporter.addColumns(new FrmtColumn("Nationality", 1));
        _exporter.addColumns(new FrmtColumn("10", 3));
        _exporter.addColumns(new FrmtColumn("11", 9));
        _exporter.addColumns(new FrmtColumn("12", 50));
        _exporter.addColumns(new FrmtColumn("13", 2));
        _exporter.addColumns(new FrmtColumn("14", 20));
        _exporter.addColumns(new FrmtColumn("15", 4));
        _exporter.addColumns(new FrmtColumn("16", 4));
        _exporter.addColumns(new FrmtColumn("17", 4));
        _exporter.addColumns(new FrmtColumn("18", 4));
        _exporter.addColumns(new FrmtColumn("19", 4));
        _exporter.addColumns(new FrmtColumn("20", 4));
        _exporter.addColumns(new FrmtColumn("21", 4));
        _exporter.addColumns(new FrmtColumn("22", 4));
        _exporter.addColumns(new FrmtColumn("23", 2));
        _exporter.addColumns(new FrmtColumn("24", 20));
        _exporter.addColumns(new FrmtColumn("25", 40));
        _exporter.addColumns(new FrmtColumn("26", 6));
        _exporter.addColumns(new FrmtColumn("27", 2));
        _exporter.addColumns(new FrmtColumn("28", 20));
        _exporter.addColumns(new FrmtColumn("29", 4));
        _exporter.addColumns(new FrmtColumn("30", 4));
        _exporter.addColumns(new FrmtColumn("31", 4));
        _exporter.addColumns(new FrmtColumn("32", 4));
        _exporter.addColumns(new FrmtColumn("33", 4));
        _exporter.addColumns(new FrmtColumn("34", 4));
        _exporter.addColumns(new FrmtColumn("35", 4));
        _exporter.addColumns(new FrmtColumn("36", 4));
        _exporter.addColumns(new FrmtColumn("37", 2));
        _exporter.addColumns(new FrmtColumn("38", 20));
        _exporter.addColumns(new FrmtColumn("39", 40));
        _exporter.addColumns(new FrmtColumn("40", 6));
        _exporter.addColumns(new FrmtColumn("1", 6));
    }

    @Override
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
}
