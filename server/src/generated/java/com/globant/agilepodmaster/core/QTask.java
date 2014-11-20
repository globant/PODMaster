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

    public final QSnapshotEntity _super;

    public final BooleanPath accepted = createBoolean("accepted");

    public final NumberPath<Double> accuracy = createNumber("accuracy", Double.class);

    public final NumberPath<Integer> actual = createNumber("actual", Integer.class);

    public final BooleanPath bug = createBoolean("bug");

    public final DateTimePath<java.util.Date> createdDate = createDateTime("createdDate", java.util.Date.class);

    public final NumberPath<Double> effort = createNumber("effort", Double.class);

    public final NumberPath<Integer> estimated = createNumber("estimated", Integer.class);

    //inherited
    public final NumberPath<Long> id;

    public final StringPath name = createString("name");

    public final BooleanPath open = createBoolean("open");

    protected QPodMember owner;

    protected QTask parentTask;

    public final EnumPath<Task.Priority> priority = createEnum("priority", Task.Priority.class);

    protected QRelease release;

    public final NumberPath<Integer> remaining = createNumber("remaining", Integer.class);

    public final EnumPath<Task.Severity> severity = createEnum("severity", Task.Severity.class);

    // inherited
    protected QSnapshot snapshot;

    protected QSprint sprint;

    public final EnumPath<Task.Status> status = createEnum("status", Task.Status.class);

    public final EnumPath<Task.Type> type = createEnum("type", Task.Type.class);

    //inherited
    public final NumberPath<Integer> version;

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
        this._super = new QSnapshotEntity(type, metadata, inits);
        this.id = _super.id;
        this.owner = inits.isInitialized("owner") ? new QPodMember(forProperty("owner"), inits.get("owner")) : null;
        this.parentTask = inits.isInitialized("parentTask") ? new QTask(forProperty("parentTask"), inits.get("parentTask")) : null;
        this.release = inits.isInitialized("release") ? new QRelease(forProperty("release"), inits.get("release")) : null;
        this.sprint = inits.isInitialized("sprint") ? new QSprint(forProperty("sprint"), inits.get("sprint")) : null;
        this.version = _super.version;
    }

    public QPodMember owner() {
        if (owner == null) {
            owner = new QPodMember(forProperty("owner"));
        }
        return owner;
    }

    public QTask parentTask() {
        if (parentTask == null) {
            parentTask = new QTask(forProperty("parentTask"));
        }
        return parentTask;
    }

    public QRelease release() {
        if (release == null) {
            release = new QRelease(forProperty("release"));
        }
        return release;
    }

    public QSnapshot snapshot() {
        if (snapshot == null) {
            snapshot = new QSnapshot(forProperty("snapshot"));
        }
        return snapshot;
    }

    public QSprint sprint() {
        if (sprint == null) {
            sprint = new QSprint(forProperty("sprint"));
        }
        return sprint;
    }

}

