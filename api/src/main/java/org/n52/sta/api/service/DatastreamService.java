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

import java.util.Objects;
import java.util.Optional;

import org.n52.shetland.oasis.odata.query.option.QueryOptions;
import org.n52.sta.api.EditorException;
import org.n52.sta.api.EntityEditor;
import org.n52.sta.api.EntityPage;
import org.n52.sta.api.EntityProvider;
import org.n52.sta.api.ProviderException;
import org.n52.sta.api.domain.aggregate.AggregateException;
import org.n52.sta.api.domain.aggregate.DatastreamAggregate;
import org.n52.sta.api.domain.aggregate.EntityAggregate;
import org.n52.sta.api.entity.Datastream;
import org.n52.sta.api.path.Request;
import org.n52.svalbard.odata.core.QueryOptionsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatastreamService implements EntityService<Datastream> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatastreamService.class);

    private final EntityProvider<Datastream> datastreamProvider;

    private Optional<EntityEditor<Datastream>> datastreamEditor;

    public DatastreamService(EntityProvider<Datastream> provider) {
        Objects.requireNonNull(provider, "provider must not be null");
        this.datastreamProvider = provider;
    }

    @Override
    public boolean exists(String id) throws ProviderException {
        return datastreamProvider.exists(id);
    }

    @Override
    public Optional<Datastream> getEntity(String id, QueryOptions queryOptions) throws ProviderException {
        return datastreamProvider.getEntity(id, queryOptions);
    }

    @Override
    public Optional<Datastream> getEntity(Request req) throws ProviderException {
        return datastreamProvider.getEntity(req);
    }

    @Override
    public EntityPage<Datastream> getEntities(Request req) throws ProviderException {
        return datastreamProvider.getEntities(req);
    }

    @Override
    public Datastream save(Datastream entity) throws EditorException {
        try {
            return createAggregate(entity).save();
        } catch (AggregateException e) {
            LOGGER.error("Could not create entity: {}", entity, e);
            throw new ProviderException("Could not create Datastream!");
        }
    }

    @Override
    public Datastream update(Datastream entity) throws EditorException {
        Objects.requireNonNull(entity, "entity must not be null!");
        try {
            String id = entity.getId();
            Datastream thing = getOrThrow(id);
            return createAggregate(thing).save(entity);
        } catch (AggregateException e) {
            LOGGER.error("Could not update entity: {}", entity, e);
            throw new ProviderException("Could not update Datastream!");
        }
    }

    @Override
    public void delete(String id) throws EditorException {
        Datastream entity = getOrThrow(id);
        try {
            createAggregate(entity).delete();
        } catch (AggregateException e) {
            LOGGER.error("Could not delete entity: {}", entity, e);
            throw new ProviderException("Could not delete Datastream!");
        }
    }

    public void setDatastreamEditor(EntityEditor<Datastream> editor) {
        datastreamEditor = Optional.ofNullable(editor);
    }

    private EntityAggregate<Datastream> createAggregate(Datastream entity) {
        return new DatastreamAggregate(entity, datastreamEditor.orElse(null));
    }

    private Datastream getOrThrow(String id) throws ProviderException {
        return datastreamProvider.getEntity(id, QueryOptionsFactory.createEmpty())
                                 .orElseThrow(() -> new ProviderException("Id '" + id + "' does not exist."));
    }

}
