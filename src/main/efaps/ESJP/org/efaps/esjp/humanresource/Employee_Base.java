/*
 * Copyright © 2003 - 2024 The eFaps Team (-)
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
 */
package org.efaps.esjp.humanresource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.efaps.admin.datamodel.Classification;
import org.efaps.admin.event.Parameter;
import org.efaps.admin.event.Parameter.ParameterValues;
import org.efaps.admin.event.Return;
import org.efaps.admin.event.Return.ReturnValues;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.admin.ui.AbstractUserInterfaceObject;
import org.efaps.admin.ui.AbstractUserInterfaceObject.TargetMode;
import org.efaps.admin.ui.field.Field;
import org.efaps.ci.CIAdminUser;
import org.efaps.db.AttributeQuery;
import org.efaps.db.CachedMultiPrintQuery;
import org.efaps.db.Delete;
import org.efaps.db.Insert;
import org.efaps.db.Instance;
import org.efaps.db.InstanceQuery;
import org.efaps.db.MultiPrintQuery;
import org.efaps.db.PrintQuery;
import org.efaps.db.QueryBuilder;
import org.efaps.db.SelectBuilder;
import org.efaps.db.Update;
import org.efaps.esjp.ci.CIFormHumanResource;
import org.efaps.esjp.ci.CIHumanResource;
import org.efaps.esjp.ci.CIMsgHumanResource;
import org.efaps.esjp.common.AbstractCommon;
import org.efaps.esjp.common.uiform.Create;
import org.efaps.esjp.common.uiform.Edit;
import org.efaps.esjp.common.uitable.MultiPrint;
import org.efaps.esjp.common.util.InterfaceUtils;
import org.efaps.esjp.humanresource.util.ActivationGroup;
import org.efaps.esjp.ui.rest.provider.ITableProvider;
import org.efaps.esjp.ui.rest.provider.StandardTableProvider;
import org.efaps.util.EFapsException;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 */
@EFapsUUID("6c060a95-0515-4809-ab2f-6834a951e93c")
@EFapsApplication("eFapsApp-HumanResource")
public abstract class Employee_Base
    extends AbstractCommon
    implements ITableProvider
{

    private ITableProvider tableProvider;

    /**
     * Method is executed on create event.
     *
     * @param _parameter Parameter as passed from the eFaps API.
     * @return empty Return
     * @throws EFapsException on error.
     */
    public Return create(final Parameter _parameter)
        throws EFapsException
    {
        final Create create = new Create()
        {

            @Override
            public void connect(final Parameter _parameter,
                                final Instance _instance)
                throws EFapsException
            {
                super.connect(_parameter, _instance);
                Employee_Base.this.connect(_parameter, _instance);
            }
        };
        return create.execute(_parameter);
    }

    /**
     * Sets the employee active.
     *
     * @param _parameter Parameter as passed from the eFaps API.
     * @return empty Return
     * @throws EFapsException on error.
     */
    public Return setActive(final Parameter _parameter)
        throws EFapsException
    {
        final PrintQuery print = new PrintQuery(_parameter.getInstance());
        final SelectBuilder selClassInst = SelectBuilder.get().clazz(CIHumanResource.ClassTR).instance();
        print.addSelect(selClassInst);
        print.execute();

        final Instance classInst = print.getSelect(selClassInst);
        final Update update;
        if (classInst == null || classInst != null && !classInst.isValid()) {
            final Classification clazz = (Classification) CIHumanResource.ClassTR.getType();
            final Insert relInsert = new Insert(clazz.getClassifyRelationType());
            relInsert.add(clazz.getRelLinkAttributeName(), _parameter.getInstance());
            relInsert.add(clazz.getRelTypeAttributeName(), clazz.getId());
            relInsert.execute();

            update = new Insert(CIHumanResource.ClassTR);
            update.add(clazz.getLinkAttributeName(), _parameter.getInstance());
        } else {
            update = new Update(classInst);
        }
        update.add(CIHumanResource.ClassTR.StartDate, _parameter.getParameterValue(
                        CIFormHumanResource.HumanResource_EmployeeSetActiveForm.date.name));
        update.add(CIHumanResource.ClassTR.EndDate, (Object) null);
        update.execute();
        return new Return();
    }

    /**
     * Sets the employee inactive.
     *
     * @param _parameter Parameter as passed from the eFaps API.
     * @return empty Return
     * @throws EFapsException on error.
     */
    public Return setInActive(final Parameter _parameter)
        throws EFapsException
    {
        final PrintQuery print = new PrintQuery(_parameter.getInstance());
        final SelectBuilder selClassInst = SelectBuilder.get().clazz(CIHumanResource.ClassTR).instance();
        print.addSelect(selClassInst);
        print.execute();

        final Instance classInst = print.getSelect(selClassInst);
        final Update update;
        if (classInst == null || classInst != null && !classInst.isValid()) {
            final Classification clazz = (Classification) CIHumanResource.ClassTR.getType();
            final Insert relInsert = new Insert(clazz.getClassifyRelationType());
            relInsert.add(clazz.getRelLinkAttributeName(), _parameter.getInstance());
            relInsert.add(clazz.getRelTypeAttributeName(), clazz.getId());
            relInsert.execute();

            update = new Insert(CIHumanResource.ClassTR);
            update.add(clazz.getLinkAttributeName(), _parameter.getInstance());
        } else {
            update = new Update(classInst);
        }
        update.add(CIHumanResource.ClassTR.EndDate, _parameter.getParameterValue(
                        CIFormHumanResource.HumanResource_EmployeeSetInActiveForm.date.name));
        update.execute();
        return new Return();
    }

    /**
     * @param _parameter Parameter as passed from the eFaps API.
     * @param _instance Instance of the Employee
     * @throws EFapsException on error.
     */
    protected void connect(final Parameter _parameter,
                           final Instance _instance)
        throws EFapsException
    {
        final Instance depInst = Instance.get(_parameter.getParameterValue("department"));
        final QueryBuilder queryBldr = new QueryBuilder(CIHumanResource.Department2EmployeeAdminister);
        queryBldr.addWhereAttrEqValue(CIHumanResource.Department2EmployeeAbstract.EmployeeAbstractLink, _instance);
        final InstanceQuery query = queryBldr.getQuery();
        final List<Instance> list = query.execute();

        if (depInst.isValid()) {
            final Update update;
            if (list.isEmpty()) {
                update = new Insert(CIHumanResource.Department2EmployeeAdminister);
            } else {
                update = new Update(list.get(0));
            }
            update.add(CIHumanResource.Department2EmployeeAbstract.DepartmentAbstractLink, depInst);
            update.add(CIHumanResource.Department2EmployeeAbstract.EmployeeAbstractLink, _instance);
            update.execute();
        } else if (!list.isEmpty()) {
            for (final Instance ins : list) {
                new Delete(ins).execute();
            }
        }

        final Instance locInst = Instance.get(_parameter.getParameterValue("location"));
        if (locInst.isValid()) {
            final QueryBuilder queryBldr2 = new QueryBuilder(CIHumanResource.Employee2LocationOffice);
            queryBldr2.addWhereAttrEqValue(CIHumanResource.Employee2LocationOffice.EmployeeLink, _instance);
            final InstanceQuery query2 = queryBldr2.getQuery();
            final List<Instance> list2 = query2.execute();

            final Update update;
            if (list2.isEmpty()) {
                update = new Insert(CIHumanResource.Employee2LocationOffice);
            } else {
                update = new Update(list2.get(0));
            }
            update.add(CIHumanResource.Employee2LocationOffice.EmployeeLink, _instance);
            update.add(CIHumanResource.Employee2LocationOffice.LocationOfficeLink, locInst);
            update.execute();
            if (!list2.isEmpty()) {
                for (final Instance ins : list2) {
                    new Delete(ins).execute();
                }
            }
        }
    }

    /**
     * @param _parameter Parameter as passed from the eFaps API.
     * @return empty Return
     * @throws EFapsException on error.
     */
    public Return edit(final Parameter _parameter)
        throws EFapsException
    {
        final Edit edit = new Edit()
        {

            @Override
            protected void add2MainUpdate(final Parameter _parameter,
                                          final Update _update)
                throws EFapsException
            {
                super.add2MainUpdate(_parameter, _update);
                connect(_parameter, _update.getInstance());
            }
        };
        return edit.execute(_parameter);
    }

    /**
     * Method is executed on an auto-complete event to present a drop-down with
     * employees.
     *
     * @param _parameter Parameter as passed from the eFaps API.
     * @return list of map used for an auto-complete event.
     * @throws EFapsException on error.
     */
    public Return autoComplete4Employee(final Parameter _parameter)
        throws EFapsException
    {
        final String input = (String) _parameter.get(ParameterValues.OTHERS);
        final List<Map<String, String>> list = new ArrayList<>();

        if (input.length() > 0) {
            final String key = containsProperty(_parameter, "Key") ? getProperty(_parameter, "Key") : "OID";

            final QueryBuilder queryBldr;
            if (containsProperty(_parameter, "Type")) {
                queryBldr = getQueryBldrFromProperties(_parameter);
            } else {
                queryBldr = new QueryBuilder(CIHumanResource.EmployeeAbstract);
            }

            final boolean nameSearch = Character.isDigit(input.charAt(0));
            final Map<String, Map<String, String>> tmpMap = new TreeMap<>();

            final QueryBuilder attQueryBldr = new QueryBuilder(CIHumanResource.EmployeeAbstract);
            if (nameSearch) {
                attQueryBldr.addWhereAttrMatchValue(CIHumanResource.EmployeeAbstract.Number, input + "*");
            } else {
                attQueryBldr.setOr(true);
                attQueryBldr.addWhereAttrMatchValue(CIHumanResource.EmployeeAbstract.LastName, input + "*")
                                .setIgnoreCase(true);
                attQueryBldr.addWhereAttrMatchValue(CIHumanResource.EmployeeAbstract.FirstName, input + "*")
                                .setIgnoreCase(true);
                attQueryBldr.addWhereAttrMatchValue(CIHumanResource.EmployeeAbstract.SecondLastName, input + "*")
                                .setIgnoreCase(true);
            }
            queryBldr.addWhereAttrInQuery(CIHumanResource.EmployeeAbstract.ID,
                            attQueryBldr.getAttributeQuery(CIHumanResource.EmployeeAbstract.ID));

            InterfaceUtils.addMaxResult2QueryBuilder4AutoComplete(_parameter, queryBldr);

            add2QueryBldr(_parameter, queryBldr);

            final MultiPrintQuery multi = queryBldr.getPrint();
            multi.addAttribute(CIHumanResource.EmployeeAbstract.Number,
                            CIHumanResource.EmployeeAbstract.FirstName,
                            CIHumanResource.EmployeeAbstract.LastName,
                            CIHumanResource.EmployeeAbstract.SecondLastName);
            multi.addAttribute(key);
            multi.execute();
            while (multi.next()) {
                final String number = multi.<String>getAttribute(CIHumanResource.EmployeeAbstract.Number);
                final String firstname = multi.<String>getAttribute(CIHumanResource.EmployeeAbstract.FirstName);
                final String lastname = multi.<String>getAttribute(CIHumanResource.EmployeeAbstract.LastName);
                final String secondLastname = multi
                                .<String>getAttribute(CIHumanResource.EmployeeAbstract.SecondLastName);
                final String dataemployee = lastname + (secondLastname.isEmpty() ? ", " : " " + secondLastname + ", ")
                                + firstname;
                final String choice = nameSearch ? number + "- " + dataemployee : dataemployee + " - " + number;
                final Map<String, String> map = new HashMap<>();
                map.put("eFapsAutoCompleteKEY", multi.getAttribute(key).toString());
                map.put("eFapsAutoCompleteCHOICE", choice);
                tmpMap.put(choice, map);
            }
            list.addAll(tmpMap.values());
        }
        final Return retVal = new Return();
        retVal.put(ReturnValues.VALUES, list);
        return retVal;
    }

    /**
     * Add2 query bldr.
     *
     * @param _parameter Parameter as passed by the eFaps API
     * @param _queryBldr the query bldr
     * @throws EFapsException on error
     */
    protected void add2QueryBldr(final Parameter _parameter,
                                 final QueryBuilder _queryBldr)
        throws EFapsException
    {
    }

    /**
     * @param _parameter Parameter as passed by the eFasp API
     * @return maplist for fielupdate
     * @throws EFapsException on error
     */
    public Return updateField4Employee(final Parameter _parameter)
        throws EFapsException
    {
        final Return ret = new Return();
        final List<Map<String, Object>> list = new ArrayList<>();
        final Map<String, Object> map = new HashMap<>();
        final String depField = getProperty(_parameter, "HR_DepartmentFieldName", "department");
        final String employeeField = getProperty(_parameter, "HR_EmployeeFieldName", "employee");

        final String employee = _parameter.getParameterValue(employeeField);
        if (employee != null && !employee.isEmpty()) {
            final Instance employeeInst = Instance.get(employee);
            if (employeeInst.isValid()) {
                final QueryBuilder attrQueryBldr = new QueryBuilder(CIHumanResource.Department2EmployeeAbstract);
                attrQueryBldr.addWhereAttrEqValue(CIHumanResource.Department2EmployeeAbstract.EmployeeAbstractLink,
                                employeeInst);

                final QueryBuilder queryBldr = new QueryBuilder(CIHumanResource.Department);
                queryBldr.addWhereAttrInQuery(CIHumanResource.Department.ID, attrQueryBldr.getAttributeQuery(
                                CIHumanResource.Department2EmployeeAbstract.DepartmentAbstractLink));
                final InstanceQuery query = queryBldr.getQuery();
                query.execute();
                if (query.next()) {
                    map.put(depField, query.getCurrentValue().getOid());
                }
            }
        }
        list.add(map);
        ret.put(ReturnValues.VALUES, list);
        return ret;
    }

    /**
     * Method for auto-complete user person (if any a case non-existing in DB.
     *
     * @param _parameter Parameter as passed from the eFaps API.
     * @return ret witch values
     * @throws EFapsException on error.
     */
    public Return autoComplete4Person(final Parameter _parameter)
        throws EFapsException
    {
        final String input = (String) _parameter.get(ParameterValues.OTHERS);
        final List<Map<String, String>> list = new ArrayList<>();
        if (input.length() > 0) {
            final Map<String, Map<String, String>> tmpMap = new TreeMap<>();
            final QueryBuilder queryBldr = new QueryBuilder(CIAdminUser.Person);
            queryBldr.addWhereAttrMatchValue(CIAdminUser.Person.Name, input + "*").setIgnoreCase(true);

            final QueryBuilder attrQueryBldr = new QueryBuilder(CIHumanResource.Employee);
            attrQueryBldr.addWhereAttrNotIsNull(CIHumanResource.Employee.UserPerson);

            final AttributeQuery attrQuery = attrQueryBldr.getAttributeQuery(CIHumanResource.Employee.UserPerson);
            queryBldr.addWhereAttrNotInQuery(CIAdminUser.Person.ID, attrQuery);
            final MultiPrintQuery multi = queryBldr.getPrint();

            multi.addAttribute(CIAdminUser.Person.ID, CIAdminUser.Person.Name,
                            CIAdminUser.Person.FirstName, CIAdminUser.Person.LastName);
            multi.execute();
            while (multi.next()) {
                final long id = multi.<Long>getAttribute(CIAdminUser.Person.ID);
                final String name = multi.<String>getAttribute(CIAdminUser.Person.Name);
                final String firstname = multi.<String>getAttribute(CIAdminUser.Person.FirstName);
                final String lastname = multi.<String>getAttribute(CIAdminUser.Person.LastName);
                final String fullName = lastname + ", " + firstname;
                final String choice = name + " - " + fullName;
                final Map<String, String> map = new HashMap<>();
                map.put("eFapsAutoCompleteKEY", String.valueOf(id));
                map.put("eFapsAutoCompleteCHOICE", choice);
                map.put("eFapsAutoCompleteVALUE", name);
                tmpMap.put(choice, map);
            }
            list.addAll(tmpMap.values());
        }
        final Return retVal = new Return();
        retVal.put(ReturnValues.VALUES, list);
        return retVal;
    }

    /**
     * Gets the age field value u i2 view.
     *
     * @param _parameter Parameter as passed by the eFaps API
     * @return the age field value u i2 view
     * @throws EFapsException on error
     */
    public Return getAgeFieldValueUI2View(final Parameter _parameter)
        throws EFapsException
    {
        final Return retVal = new Return();
        final Instance employeeInst = _parameter.getInstance();
        if (employeeInst != null && employeeInst.isValid()) {
            final PrintQuery print = new PrintQuery(employeeInst);
            print.addAttribute(CIHumanResource.Employee.BirthDate);
            print.execute();
            final DateTime birthDate = print.<DateTime>getAttribute(CIHumanResource.Employee.BirthDate);
            final DateTime todayDate = new DateTime();
            final Period p = new Period(birthDate, todayDate, PeriodType.years());
            final int resultado = p.getYears();
            retVal.put(ReturnValues.VALUES, resultado + "");
        }

        return retVal;
    }

    /**
     * Multi print4 employee.
     *
     * @param _parameter Parameter as passed by the eFaps API
     * @return the return
     * @throws EFapsException on error
     */
    public Return multiPrint4Employee(final Parameter _parameter)
        throws EFapsException
    {
        final MultiPrint multi = new MultiPrint()
        {

            @Override
            protected void add2QueryBldr(final Parameter _parameter,
                                         final QueryBuilder _queryBldr)
                throws EFapsException
            {
                super.add2QueryBldr(_parameter, _queryBldr);
                add2QueryBldr4EmployeeWithActivation(_parameter, _queryBldr);
            }
        };
        return multi.execute(_parameter);
    }

    /**
     * Add2 query bldr4 employee with activation.
     *
     * @param _parameter Parameter as passed by the eFaps API
     * @param _queryBldr the query bldr
     * @throws EFapsException on error
     */
    public void add2QueryBldr4EmployeeWithActivation(final Parameter _parameter,
                                                     final QueryBuilder _queryBldr)
        throws EFapsException
    {
        final Map<Integer, String> activations = analyseProperty(_parameter, "Activation");
        if (!activations.isEmpty()) {
            final List<ActivationGroup> activationList = new ArrayList<>();
            for (final String activationStr : activations.values()) {
                final ActivationGroup activation = ActivationGroup.valueOf(activationStr);
                activationList.add(activation);
            }
            _queryBldr.addWhereAttrEqValue(CIHumanResource.Employee.Activation, activationList.toArray());
        }
    }

    @Override
    public ITableProvider init(AbstractUserInterfaceObject cmd,
                               List<Field> fields,
                               Map<String, String> properties,
                               TargetMode targetMode,
                               String oid)
        throws EFapsException
    {
        tableProvider = new StandardTableProvider();
        return tableProvider.init(cmd, fields, properties, targetMode, oid);
    }

    @Override
    public Collection<Map<String, ?>> getValues()
        throws EFapsException
    {
        // Should this Activation thing be done here also?
        return tableProvider.getValues();
    }

    /**
     * Gets the employee assigned2 contact.
     *
     * @param _parameter Parameter as passed by the eFaps API
     * @param _contactInstance the contact instance
     * @return the employee assigned2 contact
     * @throws EFapsException on error
     */
    protected static String getEmployeeAssigned2Contact(final Parameter _parameter,
                                                        final Instance _contactInstance)
        throws EFapsException
    {
        final StringBuilder ret = new StringBuilder();
        final QueryBuilder attrQueryBldr = new QueryBuilder(CIHumanResource.Employee2Contact);
        attrQueryBldr.addWhereAttrEqValue(CIHumanResource.Employee2Contact.ToLink, _contactInstance);

        final QueryBuilder queryBldr = new QueryBuilder(CIHumanResource.Employee);
        queryBldr.addWhereAttrInQuery(CIHumanResource.Employee.ID, attrQueryBldr.getAttributeQuery(
                        CIHumanResource.Employee2Contact.FromLink));
        final CachedMultiPrintQuery multi = queryBldr.getCachedPrint4Request();
        multi.addMsgPhrase(CIMsgHumanResource.EmployeeMsgPhrase);
        multi.execute();
        while (multi.next()) {
            if (ret.length() > 0) {
                ret.append("\n");
            }
            ret.append(multi.getMsgPhrase(CIMsgHumanResource.EmployeeMsgPhrase));
        }
        return ret.toString();
    }
}
