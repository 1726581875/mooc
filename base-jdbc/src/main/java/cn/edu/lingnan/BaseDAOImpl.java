package cn.edu.lingnan;

import cn.edu.lingnan.util.NameUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Date;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiaomingzhang
 * @date 2021/12/29
 */
public class BaseDAOImpl implements BaseDAO {

    private final static Set<Class<?>> supportTypeSet = new HashSet<>(Arrays.asList(String.class, Date.class));

    private static final String INSERT_TEMPLATE = "insert into `%s`(%s) values (%s)";

    private static final String CREATE_TABLE_SQL_TEMPLATE = "create table `%s` (%) ";

    /**
     * 存储列名对应的列信息
     */
    private static ThreadLocal<Map<String, FieldDetail>> fieldDetailMapThreadLocal = new ThreadLocal<>();


    private static final Logger log = LoggerFactory.getLogger(BaseDAOImpl.class);

    public BaseDAOImpl(){

    }

    @Override
    public <T> List<T> getList(String sql, Class<T> type) {
        List<T> resultList = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement prepareStatement = connection.prepareStatement(sql);
             ResultSet resultSet = prepareStatement.executeQuery()) {
            while (resultSet.next()) {
                resultList.add(analyzeResult(resultSet, type));
            }
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }



    private <T> T analyzeResult(ResultSet resultSet, Class<T> type) throws Exception {
        if (isMappingSupportType(type)) {
            return (T)resultSet.getObject(1);
        } else {
            T resultInstance = type.getConstructor().newInstance();
            Field[] declaredFields = type.getDeclaredFields();
            Set<String> tableColumnNameSet = getTableColumnNameSet(resultSet);
            for (Field field : declaredFields) {
                if (isMappingSupportType(field.getType())) {
                    // java列名转数据库命名规则，按驼峰对应“_”规则转换
                    String fieldName = NameUtil.convertToDataBaseRule(field.getName());
                    // 字段匹配，存在的列才获取结果并赋值,不存在的列则不做处理保持为null
                    if (tableColumnNameSet.contains(fieldName)) {
                        Object value = resultSet.getObject(fieldName);
                        if (Objects.nonNull(value)) {
                            // todo 通过setxxx方法设置值
                            field.setAccessible(true);
                            field.set(resultInstance, convertValue(field, value));
                        }
                    }
                }
            }
            return resultInstance;
        }
    }


    private Object convertValue(Field field, Object value) {
        // h2数据库tinyint查询结果对应java的byte类型，想使用Integer接收在此处做转换
        if (value instanceof Byte) {
            if(field.getType().equals(Integer.class) || field.getType().equals(Integer.TYPE)) {
                value = ((Byte) value).intValue();
            }
        }
        return value;
    }


    @Override
    public <T> T getOne(String sql, Class<T> type) {
        List<T> resultList = getList(sql, type);
        if (resultList.size() == 0) {
            return null;
        }
        if (resultList.size() > 1) {
            throw new RuntimeException("返回结果行数大于1, 行数为" + resultList.size() + "");
        }
        return resultList.get(0);
    }

