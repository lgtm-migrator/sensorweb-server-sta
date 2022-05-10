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
package org.n52.sta.data.entity;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.joda.time.DateTime;
import org.n52.series.db.beans.DataEntity;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.sta.api.entity.Datastream;
import org.n52.sta.api.entity.FeatureOfInterest;
import org.n52.sta.api.entity.Observation;
import org.n52.sta.old.utils.TimeUtil;

public class ObservationData<T> extends StaData<DataEntity<T>> implements Observation<T> {

    protected ObservationData(DataEntity<T> data) {
        super(data);
    }

    @Override
    public Time getPhenomenonTime() {
        Date samplingTimeStart = data.getSamplingTimeStart();
        Date samplingTimeEnd = data.getSamplingTimeEnd();
        Optional<DateTime> sStart = Optional.ofNullable(samplingTimeStart).map(TimeUtil::createDateTime);
        Optional<DateTime> sEnd = Optional.ofNullable(samplingTimeEnd).map(TimeUtil::createDateTime);
        return sStart.map(start -> TimeUtil.createTime(start, sEnd.orElse(null))).orElse(null);
    }

    @Override
    public Time getResultTime() {
        return toTime(data.getResultTime());
    }

    @Override
    public T getResult() {
        return data.getValue();
    }

    @Override
    public Object getResultQuality() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Time getValidTime() {
        Date samplingTimeStart = data.getSamplingTimeStart();
        Date samplingTimeEnd = data.getSamplingTimeEnd();
        return toTimeInterval(samplingTimeStart, samplingTimeEnd);
    }

    @Override
    public Map<String, Object> getParameters() {
        return toMap(data.getParameters());
    }

    @Override
    public FeatureOfInterest getFeatureOfInterest() {
        return new FeatureOfInterestData(data.getFeature());
    }

    @Override
    public Datastream getDatastream() {
        return new DatastreamData(data.getDataset());
    }

}
