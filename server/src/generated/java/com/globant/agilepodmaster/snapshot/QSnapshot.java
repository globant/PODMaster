package com.globant.agilepodmaster.snapshot;

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

    private static final long serialVersionUID = -1384721225L;

    public static final QSnapshot snapshot = new QSnapshot("snapshot");

    public final com.globant.agilepodmaster.QAbstractEntity _super = new com.globant.agilepodmaster.QAbstractEntity(this);

    public final DateTimePath<java.util.Date> creationDate = createDateTime("creationDate", java.util.Date.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath name = createString("name");

    public final SetPath<com.globant.agilepodmaster.organization.Organization, com.globant.agilepodmaster.organization.QOrganization> organizations = this.<com.globant.agilepodmaster.organization.Organization, com.globant.agilepodmaster.organization.QOrganization>createSet("organizations", com.globant.agilepodmaster.organization.Organization.class, com.globant.agilepodmaster.organization.QOrganization.class, PathInits.DIRECT2);

    public final SetPath<com.globant.agilepodmaster.pod.PodMember, com.globant.agilepodmaster.pod.QPodMember> podMembers = this.<com.globant.agilepodmaster.pod.PodMember, com.globant.agilepodmaster.pod.QPodMember>createSet("podMembers", com.globant.agilepodmaster.pod.PodMember.class, com.globant.agilepodmaster.pod.QPodMember.class, PathInits.DIRECT2);

    public final SetPath<com.globant.agilepodmaster.pod.Pod, com.globant.agilepodmaster.pod.QPod> pods = this.<com.globant.agilepodmaster.pod.Pod, com.globant.agilepodmaster.pod.QPod>createSet("pods", com.globant.agilepodmaster.pod.Pod.class, com.globant.agilepodmaster.pod.QPod.class, PathInits.DIRECT2);

    public final SetPath<com.globant.agilepodmaster.product.Product, com.globant.agilepodmaster.product.QProduct> products = this.<com.globant.agilepodmaster.product.Product, com.globant.agilepodmaster.product.QProduct>createSet("products", com.globant.agilepodmaster.product.Product.class, com.globant.agilepodmaster.product.QProduct.class, PathInits.DIRECT2);

    public final SetPath<ProjectMetric, QProjectMetric> projectMetrics = this.<ProjectMetric, QProjectMetric>createSet("projectMetrics", ProjectMetric.class, QProjectMetric.class, PathInits.DIRECT2);

    public final SetPath<ProjectPodMetric, QProjectPodMetric> projectPodMetrics = this.<ProjectPodMetric, QProjectPodMetric>createSet("projectPodMetrics", ProjectPodMetric.class, QProjectPodMetric.class, PathInits.DIRECT2);

    public final SetPath<com.globant.agilepodmaster.project.Project, com.globant.agilepodmaster.project.QProject> projects = this.<com.globant.agilepodmaster.project.Project, com.globant.agilepodmaster.project.QProject>createSet("projects", com.globant.agilepodmaster.project.Project.class, com.globant.agilepodmaster.project.QProject.class, PathInits.DIRECT2);

    public final SetPath<com.globant.agilepodmaster.release.Release, com.globant.agilepodmaster.release.QRelease> releases = this.<com.globant.agilepodmaster.release.Release, com.globant.agilepodmaster.release.QRelease>createSet("releases", com.globant.agilepodmaster.release.Release.class, com.globant.agilepodmaster.release.QRelease.class, PathInits.DIRECT2);

    public final SetPath<SprintPodMetric, QSprintPodMetric> sprintPodMetrics = this.<SprintPodMetric, QSprintPodMetric>createSet("sprintPodMetrics", SprintPodMetric.class, QSprintPodMetric.class, PathInits.DIRECT2);

    public final SetPath<com.globant.agilepodmaster.sprint.Sprint, com.globant.agilepodmaster.sprint.QSprint> sprints = this.<com.globant.agilepodmaster.sprint.Sprint, com.globant.agilepodmaster.sprint.QSprint>createSet("sprints", com.globant.agilepodmaster.sprint.Sprint.class, com.globant.agilepodmaster.sprint.QSprint.class, PathInits.DIRECT2);

    public final SetPath<com.globant.agilepodmaster.task.Task, com.globant.agilepodmaster.task.QTask> tasks = this.<com.globant.agilepodmaster.task.Task, com.globant.agilepodmaster.task.QTask>createSet("tasks", com.globant.agilepodmaster.task.Task.class, com.globant.agilepodmaster.task.QTask.class, PathInits.DIRECT2);

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

