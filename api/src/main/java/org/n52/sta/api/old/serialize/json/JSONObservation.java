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
package org.n52.sta.api.old.serialize.json;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.n52.shetland.ogc.sta.StaConstants;
import org.n52.sta.api.old.dto.Observation;
import org.n52.sta.api.old.entity.ObservationDTO;
import org.n52.sta.api.old.serialize.common.AbstractJSONEntity;
import org.n52.sta.api.old.serialize.common.JSONBase;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressWarnings("VisibilityModifier")
@SuppressFBWarnings({"NM_FIELD_NAMING_CONVENTION", "UWF_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD"})
public class JSONObservation extends JSONBase.JSONwithIdTime<ObservationDTO> implements AbstractJSONEntity {

    // JSON Properties. Matched by Annotation or variable name
    public String phenomenonTime;
    public String resultTime;
    public JsonNode result;
    public Object resultQuality;
    public String validTime;
    public ObjectNode parameters;
    @JsonManagedReference
    public JSONFeatureOfInterest FeatureOfInterest;
    @JsonManagedReference
    public JSONDatastream Datastream;
//    @JsonManagedReference
//    public JSONGroup[] ObservationGroups;
//    @JsonManagedReference
//    public JSONRelation[] Subjects;
//    @JsonManagedReference
//    public JSONRelation[] Objects;
//    @JsonManagedReference
//    public JSONLicense License;

    public JSONObservation() {
        self = new Observation();
    }

    @Override
    protected void parseReferencedFrom() {
        if (referencedFromType != null) {
            switch (referencedFromType) {
                case StaConstants.DATASTREAMS:
                    assertIsNull(Datastream, INVALID_DUPLICATE_REFERENCE);
                    this.Datastream = new JSONDatastream();
                    this.Datastream.identifier = referencedFromID;
                    return;
                case StaConstants.FEATURES_OF_INTEREST:
                    assertIsNull(FeatureOfInterest, INVALID_DUPLICATE_REFERENCE);
                    this.FeatureOfInterest = new JSONFeatureOfInterest();
                    this.FeatureOfInterest.identifier = referencedFromID;
                    return;
                default:
                    throw new IllegalArgumentException(INVALID_BACKREFERENCE);
            }
        }
    }

    @Override
    public ObservationDTO parseToDTO(JSONBase.EntityType type) {
        switch (type) {
            case FULL:
                parseReferencedFrom();
                assertNotNull(result, INVALID_INLINE_ENTITY_MISSING + "result");
                return createPostEntity();
            case PATCH:
                parseReferencedFrom();
                return createPatchEntity();
            case REFERENCE:
                assertIsNull(phenomenonTime, INVALID_REFERENCED_ENTITY);
                assertIsNull(resultTime, INVALID_REFERENCED_ENTITY);
                assertIsNull(result, INVALID_REFERENCED_ENTITY);
                assertIsNull(resultTime, INVALID_REFERENCED_ENTITY);
                assertIsNull(resultQuality, INVALID_REFERENCED_ENTITY);
                assertIsNull(parameters, INVALID_REFERENCED_ENTITY);

                self.setId(identifier);
                return self;
            default:
                return null;
        }
    }

    private ObservationDTO createPatchEntity() {
        self.setId(identifier);

        // parameters
        self.setParameters(parameters);

        // phenomenonTime
        if (phenomenonTime != null) {
            self.setPhenomenonTime(parseTime(phenomenonTime));
        }

        // Set resultTime only when supplied
        if (resultTime != null) {
            self.setResultTime(parseTime(resultTime));
        }

        // validTime
        if (validTime != null) {
            self.setPhenomenonTime(parseTime(validTime));
        }

        self.setResult(result);

        // Link to Datastream
        if (Datastream != null) {
            self.setDatastream(Datastream.parseToDTO(JSONBase.EntityType.REFERENCE));
        }

        // Link to FOI
        if (FeatureOfInterest != null) {
            self.setFeatureOfInterest(FeatureOfInterest.parseToDTO(JSONBase.EntityType.REFERENCE));
        }

//        if (License != null) {
//            self.setLicense(License.parseToDTO(JSONBase.EntityType.REFERENCE));
//        }

        return self;
    }

