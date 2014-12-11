package com.globant.agilepodmaster.core;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.globant.agilepodmaster.snapshot.SnapshotEntity;
import com.mysema.query.types.path.*;
import com.mysema.query.types.PathMetadata;

import javax.annotation.Generated;

import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QSnapshotEntity is a Querydsl query type for SnapshotEntity
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QSnapshotEntity extends EntityPathBase<SnapshotEntity> {

    private static final long serialVersionUID = 625397951L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSnapshotEntity snapshotEntity = new QSnapshotEntity("snapshotEntity");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    //inherited
    public final NumberPath<Long> id = _super.id;

    protected QSnapshot snapshot;

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QSnapshotEntity(String variable) {
        this(SnapshotEntity.class, forVariable(variable), INITS);
    }

    public QSnapshotEntity(Path<? extends SnapshotEntity> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QSnapshotEntity(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QSnapshotEntity(PathMetadata<?> metadata, PathInits inits) {
        this(SnapshotEntity.class, metadata, inits);
    }

    public QSnapshotEntity(Class<? extends SnapshotEntity> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.snapshot = inits.isInitialized("snapshot") ? new QSnapshot(forProperty("snapshot")) : null;
    }

    public QSnapshot snapshot() {
        if (snapshot == null) {
            snapshot = new QSnapshot(forProperty("snapshot"));
        }
        return snapshot;
    }

}

