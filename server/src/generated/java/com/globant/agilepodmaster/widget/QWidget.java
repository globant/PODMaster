package com.globant.agilepodmaster.widget;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QWidget is a Querydsl query type for Widget
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QWidget extends EntityPathBase<Widget> {

    private static final long serialVersionUID = 359127991L;

    public static final QWidget widget = new QWidget("widget");

    public final com.globant.agilepodmaster.QAbstractEntity _super = new com.globant.agilepodmaster.QAbstractEntity(this);

    public final NumberPath<Integer> height = createNumber("height", Integer.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath name = createString("name");

    public final StringPath title = createString("title");

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public final StringPath viewName = createString("viewName");

    public final NumberPath<Integer> width = createNumber("width", Integer.class);

    public QWidget(String variable) {
        super(Widget.class, forVariable(variable));
    }

    public QWidget(Path<? extends Widget> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWidget(PathMetadata<?> metadata) {
        super(Widget.class, metadata);
    }

}

