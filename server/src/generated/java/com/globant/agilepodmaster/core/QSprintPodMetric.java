package com.globant.agilepodmaster.core;

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

    private static final long serialVersionUID = -1506535325L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSprintPodMetric sprintPodMetric = new QSprintPodMetric("sprintPodMetric");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    public final NumberPath<Integer> acceptedStoryPoints = createNumber("acceptedStoryPoints", Integer.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final QPod pod;

    public final QSprint sprint;

    //inherited
    public final NumberPath<Integer> version = _super.version;

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
        this.pod = inits.isInitialized("pod") ? new QPod(forProperty("pod"), inits.get("pod")) : null;
        this.sprint = inits.isInitialized("sprint") ? new QSprint(forProperty("sprint"), inits.get("sprint")) : null;
    }

}
