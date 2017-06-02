package com.orumjek.entity;

import com.orumjek.entity.KeywordIndex;
import com.orumjek.entity.Page;
import com.orumjek.entity.PlannedPage;
import com.orumjek.entity.Visit;
import com.orumjek.entity.VisitedPage;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-02T14:32:26")
@StaticMetamodel(Page.class)
public class Page_ { 

    public static volatile SingularAttribute<Page, String> textData;
    public static volatile SingularAttribute<Page, Double> score;
    public static volatile CollectionAttribute<Page, Visit> visitCollection;
    public static volatile CollectionAttribute<Page, Page> pageCollection;
    public static volatile CollectionAttribute<Page, KeywordIndex> keywordIndexCollection;
    public static volatile SingularAttribute<Page, String> htmlData;
    public static volatile SingularAttribute<Page, Long> id;
    public static volatile CollectionAttribute<Page, Page> pageCollection1;
    public static volatile CollectionAttribute<Page, VisitedPage> visitedPageCollection;
    public static volatile SingularAttribute<Page, String> url;
    public static volatile CollectionAttribute<Page, PlannedPage> plannedPageCollection;

}