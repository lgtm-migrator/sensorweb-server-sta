/*
 * Copyright (C) 2018-2021 52°North Spatial Information Research GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.sta.data.citsci.service;

import org.n52.series.db.beans.HibernateRelations;
import org.n52.series.db.beans.sta.plus.LicenseEntity;
import org.n52.series.db.beans.sta.plus.GroupEntity;
import org.n52.series.db.beans.sta.plus.RelationEntity;
import org.n52.series.db.beans.sta.plus.PartyEntity;
import org.n52.series.db.beans.sta.plus.ProjectEntity;
import org.n52.sta.api.dto.plus.LicenseDTO;
import org.n52.sta.api.dto.plus.GroupDTO;
import org.n52.sta.api.dto.plus.RelationDTO;
import org.n52.sta.api.dto.plus.PartyDTO;
import org.n52.sta.api.dto.plus.ProjectDTO;
import org.n52.sta.api.dto.StaDTO;
import org.n52.sta.data.common.CommonSTAServiceImpl;
import org.n52.sta.data.common.CommonServiceFacade;
import org.n52.sta.data.vanilla.DaoSemaphore;
import org.n52.sta.data.vanilla.SerDesConfig;
import org.springframework.stereotype.Component;

public class CitSciServiceFacade<R extends StaDTO, S extends HibernateRelations.HasId>
    extends CommonServiceFacade<R, S> {

    public CitSciServiceFacade(CommonSTAServiceImpl<?, R, S> serviceImpl,
                               DaoSemaphore semaphore, SerDesConfig config) {
        super(serviceImpl, semaphore, config);
    }

    @Component
    static class LicenseServiceFacade extends CommonServiceFacade<LicenseDTO, LicenseEntity> {

        LicenseServiceFacade(LicenseService serviceImpl,
                             DaoSemaphore semaphore,
                             SerDesConfig config) {
            super(serviceImpl, semaphore, config);
        }
    }


    @Component
    static class PartyServiceFacade extends CommonServiceFacade<PartyDTO, PartyEntity> {

        PartyServiceFacade(PartyService serviceImpl,
                           DaoSemaphore semaphore,
                           SerDesConfig config) {
            super(serviceImpl, semaphore, config);
        }
    }


    @Component
    static class ProjectServiceFacade extends CommonServiceFacade<ProjectDTO, ProjectEntity> {

        ProjectServiceFacade(ProjectService serviceImpl,
                             DaoSemaphore semaphore,
                             SerDesConfig config) {
            super(serviceImpl, semaphore, config);
        }
    }


    @Component
    static class ObservationRelationServiceFacade
        extends CommonServiceFacade<RelationDTO, RelationEntity> {

        ObservationRelationServiceFacade(ObservationRelationService serviceImpl,
                                         DaoSemaphore semaphore,
                                         SerDesConfig config) {
            super(serviceImpl, semaphore, config);
        }
    }


    @Component
    static class ObservationGroupServiceFacade
        extends CommonServiceFacade<GroupDTO, GroupEntity> {

        ObservationGroupServiceFacade(ObservationGroupService serviceImpl,
                                      DaoSemaphore semaphore,
                                      SerDesConfig config) {
            super(serviceImpl, semaphore, config);
        }
    }
}
