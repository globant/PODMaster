package com.globant.agilepodmaster.core;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QSnapshot is a Querydsl query type for Snapshot
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QSnapshot extends EntityPathBase<Snapshot> {

    private static final long serialVersionUID = -376291908L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSnapshot snapshot = new QSnapshot("snapshot");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    public final DateTimePath<java.util.Date> creationDate = createDateTime("creationDate", java.util.Date.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath name = createString("name");

    public final ListPath<PodMember, QPodMember> podMembers = this.<PodMember, QPodMember>createList("podMembers", PodMember.class, QPodMember.class, PathInits.DIRECT2);

    public final ListPath<Pod, QPod> pods = this.<Pod, QPod>createList("pods", Pod.class, QPod.class, PathInits.DIRECT2);

    public final QProduct product;

    public final ListPath<Release, QRelease> releases = this.<Release, QRelease>createList("releases", Release.class, QRelease.class, PathInits.DIRECT2);

    public final SetPath<SprintPodMetric, QSprintPodMetric> sprintMetrics = this.<SprintPodMetric, QSprintPodMetric>createSet("sprintMetrics", SprintPodMetric.class, QSprintPodMetric.class, PathInits.DIRECT2);

    public final ListPath<Sprint, QSprint> sprints = this.<Sprint, QSprint>createList("sprints", Sprint.class, QSprint.class, PathInits.DIRECT2);

    public final ListPath<Task, QTask> tasks = this.<Task, QTask>createList("tasks", Task.class, QTask.class, PathInits.DIRECT2);

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QSnapshot(String variable) {
        this(Snapshot.class, forVariable(variable), INITS);
    }

    public QSnapshot(Path<? extends Snapshot> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QSnapshot(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QSnapshot(PathMetadata<?> metadata, PathInits inits) {
        this(Snapshot.class, metadata, inits);
    }

    public QSnapshot(Class<? extends Snapshot> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

