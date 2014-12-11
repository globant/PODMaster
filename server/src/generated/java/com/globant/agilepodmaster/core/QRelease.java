package com.globant.agilepodmaster.core;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QRelease is a Querydsl query type for Release
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QRelease extends EntityPathBase<Release> {

    private static final long serialVersionUID = -731848433L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRelease release = new QRelease("release");

    public final QSnapshotEntity _super;

    //inherited
    public final NumberPath<Long> id;

    public final StringPath name = createString("name");

    public final NumberPath<Integer> number = createNumber("number", Integer.class);

    protected QProject project;

    // inherited
    protected QSnapshot snapshot;

    //inherited
    public final NumberPath<Integer> version;

    public QRelease(String variable) {
        this(Release.class, forVariable(variable), INITS);
    }

    public QRelease(Path<? extends Release> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QRelease(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QRelease(PathMetadata<?> metadata, PathInits inits) {
        this(Release.class, metadata, inits);
    }

    public QRelease(Class<? extends Release> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QSnapshotEntity(type, metadata, inits);
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

