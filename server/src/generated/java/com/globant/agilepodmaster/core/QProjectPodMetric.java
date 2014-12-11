package com.globant.agilepodmaster.core;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.globant.agilepodmaster.snapshot.ProjectPodMetric;
import com.mysema.query.types.path.*;
import com.mysema.query.types.PathMetadata;

import javax.annotation.Generated;

import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QProjectPodMetric is a Querydsl query type for ProjectPodMetric
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QProjectPodMetric extends EntityPathBase<ProjectPodMetric> {

    private static final long serialVersionUID = -1016382156L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProjectPodMetric projectPodMetric = new QProjectPodMetric("projectPodMetric");

    public final QAbstractMetric _super;

    //inherited
    public final NumberPath<Long> id;

    protected QPod pod;

    protected QProject project;

    public final NumberPath<Integer> remainingStoryPoints = createNumber("remainingStoryPoints", Integer.class);

    // inherited
    protected QSnapshot snapshot;

    //inherited
    public final NumberPath<Integer> version;

    public QProjectPodMetric(String variable) {
        this(ProjectPodMetric.class, forVariable(variable), INITS);
    }

    public QProjectPodMetric(Path<? extends ProjectPodMetric> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QProjectPodMetric(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QProjectPodMetric(PathMetadata<?> metadata, PathInits inits) {
        this(ProjectPodMetric.class, metadata, inits);
    }

    public QProjectPodMetric(Class<? extends ProjectPodMetric> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QAbstractMetric(type, metadata, inits);
        this.id = _super.id;
        this.pod = inits.isInitialized("pod") ? new QPod(forProperty("pod"), inits.get("pod")) : null;
        this.project = inits.isInitialized("project") ? new QProject(forProperty("project"), inits.get("project")) : null;
        this.version = _super.version;
    }

    public QPod pod() {
        if (pod == null) {
            pod = new QPod(forProperty("pod"));
        }
        return pod;
    }

    public QProject project() {
        if (project == null) {
            project = new QProject(forProperty("project"));
        }
        return project;
    }

    public QSnapshot snapshot() {
        if (snapshot == null) {
            snapshot = new QSnapshot(forProperty("snapshot"));
        }
        return snapshot;
    }

}

