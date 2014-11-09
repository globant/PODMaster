package com.globant.agilepodmaster.core;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QTask is a Querydsl query type for Task
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTask extends EntityPathBase<Task> {

    private static final long serialVersionUID = -890218115L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTask task = new QTask("task");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    public final NumberPath<Double> accuracy = createNumber("accuracy", Double.class);

    public final NumberPath<Double> actual = createNumber("actual", Double.class);

    public final DateTimePath<java.util.Date> createdDate = createDateTime("createdDate", java.util.Date.class);

    public final NumberPath<Double> effort = createNumber("effort", Double.class);

    public final NumberPath<Double> estimated = createNumber("estimated", Double.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath name = createString("name");

    public final QPodMember owner;

    public final QTask parentTask;

    public final EnumPath<Task.Priority> priority = createEnum("priority", Task.Priority.class);

    public final QRelease release;

    public final NumberPath<Double> remaining = createNumber("remaining", Double.class);

    public final EnumPath<Task.Severity> severity = createEnum("severity", Task.Severity.class);

    public final QSprint sprint;

    public final EnumPath<Task.Status> status = createEnum("status", Task.Status.class);

    public final EnumPath<Task.Type> type = createEnum("type", Task.Type.class);

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QTask(String variable) {
        this(Task.class, forVariable(variable), INITS);
    }

    public QTask(Path<? extends Task> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QTask(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QTask(PathMetadata<?> metadata, PathInits inits) {
        this(Task.class, metadata, inits);
    }

    public QTask(Class<? extends Task> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.owner = inits.isInitialized("owner") ? new QPodMember(forProperty("owner"), inits.get("owner")) : null;
        this.parentTask = inits.isInitialized("parentTask") ? new QTask(forProperty("parentTask"), inits.get("parentTask")) : null;
        this.release = inits.isInitialized("release") ? new QRelease(forProperty("release"), inits.get("release")) : null;
        this.sprint = inits.isInitialized("sprint") ? new QSprint(forProperty("sprint"), inits.get("sprint")) : null;
    }

}

