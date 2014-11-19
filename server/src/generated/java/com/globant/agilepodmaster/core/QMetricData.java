package com.globant.agilepodmaster.core;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QMetricData is a Querydsl query type for MetricData
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QMetricData extends EntityPathBase<MetricData> {

    private static final long serialVersionUID = -336508046L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMetricData metricData = new QMetricData("metricData");

    public final QSnapshotEntity _super;

    //inherited
    public final NumberPath<Long> id;

    protected QPod pod;

    // inherited
    protected QSnapshot snapshot;

    //inherited
    public final NumberPath<Integer> version;

    public QMetricData(String variable) {
        this(MetricData.class, forVariable(variable), INITS);
    }

    public QMetricData(Path<? extends MetricData> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QMetricData(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QMetricData(PathMetadata<?> metadata, PathInits inits) {
        this(MetricData.class, metadata, inits);
    }

    public QMetricData(Class<? extends MetricData> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QSnapshotEntity(type, metadata, inits);
        this.id = _super.id;
        this.pod = inits.isInitialized("pod") ? new QPod(forProperty("pod"), inits.get("pod")) : null;
        this.version = _super.version;
    }

    public QPod pod() {
        if (pod == null) {
            pod = new QPod(forProperty("pod"));
        }
        return pod;
    }

    public QSnapshot snapshot() {
        if (snapshot == null) {
            snapshot = new QSnapshot(forProperty("snapshot"));
        }
        return snapshot;
    }

}

