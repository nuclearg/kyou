package net.nuclearg.kyou.dom.query;

/**
 * 查询，用来选择某种满足条件的报文节点
 * 
 * @author ng
 * 
 */
public class KyouQuery {
    private String query;

    public KyouQuery(String query) {
        this.query = query;
    }

    public boolean matches(String path) {
        return this.query.equals(path);
    }

}