    private ObservationDTO createPostEntity() {
        self.setId(identifier);

        // phenomenonTime
        if (phenomenonTime != null) {
            self.setPhenomenonTime(parseTime(phenomenonTime));
        }

        // Set resultTime only when supplied
        if (resultTime != null) {
            self.setResultTime(parseTime(resultTime));
        }

        // validTime
        if (validTime != null) {
            self.setPhenomenonTime(parseTime(validTime));
        }

        // parameters
        self.setParameters(parameters);

        // result
        self.setResult(result);

        // Link to Datastream
        if (Datastream != null) {
            self.setDatastream(
                Datastream.parseToDTO(JSONBase.EntityType.FULL, JSONBase.EntityType.REFERENCE));
        } else if (backReference instanceof JSONDatastream) {
            self.setDatastream(((JSONDatastream) backReference).getEntity());
        } else {
            assertNotNull(null, INVALID_INLINE_ENTITY_MISSING + "Datastream");
        }

        // Link to FOI
        if (FeatureOfInterest != null) {
            self.setFeatureOfInterest(
                FeatureOfInterest.parseToDTO(JSONBase.EntityType.FULL, JSONBase.EntityType.REFERENCE));
        } else if (backReference instanceof JSONFeatureOfInterest) {
            self.setFeatureOfInterest(((JSONFeatureOfInterest) backReference).getEntity());
        }

//        if (Subjects != null) {
//            Set<RelationDTO> subjects = new HashSet<>();
//            for (JSONRelation sub : Subjects) {
//                subjects.add(sub.parseToDTO(JSONBase.EntityType.FULL,
//                                            JSONBase.EntityType.REFERENCE));
//            }
//            self.setSubjects(subjects);
//        }
//
//        if (Objects != null) {
//            Set<RelationDTO> objects = new HashSet<>();
//            for (JSONRelation obj : Objects) {
//                objects.add(obj.parseToDTO(JSONBase.EntityType.FULL,
//                                           JSONBase.EntityType.REFERENCE));
//            }
//            self.setObjects(objects);
//        }
//
//        if (ObservationGroups != null) {
//            Set<GroupDTO> objects = new HashSet<>();
//            for (JSONGroup obj : ObservationGroups) {
//                objects.add(obj.parseToDTO(JSONBase.EntityType.FULL,
//                                           JSONBase.EntityType.REFERENCE));
//            }
//            self.setObservationGroups(objects);
//        }
//
//        if (License != null) {
//            self.setLicense(License.parseToDTO(JSONBase.EntityType.FULL,
//              JSONBase.EntityType.REFERENCE));
//        }

        return self;
    }

    //TODO: refactor into dao-postgres as is implementation-specific
    /*
    public JSONObservation parseParameters(Map<String, String> propertyMapping) {
        if (parameters != null) {
            for (Map.Entry<String, String> mapping : propertyMapping.entrySet()) {
                Iterator<String> keyIt = parameters.fieldNames();
                while (keyIt.hasNext()) {
                    String paramName = keyIt.next();
                    if (paramName.equals(mapping.getValue())) {
                        JsonNode jsonNode = parameters.get(paramName);
                        switch (mapping.getKey()) {
                            case "samplingGeometry":
                                // Add as samplingGeometry to enable interoperability with SOS
                                GeometryFactory factory =
                                    new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);
                                GeoJsonReader reader = new GeoJsonReader(factory);
                                try {
                                    GeometryEntity geometryEntity = new GeometryEntity();
                                    geometryEntity.setGeometry(reader.read(jsonNode.toString()));
                                    self.setGeometryEntity(geometryEntity);
                                } catch (ParseException e) {
                                    assertNotNull(null, "Could not parse" + e.getMessage());
                                }
                                continue;
                            case "verticalFrom":
                                // Add as verticalTo to enable interoperability with SOS
                                self.setVerticalTo(BigDecimal.valueOf(jsonNode.asDouble()));
                                if (!self.hasVerticalFrom()) {
                                    self.setVerticalFrom(self.getVerticalTo());
                                }
                                continue;
                            case "verticalTo":
                                // Add as verticalTo to enable interoperability with SOS
                                self.setVerticalFrom(BigDecimal.valueOf(jsonNode.asDouble()));
                                if (!self.hasVerticalTo()) {
                                    self.setVerticalTo(self.getVerticalFrom());
                                }
                                continue;
                            case "verticalFromTo":
                                // Add as verticalTo to enable interoperability with SOS
                                self.setVerticalTo(BigDecimal.valueOf(jsonNode.asDouble()));
                                self.setVerticalFrom(BigDecimal.valueOf(jsonNode.asDouble()));
                                continue;
                            default:
                                throw new RuntimeException("Unable to parse Parameters!");
                        }
                    }
                }
            }
            // Remove parameters
            for (Map.Entry<String, String> mapping : propertyMapping.entrySet()) {
                parameters.remove(mapping.getValue());
            }
        }
        return this;
    }
     */
}
