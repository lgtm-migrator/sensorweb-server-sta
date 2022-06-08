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

package org.n52.sta.data.query.specifications;

import java.util.Optional;

import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.n52.series.db.beans.AbstractDatasetEntity;
import org.n52.series.db.beans.DataEntity;
import org.n52.series.db.beans.IdEntity;
import org.n52.series.db.beans.PhenomenonEntity;
import org.n52.series.db.beans.PlatformEntity;
import org.n52.series.db.beans.ProcedureEntity;
import org.n52.shetland.ogc.sta.StaConstants;
import org.springframework.data.jpa.domain.Specification;

public class DatastreamQuerySpecification extends QuerySpecification<AbstractDatasetEntity> {

    public DatastreamQuerySpecification() {
        this.filterByMember.put(StaConstants.SENSORS, new SensorFilter());
        this.filterByMember.put(StaConstants.OBSERVED_PROPERTIES, new ObservedPropertyFilter());
        this.filterByMember.put(StaConstants.THINGS, new ThingFilter());
        this.filterByMember.put(StaConstants.OBSERVATIONS, createObservationFilter());
    }

    @Override
    public Optional<Specification<AbstractDatasetEntity>> isStaEntity() {
        // TODO: ideally we should check for datasetType == not_initialized, but it is
        // not exposed in the abstract class
        // checking for platform == null as a workaround
        return Optional.of(
                           (root, query, builder) -> builder.and(
                                                                 root.get(AbstractDatasetEntity.PROPERTY_PLATFORM)
                                                                     .isNotNull(),
                                                                 builder.isNull(root.get(AbstractDatasetEntity.PROPERTY_AGGREGATION))));
    }

    private final class SensorFilter extends MemberFilterImpl<AbstractDatasetEntity> {

        protected Specification<AbstractDatasetEntity> prepareQuery(Specification< ? > specification) {
            return (root, query, builder) -> {
                EntityQuery memberQuery = createQuery(IdEntity.PROPERTY_ID,
                                                      ProcedureEntity.class);
                Subquery< ? > subquery = memberQuery.create(specification, query, builder);
                // n..1
                return builder.in(subquery)
                              .value(root.get(AbstractDatasetEntity.PROPERTY_PROCEDURE));
            };
        }
    }

    private final class ObservedPropertyFilter extends MemberFilterImpl<AbstractDatasetEntity> {

        protected Specification<AbstractDatasetEntity> prepareQuery(Specification< ? > specification) {
            return (root, query, builder) -> {
                EntityQuery memberQuery = createQuery(IdEntity.PROPERTY_ID,
                                                      PhenomenonEntity.class);
                Subquery< ? > subquery = memberQuery.create(specification, query, builder);
                // n..1
                return builder.in(subquery)
                              .value(root.get(AbstractDatasetEntity.PROPERTY_PHENOMENON));
            };
        }
    }

    private final class ThingFilter extends MemberFilterImpl<AbstractDatasetEntity> {

        protected Specification<AbstractDatasetEntity> prepareQuery(Specification< ? > specification) {
            return (root, query, builder) -> {
                EntityQuery memberQuery = createQuery(IdEntity.PROPERTY_ID,
                                                      PlatformEntity.class);
                Subquery< ? > subquery = memberQuery.create(specification, query, builder);
                // n..1
                return builder.in(subquery)
                              .value(root.get(AbstractDatasetEntity.PROPERTY_PLATFORM));
            };
        }
    }

    private MemberFilter<AbstractDatasetEntity> createObservationFilter() {
        // add member specification on root specfication
        return memberSpec -> (root, query, builder) -> {

            // TODO: maybe refactor this to use BaseQuerySpecifications.createQuery similar
            // to other Filters

            Subquery<AbstractDatasetEntity> idQuery = query.subquery(AbstractDatasetEntity.class);
            Root<AbstractDatasetEntity> data = idQuery.from(AbstractDatasetEntity.class);
            idQuery.select(data.get(DataEntity.PROPERTY_DATASET_ID))
                   .where(((Specification<AbstractDatasetEntity>) memberSpec).toPredicate(data, query, builder));

            Subquery<AbstractDatasetEntity> aggregationQuery = query.subquery(AbstractDatasetEntity.class);
            Root<AbstractDatasetEntity> realDataset = aggregationQuery.from(AbstractDatasetEntity.class);
            aggregationQuery.select(realDataset.get(AbstractDatasetEntity.PROPERTY_AGGREGATION))
                            .where(builder.equal(realDataset.get(IdEntity.PROPERTY_ID), idQuery));

            // matches id or aggregation
            return builder.or(builder.equal(root.get(IdEntity.PROPERTY_ID), idQuery),
                              builder.equal(root.get(IdEntity.PROPERTY_ID), aggregationQuery));
        };
    }

    private final class ObservationFilter extends MemberFilterImpl<AbstractDatasetEntity> {

        protected Specification<AbstractDatasetEntity> prepareQuery(Specification< ? > specification) {
            return (root, query, builder) -> {
                // TODO: maybe refactor this to use BaseQuerySpecifications.createQuery similar
                // to other Filters
                Subquery<AbstractDatasetEntity> sq = query.subquery(AbstractDatasetEntity.class);

                Root<DataEntity> data = sq.from(DataEntity.class);
                sq.select(data.get(DataEntity.PROPERTY_DATASET_ID))
                  .where(((Specification<DataEntity>) specification).toPredicate(data, query, builder));

                Subquery<AbstractDatasetEntity> subquery = query.subquery(AbstractDatasetEntity.class);
                Root<AbstractDatasetEntity> realDataset = subquery.from(AbstractDatasetEntity.class);
                subquery.select(realDataset.get(AbstractDatasetEntity.PROPERTY_AGGREGATION))
                        .where(builder.equal(realDataset.get(IdEntity.PROPERTY_ID), sq));

                // Either id matches or aggregation id matches
                return builder.or(builder.equal(root.get(IdEntity.PROPERTY_ID), sq),
                                  builder.equal(root.get(IdEntity.PROPERTY_ID), subquery));

            };
        }
    }

}
