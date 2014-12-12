package com.globant.agilepodmaster.snapshot;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QSprintPodMetric is a Querydsl query type for SprintPodMetric
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QSprintPodMetric extends EntityPathBase<SprintPodMetric> {

    private static final long serialVersionUID = 1138868488L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSprintPodMetric sprintPodMetric = new QSprintPodMetric("sprintPodMetric");

    public final QAbstractMetric _super;

    public final NumberPath<Integer> acceptedStoryPoints = createNumber("acceptedStoryPoints", Integer.class);

    public final NumberPath<Integer> accumutaledStoryPoints = createNumber("accumutaledStoryPoints", Integer.class);

    //inherited
    public final NumberPath<Long> id;

    public final NumberPath<Integer> numberOfBugs = createNumber("numberOfBugs", Integer.class);

    public final NumberPath<Integer> plannedStoryPoints = createNumber("plannedStoryPoints", Integer.class);

    protected com.globant.agilepodmaster.pod.QPod pod;

    // inherited
    protected QSnapshot snapshot;

    protected com.globant.agilepodmaster.sprint.QSprint sprint;

    //inherited
    public final NumberPath<Integer> version;

    public QSprintPodMetric(String variable) {
        this(SprintPodMetric.class, forVariable(variable), INITS);
    }

    public QSprintPodMetric(Path<? extends SprintPodMetric> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QSprintPodMetric(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QSprintPodMetric(PathMetadata<?> metadata, PathInits inits) {
        this(SprintPodMetric.class, metadata, inits);
    }

    public QSprintPodMetric(Class<? extends SprintPodMetric> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QAbstractMetric(type, metadata, inits);
        this.id = _super.id;
        this.pod = inits.isInitialized("pod") ? new com.globant.agilepodmaster.pod.QPod(forProperty("pod"), inits.get("pod")) : null;
        this.sprint = inits.isInitialized("sprint") ? new com.globant.agilepodmaster.sprint.QSprint(forProperty("sprint"), inits.get("sprint")) : null;
        this.version = _super.version;
    }

    public com.globant.agilepodmaster.pod.QPod pod() {
        if (pod == null) {
            pod = new com.globant.agilepodmaster.pod.QPod(forProperty("pod"));
        }
        return pod;
    }

    public QSnapshot snapshot() {
        if (snapshot == null) {
            snapshot = new QSnapshot(forProperty("snapshot"));
        }
        return snapshot;
    }

    public com.globant.agilepodmaster.sprint.QSprint sprint() {
        if (sprint == null) {
            sprint = new com.globant.agilepodmaster.sprint.QSprint(forProperty("sprint"));
        }
        return sprint;
    }

}
