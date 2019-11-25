package org.n52.sta.serdes.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("VisibilityModifier")
public class ObservedPropertyEntityDefinition extends STAEntityDefinition {

    public static String entityName = OBSERVED_PROPERTY;

    public static String entitySetName = OBSERVED_PROPERTIES;

    private static String[] navProps = new String[] {
            DATASTREAMS
    };

    private static String[] entityProps = new String[] {
            PROP_NAME,
            PROP_DESCRIPTION,
            PROP_DEFINITION,
    };

    public static Set<String> navigationProperties = new HashSet<>(Arrays.asList(navProps));

    public static Set<String> entityProperties = new HashSet<>(Arrays.asList(entityProps));
}
