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

package org.efaps.esjp.humanresource.util;

import org.efaps.admin.program.esjp.EFapsRevision;
import org.efaps.admin.program.esjp.EFapsUUID;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */

@EFapsUUID("c9501012-a352-4445-86d6-c36424c78fea")
@EFapsRevision("$Rev$")
public interface HumanResourceSettings
{
    /**
     * String value used as xml. Can be concatenated.
     */
    String DI_IDE = "org.efaps.humanresource.DataImport4IDE";

    /**
     * String value used as xml. Can be concatenated.
     */
    String DI_SSA = "org.efaps.humanresource.DataImport4SSA";

    /**
     * String value used as xml. Can be concatenated.
     */
    String DI_TRA = "org.efaps.humanresource.DataImport4TRA";

    /**
     * Boolean(true/false).<br/>
     * Activate the image menu.
     */
    String ACTIVATEIMAGE = "org.efaps.humanresource.ActivateImages";

    /**
     * Boolean(true/false).<br/>
     * Properties for employee image.
     */
    String IMAGEPROPERTIES = "org.efaps.humanresource.ImagesProperties";
}
