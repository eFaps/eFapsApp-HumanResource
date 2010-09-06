/*
  * Copyright 2003 - 2010 The eFaps Team
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

package org.efaps.esjp.humanresource;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.efaps.admin.event.Parameter;
import org.efaps.admin.event.Parameter.ParameterValues;
import org.efaps.admin.event.Return;
import org.efaps.admin.event.Return.ReturnValues;
import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.ci.CIAdminUser;
import org.efaps.db.AttributeQuery;
import org.efaps.db.MultiPrintQuery;
import org.efaps.db.QueryBuilder;
import org.efaps.esjp.ci.CIHumanResource;
import org.efaps.util.EFapsException;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
@EFapsUUID("6c060a95-0515-4809-ab2f-6834a951e93c")
@EFapsRevision("$Rev$")
public abstract class Employee_Base
{
    /**
     * Create a new Employee.
     *
     * @param _parameter    Parameter as passed from the eFAPS API
     * @return new Return
     * @throws EFapsException on error
     */
    public Return create(final Parameter _parameter)
        throws EFapsException
    {
        return new Return();
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
        final List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        final Map<?, ?> properties = (Map<?, ?>) _parameter.get(ParameterValues.PROPERTIES);
        final String key = (String) properties.get("keyValue");
        if (input.length() > 0) {
            final boolean nameSearch = Character.isDigit(input.charAt(0));
            final Map<String, Map<String, String>> tmpMap = new TreeMap<String, Map<String, String>>();
            final QueryBuilder queryBld = new QueryBuilder(CIHumanResource.EmployeeAbstract);
            if (nameSearch) {
                queryBld.addWhereAttrMatchValue(CIHumanResource.EmployeeAbstract.Number, input + "*");
            } else {
                queryBld.addWhereAttrMatchValue(CIHumanResource.EmployeeAbstract.LastName, input + "*")
                        .setIgnoreCase(true);
            }
            final MultiPrintQuery print = queryBld.getPrint();
            print.addAttribute(CIHumanResource.EmployeeAbstract.OID,
                               CIHumanResource.EmployeeAbstract.Number,
                               CIHumanResource.EmployeeAbstract.FirstName,
                               CIHumanResource.EmployeeAbstract.LastName);
            print.execute();
            while (print.next()) {
                final String number = print.<String>getAttribute(CIHumanResource.EmployeeAbstract.Number);
                final String firstname = print.<String>getAttribute(CIHumanResource.EmployeeAbstract.FirstName);
                final String lastname = print.<String>getAttribute(CIHumanResource.EmployeeAbstract.LastName);
                final String dataemployee = lastname + ", " + firstname;
                final String oid = print.<String>getAttribute(CIHumanResource.EmployeeAbstract.OID);
                final String choice = nameSearch ? number + "- " +  dataemployee : dataemployee + " - " + number;
                final Map<String, String> map = new HashMap<String, String>();
                map.put("eFapsAutoCompleteKEY", oid);
                map.put("eFapsAutoCompleteCHOICE", choice);
                if (key == null || key.isEmpty()) {
                    map.put("FirstName", firstname);
                    map.put("LastName", lastname);
                    map.put("eFapsAutoCompleteVALUE", number);
                } else {
                    map.put("eFapsAutoCompleteVALUE", dataemployee);
                }
                tmpMap.put(choice, map);
            }
            list.addAll(tmpMap.values());
        }
        final Return retVal = new Return();
        retVal.put(ReturnValues.VALUES, list);
        return retVal;
    }

    /**
     * method for auto-complete user person (if any a case non-existing in DB.)
     *
     * @param _parameter Parameter as passed from the eFaps API.
     * @return ret witch values
     * @throws EFapsException on error.
     */
    public Return autoComplete4Person(final Parameter _parameter)
        throws EFapsException
    {
        final String input = (String) _parameter.get(ParameterValues.OTHERS);
        final List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        if (input.length() > 0) {
            final Map<String, Map<String, String>> tmpMap = new TreeMap<String, Map<String, String>>();
            final QueryBuilder queryBldr = new QueryBuilder(CIAdminUser.Person);
            queryBldr.addWhereAttrMatchValue(CIAdminUser.Person.Name, input + "*").setIgnoreCase(true);
            final AttributeQuery attrQuery = new QueryBuilder(CIHumanResource.Employee)
                                                            .getAttributeQuery(CIHumanResource.Employee.UserPerson);
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
                final Map<String, String> map = new HashMap<String, String>();
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
}
