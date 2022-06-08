package org.n52.sta.api.path;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import org.n52.shetland.filter.FilterFilter;
import org.n52.shetland.oasis.odata.query.option.QueryOptions;
import org.n52.shetland.ogc.filter.FilterConstants;
import org.n52.svalbard.odata.core.QueryOptionsFactory;
import org.n52.svalbard.odata.core.expr.MemberExpr;
import org.n52.svalbard.odata.core.expr.StringValueExpr;
import org.n52.svalbard.odata.core.expr.bool.ComparisonExpr;

public class Request {

    private final Optional<ODataPath> path;
    private final QueryOptions queryOptions;

    public Request(ODataPath path, QueryOptions queryOptions) {
        Objects.requireNonNull(path, "path must not be null!");
        this.path = Optional.of(path);
        this.queryOptions = queryOptions == null
                ? QueryOptionsFactory.createEmpty()
                : queryOptions;
    }

    private Request(QueryOptions queryOptions) {
        this.queryOptions = queryOptions;
        this.path = Optional.empty();
    }

    /**
     * A Request for an instance with specified id.
     *
     * @param id the instance's id
     * @return a request to get an instance by its id
     */
    public static Request createIdRequest(String id) {
        QueryOptions query =
                QueryOptionsFactory.createQueryOptions(
                        Collections.singleton(new FilterFilter(
                                new ComparisonExpr(FilterConstants.ComparisonOperator.PropertyIsEqualTo,
                                                   new MemberExpr("id"),
                                                   new StringValueExpr(id)))));
        return new Request(query);
    }

    /**
     * The request path or Optional.empty() if not present.
     *
     * @return the request path
     */
    public Optional<ODataPath> getPath() {
        return path;
    }

    public QueryOptions getQueryOptions() {
        return queryOptions;
    }

}
