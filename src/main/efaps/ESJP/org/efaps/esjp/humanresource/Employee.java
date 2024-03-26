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

import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Instance;
import org.efaps.util.EFapsException;

/**
 * This class must be replaced for customization, therefore it is left empty.
 * Functional description can be found in the related "<code>_base</code>"
 * class.
 *
 * @author The eFaps Team
 */
@EFapsUUID("f7291f1c-260b-49ec-be6c-aeaf653fed1b")
@EFapsApplication("eFapsApp-HumanResource")
public class Employee
    extends Employee_Base
{
    /**
     * Gets the employee assigned to contact.
     *
     * @param _parameter Parameter as passed by the eFaps API
     * @param _contactInstance the contact instance
     * @return the employee assigned2 contact
     * @throws EFapsException on error
     */
    public static String getEmployeeAssigned2Contact(final Parameter _parameter,
                                                     final Instance _contactInstance)
        throws EFapsException
    {
        return Employee_Base.getEmployeeAssigned2Contact(_parameter, _contactInstance);
    }
}
