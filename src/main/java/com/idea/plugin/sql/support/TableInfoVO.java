package com.idea.plugin.sql.support;


import com.idea.plugin.sql.support.enums.FieldTypeEnum;
import com.idea.plugin.sql.support.enums.NullTypeEnum;
import com.idea.plugin.sql.support.enums.PrimaryTypeEnum;
import com.idea.plugin.sql.support.enums.ProcedureTypeEnum;
import com.idea.plugin.sql.support.exception.SqlException;
import com.idea.plugin.translator.TranslatorConfig;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TableInfoVO {
    public String author;
    public List<ProcedureTypeEnum> procedureType = new ArrayList<>();
    public String comment;
    public String filePath;
    public String fileName;
    public String jdbcUrl;
    public String username;
    public String password;
    public String tableName;
    public String tableComment = "";
    public String insertColumnName;
    public String insertColumnParam;
    public String insertSql;
    public String insertData;
    public List<IndexInfoVO> indexInfos = new ArrayList<>();
    public List<FieldInfoVO> fieldInfos = new ArrayList<>();

    public static TableInfoVO builder() {
        return new TableInfoVO();
    }

    public TableInfoVO procedureVO(ProcedureVO procedureVO) {
        this.author = procedureVO.author;
        this.fileName = procedureVO.fileName;
        this.filePath = procedureVO.filePath;
        this.jdbcUrl = procedureVO.jdbcUrl;
        this.username = procedureVO.username;
        this.password = procedureVO.password;
        return this;
    }

    public TableInfoVO procedureType(String procedureType) {
        this.procedureType = Arrays.stream(procedureType.split(",")).map(code -> ProcedureTypeEnum.codeToEnum(code.trim())).filter(Objects::nonNull).collect(Collectors.toList());
        return this;
    }

    public TableInfoVO comment(String comment) {
        this.comment = comment;
        return this;
    }

    public TableInfoVO filePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public TableInfoVO fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public TableInfoVO jdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
        return this;
    }

    public TableInfoVO username(String username) {
        this.username = username;
        return this;
    }

    public TableInfoVO password(String password) {
        this.password = password;
        return this;
    }

    public TableInfoVO tableInfo(String tableName, String tableComment) {
        this.tableName = tableName.toUpperCase();
        if (StringUtils.isEmpty(tableComment)) {
            this.tableComment = TranslatorConfig.translate(tableName.toUpperCase()) + "表";
        } else {
            this.tableComment = tableComment;
        }
        return this;
    }

    public TableInfoVO insertColumnName(String insertColumnName, String insertColumnParam) {
        this.insertColumnName = insertColumnName.toUpperCase();
        this.insertColumnParam = insertColumnParam.toUpperCase();
        return this;
    }

    public TableInfoVO insertSql(String insertSql) {
        this.insertSql = insertSql;
        return this;
    }

    public TableInfoVO insertData(String insertData) {
        if (this.insertData == null) {
            this.insertData = insertData;
        } else {
            this.insertData = this.insertData + "\n" + insertData;
        }
        return this;
    }

    public TableInfoVO indexInfos(String indexName, String indexColumnName) {
        if (StringUtils.isEmpty(indexName) || StringUtils.isEmpty(indexColumnName)) {
            return this;
        }
        IndexInfoVO indexInfoVO = IndexInfoVO.builder().indexName(indexName.toUpperCase()).indexColumnName(indexColumnName.toUpperCase());
        this.indexInfos.add(indexInfoVO);
        return this;
    }

    public TableInfoVO fieldInfos(String columnName, String columnType, String arg0, String arg1) throws SqlException {
        if (StringUtils.isEmpty(columnName) || StringUtils.isEmpty(columnType)) {
            return this;
        }
        FieldInfoVO fieldInfoVO = FieldInfoVO.builder()
                .columnName(columnName.toUpperCase())
                .columnType(FieldTypeEnum.codeToEnum(columnType.toUpperCase()))
                .columnTypeArgs(FieldTypeEnum.codeGetArgs(columnType.toUpperCase()));
        Boolean isEnum = setAttri(arg0, fieldInfoVO);
        if (!Boolean.TRUE.equals(isEnum) && !StringUtils.isEmpty(arg0)) {
            fieldInfoVO.comment(arg0);
        }
        isEnum = setAttri(arg1, fieldInfoVO);
        if (!Boolean.TRUE.equals(isEnum) && !StringUtils.isEmpty(arg1)) {
            fieldInfoVO.comment(arg0);
        }
        if (StringUtils.isEmpty(fieldInfoVO.comment)) {
            fieldInfoVO.comment(TranslatorConfig.translate(columnName.toUpperCase()));
        }
        this.fieldInfos.add(fieldInfoVO);
        return this;
    }

    private Boolean setAttri(String arg, FieldInfoVO fieldInfoVO) {
        Boolean isEnum = Boolean.FALSE;
        if (StringUtils.isNotEmpty(arg)) {
            arg = arg.toUpperCase();
            if (NullTypeEnum.codeToEnum(arg) != null) {
                fieldInfoVO.nullType(NullTypeEnum.codeToEnum(arg));
                isEnum = true;
            }
            if (PrimaryTypeEnum.codeToEnum(arg) != null) {
                fieldInfoVO.primary(PrimaryTypeEnum.codeToEnum(arg));
                fieldInfoVO.nullType(NullTypeEnum.NOT_NULL);
                fieldInfoVO.comment("主键ID");
                isEnum = true;
            }
        }
        return isEnum;
    }

}