    @Override
    public <T> int insert(T obj) {
        if (obj == null) {
            throw new RuntimeException("插入元素为空, 请检查参数");
        }
        String insertSql = getPrepareSQL(obj);
        log.debug(insertSql);
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(insertSql)) {
            return execInsert(statement, obj);
        } catch (Exception e) {
            log.error("插入失败", e);
            throw new RuntimeException("数据库插入失败:" + e.getMessage());
        } finally {
            fieldDetailMapThreadLocal.remove();
        }
    }

    int execInsert(PreparedStatement statement, Object obj) throws SQLException, IllegalAccessException {
        setParam(statement, obj);
        return statement.executeUpdate();
    }

    private void setParam(PreparedStatement statement, Object object) throws SQLException, IllegalAccessException {
        Map<String, FieldDetail> fieldDetailMap = fieldDetailMapThreadLocal.get();
        Class<?> objectClass = object.getClass();
        Field[] declaredFields = objectClass.getDeclaredFields();
        for (Field field : declaredFields) {
            String fieldName = field.getName();
            FieldDetail fieldDetail = fieldDetailMap.get(fieldName);
            if (fieldDetail != null) {
                // todo 改为getxxx方法获取
                field.setAccessible(true);
                Object value = field.get(object);
                if (value != null) {
                    log.debug("参数{} ==> [{}]", fieldDetail.getParamIndex(), value);
                    statement.setObject(fieldDetail.getParamIndex(), value);
                }
            }
        }
    }


    @Override
    public <T> int batchInsert(List<T> objectList) throws SQLException {
        if (objectList == null || objectList.isEmpty()) {
            log.warn("插入元素为空, 请检查参数");
            return 0;
        }

        Connection conn = ConnectionManager.getConnection();
        PreparedStatement statement = null;
        int successRow = 0;
        try {
            // 设置不自动提交事务
            conn.setAutoCommit(false);
            Iterator<T> iterator = objectList.iterator();
            while (iterator.hasNext()) {
                T obj = iterator.next();
                statement = conn.prepareStatement(getPrepareSQL(obj));
                successRow += execInsert(statement, obj);
            }
            // 批量插入完成，提交事务
            conn.commit();
        } catch (Exception e) {
            // 出现异常，事务回滚
            if (conn != null && !conn.isClosed()) {
                conn.rollback();
            }
            log.error("批量插入发生异常，当前执行列表报错下标:{}",successRow + 1 , e);
            throw new RuntimeException(e);
        } finally {
            fieldDetailMapThreadLocal.remove();

            if (statement != null) {
                statement.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return successRow;
    }

    @Override
    public <T> int batchDeleteByIds(List<T> ids) {

        


        return 0;
    }

    @Override
    public <T> void createTable(Class<T> type) {

    }

    private String getPrepareSQL(Object object){

        Class<?> objectClass = object.getClass();
        Field[] fields = objectClass.getDeclaredFields();
        List<String> columnList = new ArrayList<>(fields.length);

        int paramIndex = 1;
        for (int i = 0; i < fields.length; i++) {
            try {
                // todo 后续改为通过getXXX方法获取列的值
                fields[i].setAccessible(true);
                if(fields[i].get(object) == null){
                    continue;
                }
            } catch (IllegalAccessException e) {
                continue;
            }
            String column = NameUtil.convertToDataBaseRule(fields[i].getName());
            columnList.add(NameUtil.around(column, "`"));

            // 记录列参数信息到ThreadLocal
            recordFieldDetail(new FieldDetail(fields[i].getName(), paramIndex++, 0,  fields[i].getClass()));
        }

        String tableNameStr = NameUtil.convertToDataBaseRule(objectClass.getSimpleName());

        String columnStr = columnList.stream().collect(Collectors.joining(","));

        // 拼接预编译sql片段
       return String.format(INSERT_TEMPLATE, tableNameStr, columnStr, getPlaceholder(paramIndex -1));
    }

    private void recordFieldDetail(FieldDetail fieldDetail) {
        Map<String, FieldDetail> fieldDetailMap = fieldDetailMapThreadLocal.get();
        if(fieldDetailMap == null) {
            fieldDetailMap = new HashMap<>();
            fieldDetailMapThreadLocal.set(fieldDetailMap);
        }
        fieldDetailMap.put(fieldDetail.getFieldName(), fieldDetail);
    }



    /**
     * 获取占位符片段
     * 例如 ==> "?,?,?,?"
     * @param num
     * @return
     */
    private String getPlaceholder(int num) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < num; i++) {
            str.append("?");
            if (i != num - 1) {
                str.append(",");
            }
        }
        return str.toString();
    }




    private Set<String> getTableColumnNameSet(ResultSet resultSet) throws SQLException {
        Set<String> fieldNameSet = new HashSet<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            fieldNameSet.add(metaData.getColumnLabel(i));
        }
        return fieldNameSet;
    }

    /**
     * 获取数据库支持的映射类型
     *
     * @param clazz
     * @return
     */
    public static boolean isMappingSupportType(Class clazz) {
        return isBaseType(clazz) || supportTypeSet.contains(clazz);
    }




    /**
     * 判断是否是基础数据类型或者基础类型的包装类型
     *
     * @param clazz
     * @return
     */
    public static boolean isBaseType(Class clazz) {
        try {
            return clazz.isPrimitive() || ((Class) clazz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isMappingType(Class clazz){

        return false;
    }


}
