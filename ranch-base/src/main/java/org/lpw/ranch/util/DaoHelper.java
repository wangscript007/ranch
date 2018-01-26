package org.lpw.ranch.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * DAO操作支持工具。
 *
 * @author lpw
 */
public interface DaoHelper {
    /**
     * 添加WHERE语句。
     *
     * @param where     目标WHERE片段。
     * @param args      目标参数集。
     * @param column    字段名。
     * @param operation 操作类型。
     * @param value     值，为null或空字符串不添加。
     */
    void where(StringBuilder where, List<Object> args, String column, DaoOperation operation, String value);

    /**
     * 添加WHERE语句。
     *
     * @param where     目标WHERE片段。
     * @param args      目标参数集。
     * @param column    字段名。
     * @param operation 操作类型。
     * @param value     值，为null或空字符串不添加。
     * @param and       是否必须添加AND。
     */
    void where(StringBuilder where, List<Object> args, String column, DaoOperation operation, String value, boolean and);

    /**
     * 添加WHERE语句。
     *
     * @param where     目标WHERE片段。
     * @param args      目标参数集。
     * @param column    字段名。
     * @param operation 操作类型。
     * @param value     值，小于0不添加。
     */
    void where(StringBuilder where, List<Object> args, String column, DaoOperation operation, long value);

    /**
     * 添加WHERE语句。
     *
     * @param where     目标WHERE片段。
     * @param args      目标参数集。
     * @param column    字段名。
     * @param operation 操作类型。
     * @param value     值，小于0不添加。
     * @param and       是否必须添加AND。
     */
    void where(StringBuilder where, List<Object> args, String column, DaoOperation operation, long value, boolean and);

    /**
     * 添加WHERE语句。
     *
     * @param where     目标WHERE片段。
     * @param args      目标参数集。
     * @param column    字段名。
     * @param operation 操作类型。
     * @param value     值，null不添加。
     */
    void where(StringBuilder where, List<Object> args, String column, DaoOperation operation, Date value);

    /**
     * 添加WHERE语句。
     *
     * @param where     目标WHERE片段。
     * @param args      目标参数集。
     * @param column    字段名。
     * @param operation 操作类型。
     * @param value     值，null不添加。
     * @param and       是否必须添加AND。
     */
    void where(StringBuilder where, List<Object> args, String column, DaoOperation operation, Date value, boolean and);

    /**
     * 添加WHERE语句。
     *
     * @param where     目标WHERE片段。
     * @param args      目标参数集。
     * @param column    字段名。
     * @param operation 操作类型。
     * @param value     值，null不添加。
     */
    void where(StringBuilder where, List<Object> args, String column, DaoOperation operation, Timestamp value);

    /**
     * 添加WHERE语句。
     *
     * @param where     目标WHERE片段。
     * @param args      目标参数集。
     * @param column    字段名。
     * @param operation 操作类型。
     * @param value     值，null不添加。
     * @param and       是否必须添加AND。
     */
    void where(StringBuilder where, List<Object> args, String column, DaoOperation operation, Timestamp value, boolean and);

    /**
     * 添加WHERE LIKE语句。
     *
     * @param dataSource 数据源KEY。
     * @param where      目标WHERE片段。
     * @param args       目标参数集。
     * @param column     字段名。
     * @param value      值，为null或空字符串不添加。
     */
    void like(String dataSource, StringBuilder where, List<Object> args, String column, String value);

    /**
     * 添加WHERE LIKE语句。
     *
     * @param dataSource 数据源KEY。
     * @param where      目标WHERE片段。
     * @param args       目标参数集。
     * @param column     字段名。
     * @param value      值，为null或空字符串不添加。
     * @param and        是否必须添加AND。
     */
    void like(String dataSource, StringBuilder where, List<Object> args, String column, String value, boolean and);

    /**
     * 添加WHERE LIKE语句。
     *
     * @param dataSource 数据源KEY。
     * @param where      目标WHERE片段。
     * @param args       目标参数集。
     * @param column     字段名。
     * @param value      值，为null或空字符串不添加。
     * @param prefix     是否模糊匹配前部字符串，即是否在参数值前添加%。
     * @param suffix     是否模糊匹配尾部字符串，即是否在参数值末添加%。
     */
    void like(String dataSource, StringBuilder where, List<Object> args, String column, String value, boolean prefix, boolean suffix);

    /**
     * 添加WHERE LIKE语句。
     *
     * @param dataSource 数据源KEY。
     * @param where      目标WHERE片段。
     * @param args       目标参数集。
     * @param column     字段名。
     * @param value      值，为null或空字符串不添加。
     * @param prefix     是否模糊匹配前部字符串，即是否在参数值前添加%。
     * @param suffix     是否模糊匹配尾部字符串，即是否在参数值末添加%。
     * @param and        是否必须添加AND。
     */
    void like(String dataSource, StringBuilder where, List<Object> args, String column, String value,
              boolean prefix, boolean suffix, boolean and);
}
