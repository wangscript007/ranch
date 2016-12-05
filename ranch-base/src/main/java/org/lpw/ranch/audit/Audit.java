package org.lpw.ranch.audit;

/**
 * 审核数据标记。
 *
 * @author lpw
 */
public enum Audit {
    /**
     * 待审核。
     */
    Normal(0),
    /**
     * 审核通过。
     */
    Passed(1),
    /**
     * 审核不通过。
     */
    Refused(2);

    private int value;
    private String sql;

    Audit(int value) {
        this.value = value;
        sql = "c_audit=" + value;
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
