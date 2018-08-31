/*
 * Copyright (C) 2012-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.sta.data;

import java.util.List;
import java.util.OptionalLong;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.olingo.commons.api.data.Entity;

import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.server.api.uri.UriParameter;
import org.n52.sta.edm.provider.entities.HistoricalLocationEntityProvider;
import org.n52.sta.mapping.HistoricalLocationMapper;
import org.n52.sta.utils.DummyEntityCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 *
 */
@Component
public class HistoricalLocationService implements AbstractSensorThingsEntityService {

    @Autowired
    private HistoricalLocationMapper historicalLocationMapper;

    @Autowired
    private DummyEntityCreator entityCreator;

    @Override
    public EntityCollection getEntityCollection() {
        return entityCreator.createEntityCollection(HistoricalLocationEntityProvider.ET_HISTORICAL_LOCATION_NAME);
    }

    @Override
    public Entity getEntity(Long id) {
        return getEntityForId(String.valueOf(id));
    }

    @Override
    public Entity getRelatedEntity(Entity sourceEntity) {
        return getEntityForId(String.valueOf(ThreadLocalRandom.current().nextInt()));
    }

    @Override
    public Entity getRelatedEntity(Entity sourceEntity, List<UriParameter> keyPredicates) {
        return getEntityForId(keyPredicates.get(0).getText());
    }

    @Override
    public EntityCollection getRelatedEntityCollection(Entity sourceEntity) {
        return getEntityCollection();
    }

    private Entity getEntityForId(String id) {
        return entityCreator.createEntity(HistoricalLocationEntityProvider.ET_HISTORICAL_LOCATION_NAME, id);
    }

    @Override
    public boolean existsEntity(Long id) {
        return true;
    }

    @Override
    public boolean existsRelatedEntity(Long sourceId, EdmEntityType sourceEntityType) {
        return true;
    }

    @Override
    public boolean existsRelatedEntity(Long sourceId, EdmEntityType sourceEntityType, Long targetId) {
        return true;
    }

    @Override
    public EntityCollection getRelatedEntityCollection(Long sourceId, EdmEntityType sourceEntityType) {
        return getEntityCollection();
    }

    @Override
    public OptionalLong getIdForRelatedEntity(Long sourceId, EdmEntityType sourceEntityType) {
        return entityCreator.createId(sourceId);
    }

    @Override
    public OptionalLong getIdForRelatedEntity(Long sourceId, EdmEntityType sourceEntityType, Long targetId) {
        return entityCreator.createId(targetId);
    }


    @Override
    public Entity getRelatedEntity(Long sourceId, EdmEntityType sourceEntityType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Entity getRelatedEntity(Long sourceId, EdmEntityType sourceEntityType, Long targetId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
