/*
 * Copyright (C) 2018-2021 52°North Initiative for Geospatial Open Source
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
package org.n52.sta.data.vanilla.service;

import org.locationtech.jts.geom.Geometry;
import org.n52.series.db.beans.AbstractFeatureEntity;
import org.n52.series.db.beans.FeatureEntity;
import org.n52.series.db.beans.FormatEntity;
import org.n52.series.db.beans.sta.LocationEntity;
import org.n52.shetland.ogc.om.features.SfConstants;

public class ServiceUtils {

    static final String AUTOGENERATED_KEY = "autogenerated";

    static AbstractFeatureEntity<?> createFeatureOfInterest(LocationEntity location) {
        FeatureEntity featureOfInterest = new FeatureEntity();
        featureOfInterest.setIdentifier(location.getIdentifier());
        featureOfInterest.setStaIdentifier(location.getStaIdentifier());
        // Used to distinguish in FeatureOfInterestService
        featureOfInterest.setXml(AUTOGENERATED_KEY);
        featureOfInterest.setName(location.getName());
        featureOfInterest.setDescription(location.getDescription());
        featureOfInterest.setGeometryEntity(location.getGeometryEntity());
        featureOfInterest.setFeatureType(createFeatureType(location.getGeometry()));
        return featureOfInterest;
    }

    public static FormatEntity createFeatureType(Geometry geometry) {
        FormatEntity formatEntity = new FormatEntity();
        if (geometry != null) {
            switch (geometry.getGeometryType()) {
                case "Point":
                    formatEntity.setFormat(SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_POINT);
                    break;
                case "LineString":
                    formatEntity.setFormat(SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_CURVE);
                    break;
                case "Polygon":
                    formatEntity.setFormat(SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_SURFACE);
                    break;
                default:
                    formatEntity.setFormat(SfConstants.SAMPLING_FEAT_TYPE_SF_SPATIAL_SAMPLING_FEATURE);
                    break;
            }
            return formatEntity;
        }
        return formatEntity.setFormat(SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_FEATURE);
    }
}
