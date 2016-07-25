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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.esjp.data.AbstractImport;
import org.efaps.esjp.data.jaxb.DataImport;
import org.efaps.esjp.data.jaxb.Definition;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * 
 */
@EFapsUUID("71a7ccb3-bb44-49a7-abf4-3e029e8d1e21")
@EFapsApplication("eFapsApp-HumanResource")
public abstract class AbstractImportUpdate_Base
    extends AbstractImport
{

    /**
     * Windows-1252/WinLatin 1
     */
    public static String CHARSET = "Cp1252";

    @Override
    protected List<String[]> readCSV(final Parameter _parameter,
                                     final DataImport _dataImport,
                                     final Definition _definition)
    {
        final List<String[]> ret = getValueList(_parameter);
        try {
            org.efaps.esjp.data.AbstractImport_Base.LOG.trace("Reading the CSV File.");
            final URL url = getUrl(_parameter);
            final File file = new File(url.getFile());
            final InputStreamReader inReader = new InputStreamReader(new FileInputStream(file),
                            AbstractImportUpdate_Base.CHARSET);
            final CSVReader reader = new CSVReader(inReader, "|".charAt(0), CSVParser.DEFAULT_QUOTE_CHARACTER,
                            _definition.getSkipLine());
            ret.addAll(reader.readAll());
            reader.close();
        } catch (final IOException e) {
            org.efaps.esjp.data.AbstractImport_Base.LOG.error("Catched error on reading CSV from '{}'", e, _dataImport);
        }
        return ret;
    }

    protected abstract List<String[]> getValueList(final Parameter _parameter);

}
