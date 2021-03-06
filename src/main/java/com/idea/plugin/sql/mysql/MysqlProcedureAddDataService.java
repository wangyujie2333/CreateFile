package com.idea.plugin.sql.mysql;

import com.idea.plugin.sql.BaseProcedureService;
import com.idea.plugin.sql.IProcedureService;
import com.idea.plugin.sql.support.TableInfoVO;
import com.idea.plugin.sql.support.exception.SqlException;
import com.idea.plugin.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MysqlProcedureAddDataService extends BaseProcedureService {

    public void addProcedure(String path, TableInfoVO tableInfoVO) throws SqlException {
        if (StringUtils.isEmpty(path)) {
            return;
        }
        IProcedureService procedureService = new MysqlProcedureAddData();
        String comment = StringUtils.isEmpty(tableInfoVO.comment) ? tableInfoVO.tableComment + "新增数据" : tableInfoVO.comment;
        FileUtils.writeFile(path, String.format(procedureService.getComment(), comment));
        String[] columnNameArr = tableInfoVO.insertColumnName.split(",");
        String columnNameDeclare = Arrays.stream(columnNameArr)
                .map(columnName -> "    DECLARE V_" + columnName.trim() + " TINYTEXT;").collect(Collectors.joining("\n"));
        String columnNameInto = Arrays.stream(columnNameArr)
                .map(columnName -> "V_" + columnName.trim()).collect(Collectors.joining(", "));
        String columnNameValue = Arrays.stream(Arrays.copyOfRange(columnNameArr, 1, columnNameArr.length))
                .map(columnName -> "V_" + columnName.trim()).collect(Collectors.joining(", "));
        String columnParams = "P_PARAM TINYTEXT";
        String columnCondition = "PARAM = P_PARAM";
        if (tableInfoVO.insertColumnParam != null) {
            String[] columnParamArr = tableInfoVO.insertColumnParam.split(",");
            columnParams = Arrays.stream(columnParamArr)
                    .map(columnParam -> "P_" + columnParam.trim() + " TINYTEXT").collect(Collectors.joining(", "));
            columnCondition = columnParamArr[0].trim() + " = " + "P_" + columnParamArr[0].trim();
        }
        String procedure = String.format(procedureService.getProcedure(),
                tableInfoVO.tableName, tableInfoVO.tableName, columnParams, columnNameDeclare,
                tableInfoVO.tableName, columnCondition, tableInfoVO.tableName, columnCondition,
                columnNameInto, tableInfoVO.tableName, tableInfoVO.insertColumnName, columnNameValue);
        FileUtils.writeFile(path, procedure);
        FileUtils.writeFile(path, String.format(procedureService.getCall(), tableInfoVO.tableName));
        FileUtils.writeFile(path, String.format(procedureService.getDrop(), tableInfoVO.tableName));
    }
}
