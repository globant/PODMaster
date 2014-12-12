package com.globant.agilepodmaster.project;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QProject is a Querydsl query type for Project
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QProject extends EntityPathBase<Project> {

    private static final long serialVersionUID = -905790131L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProject project = new QProject("project");

    public final com.globant.agilepodmaster.snapshot.QSnapshotEntity _super;

    public final StringPath description = createString("description");

    //inherited
    public final NumberPath<Long> id;

    public final StringPath name = createString("name");

    public final NumberPath<Integer> plannedStoryPoints = createNumber("plannedStoryPoints", Integer.class);

    protected com.globant.agilepodmaster.product.QProduct product;

    // inherited
    protected com.globant.agilepodmaster.snapshot.QSnapshot snapshot;

    //inherited
    public final NumberPath<Integer> version;

    public QProject(String variable) {
        this(Project.class, forVariable(variable), INITS);
    }

    public QProject(Path<? extends Project> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QProject(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QProject(PathMetadata<?> metadata, PathInits inits) {
        this(Project.class, metadata, inits);
    }

    public QProject(Class<? extends Project> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new com.globant.agilepodmaster.snapshot.QSnapshotEntity(type, metadata, inits);
        this.id = _super.id;
        this.product = inits.isInitialized("product") ? new com.globant.agilepodmaster.product.QProduct(forProperty("product"), inits.get("product")) : null;
        this.version = _super.version;
    }

    public com.globant.agilepodmaster.product.QProduct product() {
        if (product == null) {
            product = new com.globant.agilepodmaster.product.QProduct(forProperty("product"));
        }
        return product;
    }

    public com.globant.agilepodmaster.snapshot.QSnapshot snapshot() {
        if (snapshot == null) {
            snapshot = new com.globant.agilepodmaster.snapshot.QSnapshot(forProperty("snapshot"));
        }
        return snapshot;
    }

}

