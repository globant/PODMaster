package com.globant.agilepodmaster.core;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QPod is a Querydsl query type for Pod
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QPod extends EntityPathBase<Pod> {

    private static final long serialVersionUID = -1275646131L;

    public static final QPod pod = new QPod("pod");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath name = createString("name");

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QPod(String variable) {
        super(Pod.class, forVariable(variable));
    }

    public QPod(Path<? extends Pod> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPod(PathMetadata<?> metadata) {
        super(Pod.class, metadata);
    }

}

