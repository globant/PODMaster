package com.globant.agilepodmaster.snapshot;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QAbstractMetric is a Querydsl query type for AbstractMetric
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QAbstractMetric extends EntityPathBase<AbstractMetric> {

    private static final long serialVersionUID = 761556261L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAbstractMetric abstractMetric = new QAbstractMetric("abstractMetric");

    public final QSnapshotEntity _super;

    //inherited
    public final NumberPath<Long> id;

    // inherited
    protected QSnapshot snapshot;

    //inherited
    public final NumberPath<Integer> version;

    public QAbstractMetric(String variable) {
        this(AbstractMetric.class, forVariable(variable), INITS);
    }

    public QAbstractMetric(Path<? extends AbstractMetric> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QAbstractMetric(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QAbstractMetric(PathMetadata<?> metadata, PathInits inits) {
        this(AbstractMetric.class, metadata, inits);
    }

    public QAbstractMetric(Class<? extends AbstractMetric> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QSnapshotEntity(type, metadata, inits);
        this.id = _super.id;
        this.version = _super.version;
    }

    public QSnapshot snapshot() {
        if (snapshot == null) {
            snapshot = new QSnapshot(forProperty("snapshot"));
        }
        return snapshot;
    }

}

