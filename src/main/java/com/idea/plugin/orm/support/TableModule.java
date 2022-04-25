package com.idea.plugin.orm.support;

import com.google.common.base.CaseFormat;
import com.idea.plugin.sql.support.FieldInfoVO;
import com.idea.plugin.sql.support.TableInfoVO;
import com.idea.plugin.sql.support.enums.PrimaryTypeEnum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class TableModule {

    public TableInfoVO tableInfoVO;
    public List<FieldModule> fields = new ArrayList<>();
    public FileTypeInfo fileTypeInfo;

    public TableModule(GeneratorContext context) {
        this.tableInfoVO = context.getTableInfoVO();
        for (FieldInfoVO fieldInfo : tableInfoVO.fieldInfos) {
            fields.add(new FieldModule(fieldInfo));
        }
        this.fileTypeInfo = context.getFileTypeInfo();
    }

    public List<FieldModule> getFields() {
        return fields;
    }

    public String getPackage() {
        return fileTypeInfo.getPackagePath().replaceAll("/", ".");
    }

    public Set<String> getImports() {
        return new HashSet<>();
    }

    public String getComment() {
        return tableInfoVO.tableComment.replace("表", "");
    }

    public String getSimpleName() {
        return fileTypeInfo.getFileName();
    }

    public String getVarName() {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, getSimpleName());
    }

    public FieldInfoVO getIdColumn() {
        return tableInfoVO.fieldInfos.stream().filter(fieldInfoVO -> fieldInfoVO.primary.equals(PrimaryTypeEnum.PRIMARY))
                .findAny().orElse(tableInfoVO.fieldInfos.get(0));
    }

    public String getIdName() {
        String idName = getIdColumn().columnName;
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, idName);
    }

    public String getIdType() {
        return getIdColumn().columnType.getJtype().getName();
    }

    public String getIdClass() {
        return getIdColumn().columnType.getJclass().getName();
    }

    public String getTableName() {
        return tableInfoVO.tableName;
    }

    public String getName() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableInfoVO.tableName);
    }

    public String getAuthor() {
        return tableInfoVO.author;
    }

    public String getDate() {
        return tableInfoVO.date;
    }

}
