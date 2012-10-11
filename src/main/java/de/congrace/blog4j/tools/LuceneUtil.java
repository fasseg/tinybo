package de.congrace.blog4j.tools;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.Query;

public class LuceneUtil {
    public static Query createLuceneQuery(String searchString, String... fields) throws ParseException {
        BooleanQuery orQuery = new BooleanQuery();
        for (String field : fields) {
            FuzzyQuery q = new FuzzyQuery(new Term(field, searchString));
            orQuery.add(q, BooleanClause.Occur.SHOULD);
        }
        return orQuery;
    }
}
