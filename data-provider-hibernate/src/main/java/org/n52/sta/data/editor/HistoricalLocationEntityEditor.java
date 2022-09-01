package org.n52.sta.data.editor;

import java.util.Optional;

import org.n52.series.db.beans.sta.HistoricalLocationEntity;
import org.n52.series.db.beans.sta.LocationEntity;
import org.n52.sta.api.EntityServiceLookup;
import org.n52.sta.api.entity.HistoricalLocation;
import org.n52.sta.api.entity.Thing;
import org.n52.sta.api.exception.EditorException;
import org.n52.sta.data.entity.HistoricalLocationData;
import org.n52.sta.data.entity.ThingData;
import org.n52.sta.data.repositories.entity.HistoricalLocationRepository;
import org.n52.sta.data.support.HistoricalLocationGraphBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

public class HistoricalLocationEntityEditor extends DatabaseEntityAdapter<HistoricalLocationEntity>
        implements
        EntityEditorDelegate<HistoricalLocation, HistoricalLocationData> {

    @Autowired
    private HistoricalLocationRepository historicalLocationRepository;

    private EntityEditorDelegate<Thing, ThingData> thingEditor;

    public HistoricalLocationEntityEditor(EntityServiceLookup serviceLookup) {
        super(serviceLookup);
    }

    @EventListener
    @SuppressWarnings("unchecked")
    private void postConstruct(ContextRefreshedEvent event) {
        //@formatter:off
        // As we are the package providing the EE Implementations, this cast should never fail.
        this.thingEditor = (EntityEditorDelegate<Thing, ThingData>)
                getService(Thing.class).unwrapEditor();
        //@formatter:on
    }

    @Override
    public HistoricalLocationData getOrSave(HistoricalLocation entity) throws EditorException {
        throw new EditorException();
    }

    @Override
    public HistoricalLocationData save(HistoricalLocation entity) throws EditorException {
        throw new EditorException();
    }

    @Override
    public HistoricalLocationData update(HistoricalLocation entity) throws EditorException {
        throw new EditorException();
    }

    @Override
    public void delete(String id) throws EditorException {
        HistoricalLocationEntity historicalLocation = getEntity(id)
                .orElseThrow(() -> new EditorException("could not find entity with id: " + id));

        updateLocations(historicalLocation);
        updateThing(historicalLocation);

        historicalLocationRepository.deleteByStaIdentifier(id);
    }

    @Override
    protected Optional<HistoricalLocationEntity> getEntity(String id) {
        HistoricalLocationGraphBuilder graphBuilder = HistoricalLocationGraphBuilder.createEmpty();
        return historicalLocationRepository.findByStaIdentifier(id, graphBuilder);
    }

    private void updateLocations(HistoricalLocationEntity historicalLocation) {
        for (LocationEntity location : historicalLocation.getLocations()) {
            location.getHistoricalLocations()
                    .remove(historicalLocation);
        }
    }

    private void updateThing(HistoricalLocationEntity historicalLocation) {
        historicalLocation.getThing().setHistoricalLocations(null);
    }
}
