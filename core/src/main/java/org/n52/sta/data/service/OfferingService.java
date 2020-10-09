/*
 * Copyright (C) 2018-2020 52°North Initiative for Geospatial Open Source
 * Software GmbH
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

package org.n52.sta.data.service;

import org.n52.series.db.beans.FormatEntity;
import org.n52.series.db.beans.OfferingEntity;
import org.n52.series.db.beans.ProcedureEntity;
import org.n52.series.db.beans.sta.AbstractDatastreamEntity;
import org.n52.shetland.ogc.sta.exception.STACRUDException;
import org.n52.sta.data.MutexFactory;
import org.n52.sta.data.repositories.aux.OfferingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
@Component
@DependsOn({"springApplicationContext"})
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class OfferingService {

    public static final String DEFAULT_CATEGORY = "DEFAULT_STA_CATEGORY";

    private static final Logger logger = LoggerFactory.getLogger(OfferingService.class);
    private final MutexFactory mutexFactory;
    private final OfferingRepository offeringRepository;

    public OfferingService(MutexFactory mutexFactory,
                           OfferingRepository offeringRepository) {
        this.mutexFactory = mutexFactory;
        this.offeringRepository = offeringRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
                   transactionManager = "auxiliaryTransactionManager")
    public OfferingEntity createOrFetchOffering(AbstractDatastreamEntity datastream) throws STACRUDException {
        ProcedureEntity procedure = datastream.getProcedure();
        if (!offeringRepository.existsByIdentifier(procedure.getIdentifier())) {
            OfferingEntity offering = new OfferingEntity();
            offering.setIdentifier(procedure.getIdentifier());
            offering.setStaIdentifier(procedure.getStaIdentifier());
            offering.setName(procedure.getName());
            offering.setDescription(procedure.getDescription());
            if (datastream.hasSamplingTimeStart()) {
                offering.setSamplingTimeStart(datastream.getSamplingTimeStart());
            }
            if (datastream.hasSamplingTimeEnd()) {
                offering.setSamplingTimeEnd(datastream.getSamplingTimeEnd());
            }
            if (datastream.getResultTimeStart() != null) {
                offering.setResultTimeStart(datastream.getResultTimeStart());
            }
            if (datastream.getResultTimeEnd() != null) {
                offering.setResultTimeEnd(datastream.getResultTimeEnd());
            }
            if (datastream.isSetGeometry()) {
                offering.setGeometryEntity(datastream.getGeometryEntity());
            }
            HashSet<FormatEntity> set = new HashSet<>();
            set.add(datastream.getOMObservationType());
            offering.setObservationTypes(set);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.debug("Persisting from:" + Thread.currentThread().getName());
            logger.debug("With status:" + offeringRepository.existsByIdentifier(procedure.getIdentifier()));
            OfferingEntity offeringEntity = offeringRepository.save(offering);
            return offeringEntity;
        } else {
            // TODO expand time and geometry if necessary
            return offeringRepository.findByIdentifier(procedure.getIdentifier()).get();
        }
    }

}
