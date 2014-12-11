package com.globant.agilepodmaster.core;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.globant.agilepodmaster.snapshot.AbstractMetric;
import com.mysema.query.types.path.*;
import com.mysema.query.types.PathMetadata;

import javax.annotation.Generated;

import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QAbstractPodMetric is a Querydsl query type for AbstractPodMetric
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QAbstractPodMetric extends EntityPathBase<AbstractMetric> {

    private static final long serialVersionUID = -1363783333L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAbstractPodMetric abstractPodMetric = new QAbstractPodMetric("abstractPodMetric");

    public final QSnapshotEntity _super;

    //inherited
    public final NumberPath<Long> id;

    protected QPod pod;

    // inherited
    protected QSnapshot snapshot;

    //inherited
    public final NumberPath<Integer> version;

    public QAbstractPodMetric(String variable) {
        this(AbstractMetric.class, forVariable(variable), INITS);
    }

    public QAbstractPodMetric(Path<? extends AbstractMetric> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QAbstractPodMetric(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QAbstractPodMetric(PathMetadata<?> metadata, PathInits inits) {
        this(AbstractMetric.class, metadata, inits);
    }

    public QAbstractPodMetric(Class<? extends AbstractMetric> type, PathMetadata<?> metadata, PathInits inits) {
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

