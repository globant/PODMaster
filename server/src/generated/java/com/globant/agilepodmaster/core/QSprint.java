package com.globant.agilepodmaster.core;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QSprint is a Querydsl query type for Sprint
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QSprint extends EntityPathBase<Sprint> {

    private static final long serialVersionUID = -815921134L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSprint sprint = new QSprint("sprint");

    public final QSnapshotEntity _super;

    public final DateTimePath<java.util.Date> endDate = createDateTime("endDate", java.util.Date.class);

    //inherited
    public final NumberPath<Long> id;

    public final StringPath name = createString("name");

    public final NumberPath<Integer> number = createNumber("number", Integer.class);

    public final QRelease release;

    // inherited
    public final QSnapshot snapshot;

    public final DateTimePath<java.util.Date> startDate = createDateTime("startDate", java.util.Date.class);

    //inherited
    public final NumberPath<Integer> version;

    public QSprint(String variable) {
        this(Sprint.class, forVariable(variable), INITS);
    }

    public QSprint(Path<? extends Sprint> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QSprint(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QSprint(PathMetadata<?> metadata, PathInits inits) {
        this(Sprint.class, metadata, inits);
    }

    public QSprint(Class<? extends Sprint> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QSnapshotEntity(type, metadata, inits);
        this.id = _super.id;
        this.release = inits.isInitialized("release") ? new QRelease(forProperty("release"), inits.get("release")) : null;
        this.snapshot = _super.snapshot;
        this.version = _super.version;
    }

}

