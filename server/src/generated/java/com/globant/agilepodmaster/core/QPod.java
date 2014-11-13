package com.globant.agilepodmaster.core;

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

    private static final long serialVersionUID = -1275646131L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPod pod = new QPod("pod");

    public final QSnapshotEntity _super;

    //inherited
    public final NumberPath<Long> id;

    public final StringPath name = createString("name");

    // inherited
    public final QSnapshot snapshot;

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
        this._super = new QSnapshotEntity(type, metadata, inits);
        this.id = _super.id;
        this.snapshot = _super.snapshot;
        this.version = _super.version;
    }

}
