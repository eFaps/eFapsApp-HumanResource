/*
 * Copyright 2003 - 2014 The eFaps Team
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

package org.efaps.esjp.humanresource.listener;

import java.util.Map;
import java.util.UUID;

import org.efaps.admin.common.MsgPhrase;
import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Instance;
import org.efaps.db.MultiPrintQuery;
import org.efaps.db.PrintQuery;
import org.efaps.db.QueryBuilder;
import org.efaps.db.SelectBuilder;
import org.efaps.esjp.ci.CIHumanResource;
import org.efaps.esjp.common.AbstractCommon;
import org.efaps.esjp.contacts.listener.IOnContact;
import org.efaps.util.EFapsException;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id: $
 */
@EFapsUUID("4d813d9f-8674-4799-979d-c783a7e78c85")
@EFapsRevision("$Rev: 6164 $")
public abstract class OnContact_Base
    extends AbstractCommon
    implements IOnContact
{

    @Override
    public void add2UpdateMap4Contact(final Parameter _parameter,
                                      final Instance _contactInstance,
                                      final Map<String, Object> _map)
        throws EFapsException
    {
        if ("true".equalsIgnoreCase(getProperty(_parameter, "SetEmployee", "false"))) {
            final String fieldname = getProperty(_parameter, "EmployeeField", "employee");
            final QueryBuilder queryBldr = new QueryBuilder(CIHumanResource.Employee2Contact);
            queryBldr.addWhereAttrEqValue(CIHumanResource.Employee2Contact.ToLink, _contactInstance);
            final MultiPrintQuery multi = queryBldr.getPrint();
            final SelectBuilder selEmplInst = SelectBuilder.get().linkto(CIHumanResource.Employee2Contact.FromLink)
                            .instance();
            multi.addSelect(selEmplInst);
            multi.execute();
            if (multi.next()) {
                final Instance emplInst = multi.getSelect(selEmplInst);
                if (emplInst != null && emplInst.isValid()) {
                    final PrintQuery print = new PrintQuery(emplInst);
                    // HumanResource_EmployeeMsgPhrase
                    final MsgPhrase msgPhrase = MsgPhrase.get(UUID.fromString("f543ca6d-29fb-4f1a-8747-0057b9a08404"));
                    print.addMsgPhrase(msgPhrase);
                    print.execute();
                    final String label = print.getMsgPhrase(msgPhrase);
                    _map.put(fieldname, new String[] { emplInst.getOid(), label });
                }
            }
        }
    }

    @Override
    public int getWeight()
    {
        return 1;
    }
}
