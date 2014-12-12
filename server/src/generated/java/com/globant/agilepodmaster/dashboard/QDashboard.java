package com.globant.agilepodmaster.dashboard;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QDashboard is a Querydsl query type for Dashboard
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QDashboard extends EntityPathBase<Dashboard> {

    private static final long serialVersionUID = 788695875L;

    public static final QDashboard dashboard = new QDashboard("dashboard");

    public final com.globant.agilepodmaster.QAbstractEntity _super = new com.globant.agilepodmaster.QAbstractEntity(this);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final BooleanPath locked = createBoolean("locked");

    public final EnumPath<Dashboard.DashboardType> type = createEnum("type", Dashboard.DashboardType.class);

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public final SetPath<DashboardWidget, QDashboardWidget> widgets = this.<DashboardWidget, QDashboardWidget>createSet("widgets", DashboardWidget.class, QDashboardWidget.class, PathInits.DIRECT2);

    public QDashboard(String variable) {
        super(Dashboard.class, forVariable(variable));
    }

    public QDashboard(Path<? extends Dashboard> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDashboard(PathMetadata<?> metadata) {
        super(Dashboard.class, metadata);
    }

}

