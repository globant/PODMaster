package com.globant.agilepodmaster.pod;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QPod is a Querydsl query type for Pod
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QPod extends EntityPathBase<Pod> {

    private static final long serialVersionUID = 120982565L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPod pod = new QPod("pod");

    public final com.globant.agilepodmaster.snapshot.QSnapshotEntity _super;

    //inherited
    public final NumberPath<Long> id;

    public final StringPath name = createString("name");

    // inherited
    protected com.globant.agilepodmaster.snapshot.QSnapshot snapshot;

    //inherited
    public final NumberPath<Integer> version;

    public QPod(String variable) {
        this(Pod.class, forVariable(variable), INITS);
    }

    public QPod(Path<? extends Pod> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QPod(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QPod(PathMetadata<?> metadata, PathInits inits) {
        this(Pod.class, metadata, inits);
    }

    public QPod(Class<? extends Pod> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new com.globant.agilepodmaster.snapshot.QSnapshotEntity(type, metadata, inits);
        this.id = _super.id;
        this.version = _super.version;
    }

    public com.globant.agilepodmaster.snapshot.QSnapshot snapshot() {
        if (snapshot == null) {
            snapshot = new com.globant.agilepodmaster.snapshot.QSnapshot(forProperty("snapshot"));
        }
        return snapshot;
    }

}

