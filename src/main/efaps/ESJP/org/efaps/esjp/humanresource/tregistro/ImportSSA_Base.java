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

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.esjp.humanresource.util.HumanResource;
import org.efaps.esjp.humanresource.util.HumanResourceSettings;
import org.efaps.util.EFapsException;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
@EFapsUUID("d8f9c785-3319-47de-870d-7ecbd41f9081")
@EFapsRevision("$Rev$")
public abstract class ImportSSA_Base
    extends AbstractImportUpdate
{

    public enum HEADER
    {
        DOCTYPE, DOCNUMBER, LASTNAME1, LASTNAME2, FIRSTNAME, SITUATION, EPMPLOYEETYPE, HEALTHREGIME, HDATE, OWN,
        PENSIONREGIME, PDATE, CUSPP, SCTR_H_NONE, SCTR_H_ESSALUD, SCTR_H_EPS, SCTR_P_NONE, SCTR_P_ONP,
        SCTR_P_PRIVATE, LIR, DOBBLETAX
    }

    @Override
    protected List<String[]> getValueList(final Parameter _parameter)
    {
        final List<String[]> ret = new ArrayList<String[]>();
        final List<String> headers = new ArrayList<String>();
        for (final HEADER header : HEADER.values()) {
            headers.add(header.name());
        }
        ret.add(headers.toArray(new String[headers.size()]));
        return ret;
    }


    @Override
    protected Source getSource4DataImport(final Parameter _parameter)
        throws EFapsException
    {
        Source source = null;
        final StringBuilder bldr = new StringBuilder();

        final String xml = HumanResource.getSysConfig().getAttributeValue(HumanResourceSettings.DI_SSA);
        bldr.append(xml == null ? "" : xml);
        for (int i = 1; i < 100; i++) {
            final String keyTmp = HumanResourceSettings.DI_SSA + String.format("%02d", i);
            final String valueTmp = HumanResource.getSysConfig().getAttributeValue(keyTmp);
            if (valueTmp != null) {
                bldr.append(valueTmp);
            } else {
                break;
            }
        }
        source = new StreamSource(new StringReader(bldr.toString()));
        return source;
    }
}
