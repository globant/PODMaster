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

    public final QAbstractEntity _super = new QAbstractEntity(this);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath name = createString("name");

    public final QSprint sprint;

    public final EnumPath<Pod.Type> type = createEnum("type", Pod.Type.class);

    //inherited
    public final NumberPath<Integer> version = _super.version;

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
        this.sprint = inits.isInitialized("sprint") ? new QSprint(forProperty("sprint"), inits.get("sprint")) : null;
    }

}

