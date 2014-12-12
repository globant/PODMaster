package com.globant.agilepodmaster.dashboard;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QDashboardWidget is a Querydsl query type for DashboardWidget
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QDashboardWidget extends EntityPathBase<DashboardWidget> {

    private static final long serialVersionUID = -631499929L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDashboardWidget dashboardWidget = new QDashboardWidget("dashboardWidget");

    public final com.globant.agilepodmaster.QAbstractEntity _super = new com.globant.agilepodmaster.QAbstractEntity(this);

    public final BooleanPath collapsed = createBoolean("collapsed");

    public final QDashboard dashboard;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public final com.globant.agilepodmaster.widget.QWidget widget;

    public QDashboardWidget(String variable) {
        this(DashboardWidget.class, forVariable(variable), INITS);
    }

    public QDashboardWidget(Path<? extends DashboardWidget> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QDashboardWidget(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QDashboardWidget(PathMetadata<?> metadata, PathInits inits) {
        this(DashboardWidget.class, metadata, inits);
    }

    public QDashboardWidget(Class<? extends DashboardWidget> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.dashboard = inits.isInitialized("dashboard") ? new QDashboard(forProperty("dashboard")) : null;
        this.widget = inits.isInitialized("widget") ? new com.globant.agilepodmaster.widget.QWidget(forProperty("widget")) : null;
    }

}

