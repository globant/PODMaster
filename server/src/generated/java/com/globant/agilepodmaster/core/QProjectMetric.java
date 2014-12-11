package com.globant.agilepodmaster.core;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.globant.agilepodmaster.snapshot.ProjectMetric;
import com.mysema.query.types.path.*;
import com.mysema.query.types.PathMetadata;

import javax.annotation.Generated;

import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QProjectMetric is a Querydsl query type for ProjectMetric
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QProjectMetric extends EntityPathBase<ProjectMetric> {

    private static final long serialVersionUID = 11897137L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProjectMetric projectMetric = new QProjectMetric("projectMetric");

    public final QAbstractMetric _super;

    public final NumberPath<Double> actualPercentComplete = createNumber("actualPercentComplete", Double.class);

    //inherited
    public final NumberPath<Long> id;

    protected QProject project;

    public final NumberPath<Integer> remainingStoryPoints = createNumber("remainingStoryPoints", Integer.class);

    // inherited
    protected QSnapshot snapshot;

    //inherited
    public final NumberPath<Integer> version;

    public QProjectMetric(String variable) {
        this(ProjectMetric.class, forVariable(variable), INITS);
    }

    public QProjectMetric(Path<? extends ProjectMetric> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QProjectMetric(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QProjectMetric(PathMetadata<?> metadata, PathInits inits) {
        this(ProjectMetric.class, metadata, inits);
    }

    public QProjectMetric(Class<? extends ProjectMetric> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QAbstractMetric(type, metadata, inits);
        this.id = _super.id;
        this.project = inits.isInitialized("project") ? new QProject(forProperty("project"), inits.get("project")) : null;
        this.version = _super.version;
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

