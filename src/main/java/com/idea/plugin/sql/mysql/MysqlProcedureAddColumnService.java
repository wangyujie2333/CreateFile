package com.idea.plugin.sql.mysql;

import com.idea.plugin.sql.BaseProcedureService;
import com.idea.plugin.sql.IProcedureService;
import com.idea.plugin.sql.support.FieldInfoVO;
import com.idea.plugin.sql.support.TableInfoVO;
import com.idea.plugin.sql.support.exception.SqlException;
import com.idea.plugin.utils.FileUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class MysqlProcedureAddColumnService extends BaseProcedureService {

    public void addProcedure(String path, TableInfoVO tableInfoVO) throws SqlException {
        if (StringUtils.isEmpty(path) || CollectionUtils.isEmpty(tableInfoVO.fieldInfos)) {
            return;
        }
        IProcedureService procedureService = new MysqlProcedureAddColumn();
        String comment = StringUtils.isEmpty(tableInfoVO.comment) ? tableInfoVO.tableComment + "新增字段" : tableInfoVO.comment;
        FileUtils.writeFile(path, String.format(procedureService.getComment(), comment));
        FileUtils.writeFile(path, String.format(procedureService.getProcedure(), tableInfoVO.tableName, tableInfoVO.tableName));
        for (FieldInfoVO fieldVO : tableInfoVO.fieldInfos) {
            String alterAddColumnCall = String.format(procedureService.getCall(),
                    tableInfoVO.tableName, tableInfoVO.tableName, fieldVO.columnName, fieldVO.columnType.getMtype(fieldVO.columnTypeArgs), fieldVO.nullType.getCode(), fieldVO.comment);
            FileUtils.writeFile(path, alterAddColumnCall);
        }
        FileUtils.writeFile(path, String.format(procedureService.getDrop(), tableInfoVO.tableName));
    }
}
