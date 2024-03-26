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
package org.efaps.esjp.humanresource.listener;

import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsListener;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Instance;
import org.efaps.eql.EQL;
import org.efaps.esjp.ci.CIHumanResource;
import org.efaps.esjp.ci.CIProducts;
import org.efaps.esjp.db.InstanceUtils;
import org.efaps.esjp.humanresource.util.HumanResource;
import org.efaps.esjp.products.listener.IPriceListListener;
import org.efaps.util.EFapsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@EFapsUUID("ab36ce21-e15b-4f67-8610-086aa8ff5078")
@EFapsApplication("eFapsApp-HumanResource")
@EFapsListener
public class PriceListener
    implements IPriceListListener
{
    private static final Logger LOG = LoggerFactory.getLogger(PriceListener.class);

    @Override
    public boolean groupApplies(Parameter parameter,
                                Instance priceGroupInstance)
        throws EFapsException
    {
        boolean ret = false;
        LOG.debug("Checking if Backend-PriceGroup applies: {}", priceGroupInstance);
        final var department = parameter.getParameterValue("department");
        if (HumanResource.DEPARTMENT_ACTIVATEPRICEGRP.get()
                        && InstanceUtils.isKindOf(priceGroupInstance, CIProducts.PriceGroupAbstract)
                        && department != null) {
            final var departmentInst = Instance.get(department);
            if (departmentInst.isValid()) {
                final var eval = EQL.builder()
                                .print(departmentInst)
                                .linkto(CIHumanResource.DepartmentAbstract.PriceGroupLink).instance().as("priceGrpInst")
                                .evaluate();
                final Instance priceGrpInst = eval.get("priceGrpInst");
                ret = InstanceUtils.isValid(priceGrpInst) && priceGrpInst.equals(priceGroupInstance);
            }
        }
        LOG.debug("Evaluation result: {}", ret);
        return ret;
    }
}
