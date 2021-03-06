# CreateFile
元年共享产品根据配置文件自动生成可重复执行sql，提高工作效率和减少脚本出错，欢迎使用
## 插件操作
下载插件进行导入IDEA
![img.png](src/main/resources/img/install.png)

右键配编辑窗口，菜单点击SqlFile就可以根据配置生成SQL可重复执行文件
菜单点击CreateFile可以生成配置文件或者java文件
![img.png](src/main/resources/img/operation.png)

菜单点击DemoFile可以导出sql配置文件或者java配置文件
![img.png](src/main/resources/img/demofile.png)

## 导入可重复执行文件示例
### 生成文件配置参数说明
* author:作者名称,用于文件注释作者
* filePath：生成sql文件地址为项目sql文件，会根据项目自动在地址下mysql文件夹和oracle文件夹新增sql文件
* fileName:文件名
* jdbcUrl:数据库地址支持oracle和mysql数据库连接--只有INSERT_SQL插入查询数据需要
* username:数据库用户
* password:数据库密码
### 示例
``` 
author:wangyuj
filePath:D:/git/ECS/DEV/java-web/sql/fssc/
fileName:发票业务对象内置类型
jdbcUrl: jdbc:mysql://127.0.0.1:3306/db
username: root
password: 1234
```

### 生成文件内容配置参数说明
1. 可重复执行脚本类型可自由组合
2. 需创建多个不同表的可重复执行脚本，需配置多个信息
3. 多个参数以分号;进行分隔
- procedureType:可重复执行脚本类型
ADD_TABLE(创建表),ADD_INDEX(创建索引), ADD_COLUMN(创建字段), ADD_DATA(插入数据存储过程需自己编辑), INSERT_DATA(插入可重复执行数据),INSERT_SQL(根据查询语句插入可重复执行数据)
- comment:脚本注释
- tableInfo:表名;表名注释(忽略时使用表名翻译)
- indexInfos:索引名;索引字段
- fieldInfos:字段编码;字段类型(字符串VARCHAR(32);字段注释(忽略时使用字段名翻译)(长文本TEXT,日期TIMESTAMP,数值NUMBER(32,6));字段属性-可忽略(PRIMARY主键标识/不为空标识NOT_NULL)
- insertColumnName:插入字段编码使用逗号分隔;条件字段编码使用逗号分隔
- 根据插入语句生成INSERT INTO T_TABLE_NAME (ID, COL_NAME, CREATE_DATE) VALUES ('0279a59a333da6ee68dd7b3000400001', 'name', '2022-03-23 16:07:57');
- insertSql:查询语句，根据查询出来的数据生成
### 示例ADD_TABLE
``` 
procedureType:ADD_TABLE
comment:T_TABLE_NAME内置
tableInfo:T_TABLE_NAME;示例表
fieldInfos:ID; VARCHAR ; PRIMARY
fieldInfos:COL_NAME1; VARCHAR(32); 名称1
fieldInfos:COL_NAME2; NUMBER(32,6); NOT_NULL
fieldInfos:CREATE_DATE; TIMESTAMP; 创建时间; NOT_NULL
```
### 示例ADD_INDEX
``` 
procedureType:ADD_INDEX
comment:T_TABLE_NAME新增索引
tableInfo:T_TABLE_NAME
indexInfos:INDEX_T_NAME;COL_NAME, CREATE_DATE
```
### 示例ADD_COLUMN
``` 
procedureType:ADD_COLUMN
comment:T_TABLE_NAME新增字段
tableInfo:T_TABLE_NAME
fieldInfos:COL_NAME3; VARCHAR(32); 名称3
fieldInfos:COL_NAME4; VARCHAR(32); 名称4
```
### 示例ADD_DATA
``` 
procedureType:ADD_DATA
comment:T_TABLE_NAME新增数据
tableInfo:T_TABLE_NAME
insertColumnName:ID, COL_NAME, CREATE_DATE;ID
```
### 示例INSERT_DATA
``` 
procedureType:INSERT_DATA
comment:T_TABLE_NAME新增数据
tableInfo:T_TABLE_NAME
INSERT INTO T_TABLE_NAME (ID, COL_NAME, CREATE_DATE) VALUES ('0279a59a333da6ee68dd7b3000400001', 'name', '2022-03-23 16:07:57');
INSERT INTO T_TABLE_NAME (ID, COL_NAME, CREATE_DATE) VALUES ('0279a59a333da6ee68dd7b3000400001', 'name', '2022-03-23 16:07:57');
```
### 示例INSERT_SQL
``` 查全部数据
procedureType:INSERT_SQL
comment:T_TABLE_NAME新增数据
tableInfo:T_TABLE_NAME
insertSql:SELECT *
          FROM T_TABLE_NAME WHERE ID = '';
```
``` 自定义列和数据
procedureType:INSERT_SQL
comment:T_TABLE_NAME新增数据
tableInfo:T_TABLE_NAME
insertSql:SELECT ID, '测试数据' AS COL_NAME, CREATE_DATE
          FROM T_TABLE_NAME WHERE ID = '';
```
### 示例ADD_TABLE; ADD_INDEX; INSERT_SQL
``` T_TABLE_NAME创建表，新增索引，新增数据
procedureType:ADD_TABLE, ADD_INDEX, INSERT_SQL
comment:T_TABLE_NAME初始化
tableInfo:T_TABLE_NAME;示例表
fieldInfos:ID; VARCHAR ; PRIMARY
fieldInfos:COL_NAME1; VARCHAR(32); 名称1
fieldInfos:COL_NAME2; NUMBER(32,6); NOT_NULL
fieldInfos:CREATE_DATE; TIMESTAMP; 创建时间; NOT_NULL
indexInfos:INDEX_T_NAME;COL_NAME, CREATE_DATE
insertSql:SELECT ID, COL_NAME1, COL_NAME2, CREATE_DATE
          FROM T_TABLE_NAME2 WHERE ID = '';
```

## 更新日志
- v1.03 update
  - 支持下载模板文件demo.txt
  - 支持根据表生成代码文件
- v1.02
  - 支持表名和列表根据定义编码自动翻译成注释
- v1.01
  - 优化支持数据库查询数据生成可重复执行sql语句INSERT_SQL
- v1.0
  - 支持生成以下生成脚本
    - ADD_TABLE(创建表),
    - ADD_INDEX(创建索引), 
    - ADD_COLUMN(创建字段), 
    - ADD_DATA(插入数据存储过程需自己编辑), 
    - INSERT_DATA(插入可重复执行数据)

