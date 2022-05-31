/*
 * Copyright (C) 2018-2022 52°North Spatial Information Research GmbH
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
package org.n52.sta.api.service;

import org.n52.sta.api.EntityEditor;
import org.n52.sta.api.EntityPage;
import org.n52.sta.api.EntityProvider;
import org.n52.sta.api.ProviderException;
import org.n52.sta.api.domain.aggregate.AggregateException;
import org.n52.sta.api.domain.aggregate.EntityAggregate;
import org.n52.sta.api.domain.aggregate.HistoricalLocationAggregate;
import org.n52.sta.api.domain.service.DefaultDomainService;
import org.n52.sta.api.domain.service.DomainService;
import org.n52.sta.api.entity.HistoricalLocation;
import org.n52.sta.api.path.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;

public class HistoricalLocationService implements EntityService<HistoricalLocation>, EntityEditor<HistoricalLocation> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HistoricalLocationService.class);

    private final EntityProvider<HistoricalLocation> historicalLocationProvider;

    private final DomainService<HistoricalLocation> domainService;

    private Optional<EntityEditor<HistoricalLocation>> historicalLocationEditor;

    public HistoricalLocationService(EntityProvider<HistoricalLocation> provider) {
        this(provider, new DefaultDomainService<>(provider));
    }

    public HistoricalLocationService(EntityProvider<HistoricalLocation> provider,
            DomainService<HistoricalLocation> domainService) {
        Objects.requireNonNull(provider, "provider must not be null");
        this.historicalLocationProvider = provider;
        this.domainService = domainService == null
                ? new DefaultDomainService<>(provider)
                : domainService;
    }

    @Override
    public boolean exists(String id) throws ProviderException {
        return domainService.exists(id);
    }

    @Override
    public Optional<HistoricalLocation> getEntity(Request req) throws ProviderException {
        return domainService.getEntity(req);
    }

    @Override
    public EntityPage<HistoricalLocation> getEntities(Request req)throws ProviderException {
        return domainService.getEntities(req);
    }

    @Override
    public HistoricalLocation create(HistoricalLocation entity) throws ProviderException {
        try {
            return createAggregate(entity).save();
        } catch (AggregateException e) {
            LOGGER.error("Could not create entity: {}", entity, e);
            throw new ProviderException("Could not create HistoricalLocation!");
        }
    }

    @Override
    public HistoricalLocation update(HistoricalLocation entity) throws ProviderException {
        Objects.requireNonNull(entity, "entity must not be null!");
        try {
            String id = entity.getId();
            HistoricalLocation historicalLocation = getOrThrow(id);
            return createAggregate(historicalLocation).save(entity);
        } catch (AggregateException e) {
            LOGGER.error("Could not update entity: {}", entity, e);
            throw new ProviderException("Could not update HistoricalLocation!");
        }
    }

    @Override
    public void delete(String id) throws ProviderException {
        HistoricalLocation entity = getOrThrow(id);
        try {
            createAggregate(entity).delete();
        } catch (AggregateException e) {
            LOGGER.error("Could not delete entity: {}", entity, e);
            throw new ProviderException("Could not delete HistoricalLocation!");
        }
    }

    public void setHistoricalLocationEditor(EntityEditor<HistoricalLocation> editor) {
        historicalLocationEditor = Optional.ofNullable(editor);
    }

    private EntityAggregate<HistoricalLocation> createAggregate(HistoricalLocation entity) {
        return new HistoricalLocationAggregate(entity, domainService, historicalLocationEditor.orElse(null));
    }

    private HistoricalLocation getOrThrow(String id) throws ProviderException {
        return domainService.getEntity(id)
                .orElseThrow(() -> new ProviderException("Id '" + id + "' does not exist."));
    }

}
