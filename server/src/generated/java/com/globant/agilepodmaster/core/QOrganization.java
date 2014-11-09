package com.globant.agilepodmaster.core;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QOrganization is a Querydsl query type for Organization
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QOrganization extends EntityPathBase<Organization> {

    private static final long serialVersionUID = -1267230421L;

    public static final QOrganization organization = new QOrganization("organization");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    public final StringPath description = createString("description");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath name = createString("name");

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QOrganization(String variable) {
        super(Organization.class, forVariable(variable));
    }

    public QOrganization(Path<? extends Organization> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOrganization(PathMetadata<?> metadata) {
        super(Organization.class, metadata);
    }

}

