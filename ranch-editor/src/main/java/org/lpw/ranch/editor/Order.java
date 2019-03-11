package org.lpw.ranch.editor;

/**
 * @author lpw
 */
public enum Order {
    /**
     * 热门。
     */
    Sort("c_sort,c_used desc"),
    /**
     * 热门。
     */
    Hot("c_used desc"),
    /**
     * 最新。
     */
    Newest("c_modify desc"),
    /**
     * 不排序。
     */
    None("");

    private String by;

    Order(String by) {
        this.by = by;
    }

    /**
     * 获取Order By值。
     *
     * @return Order By值。
     */
    public String by() {
        return by;
    }

    /**
     * 查找排序规则。
     *
     * @param name         名称。
     * @param defaultOrder 默认排序规则。
     * @return 排序规则。
     */
    static Order find(String name, Order defaultOrder) {
        if (name == null)
            return defaultOrder;

        switch (name) {
            case "sort":
                return Sort;
            case "hot":
                return Hot;
            case "newest":
                return Newest;
            case "none":
                return None;
            default:
                return defaultOrder;
        }
    }
}
