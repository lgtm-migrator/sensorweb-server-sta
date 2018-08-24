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
package org.n52.sta.mapping;

import static org.n52.sta.edm.provider.entities.AbstractSensorThingsEntityProvider.ID_ANNOTATION;
import static org.n52.sta.edm.provider.entities.AbstractSensorThingsEntityProvider.PROP_DESCRIPTION;
import static org.n52.sta.edm.provider.entities.SensorEntityProvider.ES_SENSORS_NAME;
import static org.n52.sta.edm.provider.entities.SensorEntityProvider.ET_SENSOR_FQN;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.n52.series.db.beans.FeatureEntity;
import org.n52.sta.edm.provider.entities.AbstractSensorThingsEntityProvider;
import org.n52.sta.utils.EntityCreationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 *
 */
@Component
public class FeatureOfInterestMapper {

    private static final String ENCODINGTYPE_GEOJSON = "application/vnd.geo+json";

    @Autowired
    EntityCreationHelper entityCreationHelper;

    public Entity createFeatureOfInterestEntity(FeatureEntity feature) {
        Entity entity = new Entity();
        entity.addProperty(new Property(null, ID_ANNOTATION, ValueType.PRIMITIVE, feature.getId()));
        entity.addProperty(new Property(null, PROP_DESCRIPTION, ValueType.PRIMITIVE, feature.getDescription()));
        entity.addProperty(new Property(null, AbstractSensorThingsEntityProvider.PROP_NAME, ValueType.PRIMITIVE, feature.getName()));
        //TODO: generate GeoJSON from feature.getGeometry()
        entity.addProperty(new Property(null, AbstractSensorThingsEntityProvider.PROP_FEATURE, ValueType.PRIMITIVE, feature.getXml()));

        entity.addProperty(new Property(null, AbstractSensorThingsEntityProvider.PROP_ENCODINGTYPE, ValueType.PRIMITIVE, ENCODINGTYPE_GEOJSON));
        entity.setType(ET_SENSOR_FQN.getFullQualifiedNameAsString());
        entity.setId(entityCreationHelper.createId(entity, ES_SENSORS_NAME, ID_ANNOTATION));

        return entity;
    }

}
