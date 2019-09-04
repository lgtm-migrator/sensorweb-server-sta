/*
 * Copyright (C) 2018-2019 52°North Initiative for Geospatial Open Source
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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.n52.sta.mqtt.handler;

import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.deserializer.DeserializerException;
import org.apache.olingo.server.api.deserializer.DeserializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.core.uri.parser.Parser;
import org.apache.olingo.server.core.uri.parser.UriParserException;
import org.apache.olingo.server.core.uri.validator.UriValidationException;
import org.n52.sta.mqtt.MqttHandlerException;
import org.n52.sta.mqtt.core.AbstractMqttSubscription;
import org.n52.sta.mqtt.core.MqttEventHandler;
import org.n52.sta.service.handler.AbstractEntityCollectionRequestHandler;
import org.n52.sta.service.query.URIQueryOptions;
import org.n52.sta.service.response.EntityResponse;
import org.n52.sta.utils.CrudHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.moquette.interception.messages.InterceptPublishMessage;
import io.moquette.interception.messages.InterceptSubscribeMessage;
import io.moquette.interception.messages.InterceptUnsubscribeMessage;
import io.netty.buffer.ByteBufInputStream;
import org.apache.olingo.server.api.uri.UriResourcePartTyped;
import org.n52.sta.mqtt.core.MqttEntityCollectionSubscription;
import org.n52.sta.mqtt.core.MqttEntitySubscription;
import org.n52.sta.mqtt.core.MqttPropertySubscription;
import org.n52.sta.mqtt.request.SensorThingsMqttRequest;
import org.n52.sta.service.handler.AbstractEntityRequestHandler;
import org.n52.sta.service.handler.AbstractPropertyRequestHandler;

/**
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
@Component
public class MqttMessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MqttMessageHandler.class);

    private final Parser parser;
    private final CrudHelper crudHelper;

    public MqttMessageHandler(Parser parser, CrudHelper crudHelper) {
        this.parser = parser;
        this.crudHelper = crudHelper;
    }

    @SuppressWarnings("unchecked")
    public void processPublishMessage(InterceptPublishMessage msg) throws UriParserException, UriValidationException, ODataApplicationException, DeserializerException {
        UriInfo uriInfo = parser.parseUri(msg.getTopicName(), null, null, "");
        EntityResponse entityResponse = new EntityResponse();
        DeserializerResult deserializeRequestBody = crudHelper.deserializeRequestBody(new ByteBufInputStream(msg.getPayload()), uriInfo);
        if (deserializeRequestBody.getEntity() != null) {
            entityResponse = crudHelper.getCrudEntityHanlder(uriInfo)
                    .handleCreateEntityRequest(deserializeRequestBody.getEntity(), uriInfo.getUriResourceParts());
            LOGGER.debug("Creation of Entity {} was successful", entityResponse.getEntity());
        }
    }
}
