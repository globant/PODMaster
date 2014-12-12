package com.globant.agilepodmaster.release;

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

    private static final long serialVersionUID = -1477990103L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRelease release = new QRelease("release");

    public final com.globant.agilepodmaster.snapshot.QSnapshotEntity _super;

    //inherited
    public final NumberPath<Long> id;

    public final StringPath name = createString("name");

    public final NumberPath<Integer> number = createNumber("number", Integer.class);

    protected com.globant.agilepodmaster.project.QProject project;

    // inherited
    protected com.globant.agilepodmaster.snapshot.QSnapshot snapshot;

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
        this._super = new com.globant.agilepodmaster.snapshot.QSnapshotEntity(type, metadata, inits);
        this.id = _super.id;
        this.project = inits.isInitialized("project") ? new com.globant.agilepodmaster.project.QProject(forProperty("project"), inits.get("project")) : null;
        this.version = _super.version;
    }

    public com.globant.agilepodmaster.project.QProject project() {
        if (project == null) {
            project = new com.globant.agilepodmaster.project.QProject(forProperty("project"));
        }
        return project;
    }

    public com.globant.agilepodmaster.snapshot.QSnapshot snapshot() {
        if (snapshot == null) {
            snapshot = new com.globant.agilepodmaster.snapshot.QSnapshot(forProperty("snapshot"));
        }
        return snapshot;
    }

}

