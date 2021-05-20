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

package org.n52.sta.api.dto.impl.citsci;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.sta.api.dto.DatastreamDTO;
import org.n52.sta.api.dto.ProjectDTO;
import org.n52.sta.api.dto.impl.Entity;

import java.util.Set;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class Project extends Entity implements ProjectDTO {

    private String name;
    private String description;
    private Time runtime;
    private Time created;
    private String privacyPolicy;
    private String termsOfUse;
    private String classification;
    private String url;
    private ObjectNode properties;
    private Set<DatastreamDTO> datastreams;

    @Override public Set<DatastreamDTO> getDatastreams() {
        return datastreams;
    }

    @Override public void setDatastreams(Set<DatastreamDTO> datastreams) {
        this.datastreams = datastreams;
    }

    @Override public String getName() {
        return name;
    }

    @Override public void setName(String name) {
        this.name = name;
    }

    @Override public String getDescription() {
        return description;
    }

    @Override public void setDescription(String description) {
        this.description = description;
    }

    @Override public Time getRuntime() {
        return runtime;
    }

    @Override public void setRuntime(Time runtime) {
        this.runtime = runtime;
    }

    @Override public String getPrivacyPolicy() {
        return privacyPolicy;
    }

    @Override public void setPrivacyPolicy(String privacyPolicy) {
        this.privacyPolicy = privacyPolicy;
    }

    @Override public String getTermsOfUse() {
        return termsOfUse;
    }

    @Override public void setTermsOfUse(String termsOfUse) {
        this.termsOfUse = termsOfUse;
    }

    @Override public String getClassification() {
        return classification;
    }

    @Override public void setClassification(String classification) {
        this.classification = classification;
    }

    @Override public String getUrl() {
        return url;
    }

    @Override public void setUrl(String url) {
        this.url = url;
    }

    @Override public ObjectNode getProperties() {
        return properties;
    }

    @Override public void setProperties(ObjectNode properties) {
        this.properties = properties;
    }

    @Override public Time getCreated() {
        return created;
    }

    @Override public void setCreated(Time created) {
        this.created = created;
    }
}
