
package org.n52.sta.api.path;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.n52.shetland.filter.FilterFilter;
import org.n52.shetland.oasis.odata.query.option.QueryOptions;
import org.n52.shetland.ogc.filter.FilterClause;
import org.n52.shetland.ogc.filter.FilterConstants;
import org.n52.shetland.ogc.filter.FilterConstants.ComparisonOperator;
import org.n52.sta.api.entity.Identifiable;
import org.n52.svalbard.odata.core.QueryOptionsFactory;
import org.n52.svalbard.odata.core.expr.MemberExpr;
import org.n52.svalbard.odata.core.expr.StringValueExpr;
import org.n52.svalbard.odata.core.expr.bool.ComparisonExpr;

public class Request {

    private final Optional<SelectPath< ? extends Identifiable>> selectPath;
    private final QueryOptions queryOptions;

    public Request(SelectPath< ? extends Identifiable> selectPath, QueryOptions queryOptions) {
        Objects.requireNonNull(selectPath, "selectPath must not be null!");
        this.selectPath = Optional.of(selectPath);
        this.queryOptions = queryOptions == null
                ? QueryOptionsFactory.createEmpty()
                : queryOptions;
    }

    private Request(QueryOptions queryOptions) {
        this.queryOptions = queryOptions;
        this.selectPath = Optional.empty();
    }

    /**
     * A Request for an instance with specified id.
     *
     * @param id
     *        the instance's id
     * @return a request to get an instance by its id
     */
    public static Request createIdRequest(String id) {
        MemberExpr leftExpr = new MemberExpr("id");
        StringValueExpr rightExpr = new StringValueExpr(id);
        ComparisonOperator operator = FilterConstants.ComparisonOperator.PropertyIsEqualTo;
        ComparisonExpr expr = new ComparisonExpr(operator, leftExpr, rightExpr);
        Set<FilterClause> filters = Collections.singleton(new FilterFilter(expr));
        QueryOptions query = QueryOptionsFactory.createQueryOptions(filters);
        return new Request(query);
    }

    /**
     * The request path or Optional.empty() if not present.
     *
     * @return the request path
     */
    public Optional<SelectPath< ? extends Identifiable>> getPath() {
        return selectPath;
    }

    public QueryOptions getQueryOptions() {
        return queryOptions;
    }

    public boolean isRefRequest() {
        return selectPath.map(SelectPath::isRef)
                         .orElse(false);
    }

}
