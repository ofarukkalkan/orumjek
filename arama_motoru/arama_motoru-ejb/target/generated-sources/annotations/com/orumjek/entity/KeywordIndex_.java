package com.orumjek.entity;

import com.orumjek.entity.Keyword;
import com.orumjek.entity.KeywordIndexPK;
import com.orumjek.entity.Page;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-02T14:32:26")
@StaticMetamodel(KeywordIndex.class)
public class KeywordIndex_ { 

    public static volatile SingularAttribute<KeywordIndex, KeywordIndexPK> keywordIndexPK;
    public static volatile SingularAttribute<KeywordIndex, Page> page;
    public static volatile SingularAttribute<KeywordIndex, Keyword> keyword;

}