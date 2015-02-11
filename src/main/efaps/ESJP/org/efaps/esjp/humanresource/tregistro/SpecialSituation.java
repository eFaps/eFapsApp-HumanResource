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

import org.efaps.admin.datamodel.IEnum;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id: SpecialSituation.java 11409 2013-12-16 16:43:43Z jan@moxter.net
 *          $
 */
public enum SpecialSituation
                implements IEnum
{
    NONE(0), MANAGEMENT(1), TRUST(2);

    private int idx;

    private SpecialSituation(final int _idx)
    {
        this.idx = _idx;
    }

    @Override
    public int getInt()
    {
        return ordinal();
    }

    /**
     * Getter method for the instance variable {@link #idx}.
     *
     * @return value of instance variable {@link #idx}
     */
    public int getIdx()
    {
        return this.idx;
    }
}
