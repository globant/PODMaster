package com.globant.agilepodmaster.core;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QSnapshot is a Querydsl query type for Snapshot
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QSnapshot extends EntityPathBase<Snapshot> {

    private static final long serialVersionUID = -376291908L;

    public static final QSnapshot snapshot = new QSnapshot("snapshot");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    public final DateTimePath<java.util.Date> creationDate = createDateTime("creationDate", java.util.Date.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath name = createString("name");

    public final SetPath<Organization, QOrganization> organizations = this.<Organization, QOrganization>createSet("organizations", Organization.class, QOrganization.class, PathInits.DIRECT2);

    public final SetPath<PodMember, QPodMember> podMembers = this.<PodMember, QPodMember>createSet("podMembers", PodMember.class, QPodMember.class, PathInits.DIRECT2);

    public final SetPath<Pod, QPod> pods = this.<Pod, QPod>createSet("pods", Pod.class, QPod.class, PathInits.DIRECT2);

    public final SetPath<Product, QProduct> products = this.<Product, QProduct>createSet("products", Product.class, QProduct.class, PathInits.DIRECT2);

    public final SetPath<ProjectMetric, QProjectMetric> projectMetrics = this.<ProjectMetric, QProjectMetric>createSet("projectMetrics", ProjectMetric.class, QProjectMetric.class, PathInits.DIRECT2);

    public final SetPath<ProjectPodMetric, QProjectPodMetric> projectPodMetrics = this.<ProjectPodMetric, QProjectPodMetric>createSet("projectPodMetrics", ProjectPodMetric.class, QProjectPodMetric.class, PathInits.DIRECT2);

    public final SetPath<Project, QProject> projects = this.<Project, QProject>createSet("projects", Project.class, QProject.class, PathInits.DIRECT2);

    public final SetPath<Release, QRelease> releases = this.<Release, QRelease>createSet("releases", Release.class, QRelease.class, PathInits.DIRECT2);

    public final SetPath<SprintPodMetric, QSprintPodMetric> sprintPodMetrics = this.<SprintPodMetric, QSprintPodMetric>createSet("sprintPodMetrics", SprintPodMetric.class, QSprintPodMetric.class, PathInits.DIRECT2);

    public final SetPath<Sprint, QSprint> sprints = this.<Sprint, QSprint>createSet("sprints", Sprint.class, QSprint.class, PathInits.DIRECT2);

    public final SetPath<Task, QTask> tasks = this.<Task, QTask>createSet("tasks", Task.class, QTask.class, PathInits.DIRECT2);

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QSnapshot(String variable) {
        super(Snapshot.class, forVariable(variable));
    }

    public QSnapshot(Path<? extends Snapshot> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSnapshot(PathMetadata<?> metadata) {
        super(Snapshot.class, metadata);
    }

}

