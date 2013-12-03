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
import org.efaps.util.EFapsException;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
public abstract class ExportTRA_Base
    extends AbstractExport
{

    public static final String PREFIX = "RP_";

    public static final String SUFFIX = "tra";

    @Override
    protected String getPrefix(final Parameter _parameter)
        throws EFapsException
    {
        return ExportTRA_Base.PREFIX;
    }

    @Override
    protected String getSuffix(final Parameter _parameter)
        throws EFapsException
    {
        return ExportTRA_Base.SUFFIX;
    }

    @Override
    public void addColumnDefinition(final Parameter _parameter,
                                    final Exporter _exporter)
    {
        _exporter.addColumns(new FrmtColumn("DOIType", 2));//1
        _exporter.addColumns(new FrmtColumn("Number").setMaxWidth(15));//2
        _exporter.addColumns(new FrmtColumn("EmitterCountry", 3));//3
        _exporter.addColumns(new FrmtColumn("RegimeLink", 2));//4
        _exporter.addColumns(new FrmtColumn("EducationLink", 2));//5
        _exporter.addColumns(new FrmtColumn("OccupationLink", 6));//6
        _exporter.addColumns(new FrmtColumn("Disability", 1));//7
        _exporter.addColumns(new FrmtColumn("CUSPP", 12));//8
        _exporter.addColumns(new FrmtColumn("SCTR", 1));//9
        _exporter.addColumns(new FrmtColumn("ContractLink", 2));//10
        _exporter.addColumns(new FrmtColumn("Cumulative", 1));//11
        _exporter.addColumns(new FrmtColumn("Maximum", 1));//12
        _exporter.addColumns(new FrmtColumn("Nightly", 1));//13
        _exporter.addColumns(new FrmtColumn("Unionized", 1));//14
        _exporter.addColumns(new FrmtColumn("PeriodicityLink", 1));//15
        _exporter.addColumns(new FrmtColumn("Remuneration", 7));//16 - 7.2
        _exporter.addColumns(new FrmtColumn("Situacion", 2));//17
        _exporter.addColumns(new FrmtColumn("Renta5", 1));//18
        _exporter.addColumns(new FrmtColumn("SpecialSituacion", 7));//19
        _exporter.addColumns(new FrmtColumn("PaymentLink", 1));//20
        _exporter.addColumns(new FrmtColumn("OccupationCatLink", 2));//21
        _exporter.addColumns(new FrmtColumn("Convenio", 2));//22
        _exporter.addColumns(new FrmtColumn("RUC", 2));//23
    }

    @Override
    public void buildDataSource(final Parameter _parameter,
                                final Exporter _exporter)
        throws EFapsException
    {
        final QueryBuilder queryBldr = new QueryBuilder(CIHumanResource.Employee);
        final MultiPrintQuery multi = queryBldr.getPrint();
        multi.addAttribute(CIHumanResource.Employee.Number);
        final SelectBuilder selNumberType = new SelectBuilder().linkto(CIHumanResource.Employee.NumberTypeLink)
                        .attribute(CIHumanResource.AttributeDefinitionDOIType.MappingKey);
        final SelectBuilder selEmitterCount = new SelectBuilder().linkto(CIHumanResource.Employee.EmitterCountryLink)
                        .attribute(CIHumanResource.AttributeDefinitionEmitterCountry.MappingKey);
        final SelectBuilder selRegime = new SelectBuilder().clazz(CIHumanResource.ClassTR_Labor)
                        .linkto(CIHumanResource.ClassTR_Labor.RegimeLink)
                        .attribute(CIHumanResource.AttributeDefinitionLaborRegime.MappingKey);
        final SelectBuilder selEducation = new SelectBuilder().clazz(CIHumanResource.ClassTR_Labor)
                        .linkto(CIHumanResource.ClassTR_Labor.EducationLink)
                        .attribute(CIHumanResource.AttributeDefinitionEducation.MappingKey);
        final SelectBuilder selOccupation = new SelectBuilder().clazz(CIHumanResource.ClassTR_Labor)
                        .linkto(CIHumanResource.ClassTR_Labor.OccupationLink)
                        .attribute(CIHumanResource.AttributeDefinitionOccupation.MappingKey);
        final SelectBuilder selDisability = new SelectBuilder().clazz(CIHumanResource.ClassTR_Labor)
                        .attribute(CIHumanResource.ClassTR_Labor.Disability);
        final SelectBuilder selCUSPP = new SelectBuilder().clazz(CIHumanResource.ClassTR_Health)
                        .attribute(CIHumanResource.ClassTR_Health.CUSPP);
        final SelectBuilder selContract = new SelectBuilder().clazz(CIHumanResource.ClassTR_Labor)
                        .linkto(CIHumanResource.ClassTR_Labor.ContractLink)
                        .attribute(CIHumanResource.AttributeDefinitionContract.MappingKey);
        final SelectBuilder selCumulative = new SelectBuilder().clazz(CIHumanResource.ClassTR_Labor)
                        .attribute(CIHumanResource.ClassTR_Labor.Cumulative);
        final SelectBuilder selMaximum = new SelectBuilder().clazz(CIHumanResource.ClassTR_Labor)
                        .attribute(CIHumanResource.ClassTR_Labor.Maximum);
        final SelectBuilder selNightly = new SelectBuilder().clazz(CIHumanResource.ClassTR_Labor)
                        .attribute(CIHumanResource.ClassTR_Labor.Nightly);
        final SelectBuilder selUnionized = new SelectBuilder().clazz(CIHumanResource.ClassTR_Labor)
                        .attribute(CIHumanResource.ClassTR_Labor.Unionized);
        final SelectBuilder selPeriodicity = new SelectBuilder().clazz(CIHumanResource.ClassTR_Labor)
                        .linkto(CIHumanResource.ClassTR_Labor.PeriodicityLink)
                        .attribute(CIHumanResource.AttributeDefinitionPeriodicity.MappingKey);
        final SelectBuilder selRemuneration = new SelectBuilder().clazz(CIHumanResource.ClassTR_Labor)
                        .attribute(CIHumanResource.ClassTR_Labor.Remuneration);
        final SelectBuilder selSpecialSituacion = new SelectBuilder().clazz(CIHumanResource.ClassTR_Labor)
                        .attribute(CIHumanResource.ClassTR_Labor.SpecialSituacion);
        final SelectBuilder selPayment = new SelectBuilder().clazz(CIHumanResource.ClassTR_Labor)
                        .linkto(CIHumanResource.ClassTR_Labor.PaymentLink)
                        .attribute(CIHumanResource.AttributeDefinitionPayment.MappingKey);
        final SelectBuilder selOccupationCat = new SelectBuilder().clazz(CIHumanResource.ClassTR_Labor)
                        .linkto(CIHumanResource.ClassTR_Labor.OccupationCatLink)
                        .attribute(CIHumanResource.AttributeDefinitionPayment.MappingKey);



        multi.addSelect(selEmitterCount, selNumberType, selRegime, selEducation, selOccupation, selDisability,
                        selCUSPP, selContract, selCumulative, selMaximum, selNightly, selUnionized, selPeriodicity,
                        selRemuneration, selSpecialSituacion, selPayment, selOccupationCat);
        multi.execute();
        while (multi.next()) {
            final String number = multi.<String>getAttribute(CIHumanResource.Employee.Number);
            final String numberType = multi.<String>getSelect(selNumberType);
            final String emitterCount = multi.<String>getSelect(selEmitterCount);
            final String regime = multi.<String>getSelect(selRegime);
            final String education = multi.<String>getSelect(selEducation);
            final String occupation = multi.<String>getSelect(selOccupation);
            final Object disability = multi.<Object>getSelect(selDisability);
            final String cUSPP = multi.<String>getSelect(selCUSPP);
            final String contract = multi.<String>getSelect(selContract);
            final Object cumulative = multi.<Object>getSelect(selCumulative);
            final Object maximum = multi.<Object>getSelect(selMaximum);
            final Object nightly = multi.<Object>getSelect(selNightly);
            final Object unionized = multi.<Object>getSelect(selUnionized);
            final Object periodicity = multi.<Object>getSelect(selPeriodicity);
            final Object remuneration = multi.<Object>getSelect(selRemuneration);
            final Object specialSituacion = multi.<Object>getSelect(selSpecialSituacion);
            final Object payment = multi.<Object>getSelect(selPayment);
            final Object occupationCat = multi.<Object>getSelect(selOccupationCat);

            _exporter.addRow(numberType, number, emitterCount, regime, education, occupation, disability, cUSPP,
                            null, contract, cumulative, maximum, nightly, unionized, periodicity, remuneration,
                            null, null, specialSituacion, payment, occupationCat);
        }
    }
}
