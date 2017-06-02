package com.orumjek.entity;

import com.orumjek.entity.Page;
import com.orumjek.entity.PlannedPage;
import com.orumjek.entity.VisitedPage;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-02T14:32:26")
@StaticMetamodel(Visit.class)
public class Visit_ { 

    public static volatile SingularAttribute<Visit, Page> startPage;
    public static volatile SingularAttribute<Visit, Short> depth;
    public static volatile SingularAttribute<Visit, Long> id;
    public static volatile CollectionAttribute<Visit, VisitedPage> visitedPageCollection;
    public static volatile CollectionAttribute<Visit, PlannedPage> plannedPageCollection;

}