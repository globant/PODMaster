package com.globant.agilepodmaster.core;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QPodMember is a Querydsl query type for PodMember
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QPodMember extends EntityPathBase<PodMember> {

    private static final long serialVersionUID = 1442789447L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPodMember podMember = new QPodMember("podMember");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    public final StringPath email = createString("email");

    public final StringPath firstName = createString("firstName");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath lastName = createString("lastName");

    public final QPod pod;

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QPodMember(String variable) {
        this(PodMember.class, forVariable(variable), INITS);
    }

    public QPodMember(Path<? extends PodMember> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QPodMember(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QPodMember(PathMetadata<?> metadata, PathInits inits) {
        this(PodMember.class, metadata, inits);
    }

    public QPodMember(Class<? extends PodMember> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.pod = inits.isInitialized("pod") ? new QPod(forProperty("pod")) : null;
    }

}

