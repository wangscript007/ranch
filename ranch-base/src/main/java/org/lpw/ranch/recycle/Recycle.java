package org.lpw.ranch.recycle;

/**
 * 回收站数据标记。
 *
 * @author lpw
 */
public enum Recycle {
    /**
     * 非回收站数据。
     */
    No(0),
    /**
     * 回收站数据。
     */
    Yes(1);

    private int value;
    private String sql;

    Recycle(int value) {
        this.value = value;
        sql = "c_recycle=" + value;
    }

    /**
     * 获取数据值。
     *
     * @return 数据值。
     */
    public int getValue() {
        return value;
    }

    /**
     * 获取SQL表达式。
     *
     * @return SQL表达式。
     */
    public String getSql() {
        return sql;
    }
}
