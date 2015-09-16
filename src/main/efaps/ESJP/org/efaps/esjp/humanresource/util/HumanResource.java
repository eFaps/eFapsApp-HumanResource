/*
 * Copyright 2003 - 2015 The eFaps Team
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
 */

package org.efaps.esjp.humanresource.util;

import java.util.UUID;

import org.efaps.admin.common.SystemConfiguration;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.api.annotation.EFapsSysConfAttribute;
import org.efaps.api.annotation.EFapsSystemConfiguration;
import org.efaps.esjp.admin.common.systemconfiguration.BooleanSysConfAttribute;
import org.efaps.esjp.admin.common.systemconfiguration.PropertiesSysConfAttribute;
import org.efaps.esjp.admin.common.systemconfiguration.StringSysConfAttribute;
import org.efaps.util.cache.CacheReloadException;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 */
@EFapsUUID("8b0780d7-b6db-4f67-ab7b-ccabe8d77359")
@EFapsApplication("eFapsApp-HumanResource")
@EFapsSystemConfiguration("4620a7d6-0c39-48ac-9c52-e90dd077f52e")
public final class HumanResource
{
    /** The base. */
    public static final String BASE = "org.efaps.humanresource.";

    /** HummanResource-Configuration. */
    public static final UUID SYSCONFUUID = UUID.fromString("4620a7d6-0c39-48ac-9c52-e90dd077f52e");

    /** See description. */
    @EFapsSysConfAttribute
    public static final BooleanSysConfAttribute ACTIVATEALL = new BooleanSysConfAttribute()
                    .sysConfUUID(SYSCONFUUID)
                    .key(BASE + "ActivateAll")
                    .description("Activates T-Registro, related persons, hirachic relations, previous works etc.");

    /** See description. */
    @EFapsSysConfAttribute
    public static final BooleanSysConfAttribute ACTIVATETR = new BooleanSysConfAttribute()
                    .sysConfUUID(SYSCONFUUID)
                    .key(BASE + "ActivateTRegistro")
                    .description("Activate the classifcation and mechanism realted to T-Registro");

    /** See description. */
    @EFapsSysConfAttribute
    public static final BooleanSysConfAttribute ACTIVATEREALTED = new BooleanSysConfAttribute()
                    .sysConfUUID(SYSCONFUUID)
                    .key(BASE + "ActivateRelated")
                    .description("Activate the possibilty to register related persons. (family etc.)");


    /** See description. */
    @EFapsSysConfAttribute
    public static final BooleanSysConfAttribute ACTIVATEREALTEDEM = new BooleanSysConfAttribute()
                    .sysConfUUID(SYSCONFUUID)
                    .key(BASE + "ActivateRelatedEmergency")
                    .description("Activate the possibilty to register related persons fro emergency.");

    /** See description. */
    @EFapsSysConfAttribute
    public static final BooleanSysConfAttribute ACTIVATEHIRACHY = new BooleanSysConfAttribute()
                    .sysConfUUID(SYSCONFUUID)
                    .key(BASE + "ActivateHirachy")
                    .description("Activate the possibilty to register hirachic relations");

    /** See description. */
    @EFapsSysConfAttribute
    public static final BooleanSysConfAttribute ACTIVATEPREWORK = new BooleanSysConfAttribute()
                    .sysConfUUID(SYSCONFUUID)
                    .key(BASE + "ActivatePreviousWorks")
                    .description("Activate the possibilty to register previous works.");

    /** See description. */
    @EFapsSysConfAttribute
    public static final BooleanSysConfAttribute ACTIVATEEDU = new BooleanSysConfAttribute()
                    .sysConfUUID(SYSCONFUUID)
                    .key(BASE + "ActivateEducation")
                    .description("Activate the possibilty to register education.");

    /** See description. */
    @EFapsSysConfAttribute
    public static final BooleanSysConfAttribute ACTIVATEREF = new BooleanSysConfAttribute()
                    .sysConfUUID(SYSCONFUUID)
                    .key(BASE + "ActivateWorkReferences")
                    .description("Activate the possibilty to register Work References.");

    /** See description. */
    @EFapsSysConfAttribute
    public static final BooleanSysConfAttribute ACTIVATETRAINING = new BooleanSysConfAttribute()
                    .sysConfUUID(SYSCONFUUID)
                    .key(BASE + "ActivateTraining")
                    .description("Activate the possibilty to register trainings.");

    /** See description. */
    @EFapsSysConfAttribute
    public static final BooleanSysConfAttribute ACTIVATEACTIVATIONGRP = new BooleanSysConfAttribute()
                    .sysConfUUID(SYSCONFUUID)
                    .key(BASE + "ActivateActivationGroup")
                    .description("");

    /** See description. */
    @EFapsSysConfAttribute
    public static final BooleanSysConfAttribute ACTIVATEIMAGE = new BooleanSysConfAttribute()
                    .sysConfUUID(SYSCONFUUID)
                    .key(BASE + "ActivateImages")
                    .description(" Activate the image menu.");

    /** See description. */
    @EFapsSysConfAttribute
    public static final PropertiesSysConfAttribute IMAGEPROPERTIES = new PropertiesSysConfAttribute()
                    .sysConfUUID(SYSCONFUUID)
                    .key(BASE + "ImagesProperties")
                    .description(" Properties for employee image.");

    /** See description. */
    @EFapsSysConfAttribute
    public static final StringSysConfAttribute DIIDE = new StringSysConfAttribute()
                    .sysConfUUID(SYSCONFUUID)
                    .key(BASE + "DataImport4IDE")
                    .description(" Import xml.");

    /** See description. */
    @EFapsSysConfAttribute
    public static final StringSysConfAttribute DI4IDE = new StringSysConfAttribute()
                    .sysConfUUID(SYSCONFUUID)
                    .key(BASE + "DataImport4IDE")
                    .description(" Import xml.");

    /** See description. */
    @EFapsSysConfAttribute
    public static final StringSysConfAttribute DI4SSA = new StringSysConfAttribute()
                    .sysConfUUID(SYSCONFUUID)
                    .key(BASE + "DataImport4SSA")
                    .description(" Import xml.");
    /** See description. */
    @EFapsSysConfAttribute
    public static final StringSysConfAttribute DI4TRA = new StringSysConfAttribute()
                    .sysConfUUID(SYSCONFUUID)
                    .key(BASE + "DataImport4TRA")
                    .description(" Import xml.");

    /**
     * Singelton.
     */
    private HumanResource()
    {
    }

    /**
     * @return the SystemConfigruation for HumanResource
     * @throws CacheReloadException on error
     */
    public static SystemConfiguration getSysConfig()
        throws CacheReloadException
    {
        // HummanResource-Configuration
        return SystemConfiguration.get(SYSCONFUUID);
    }

}
