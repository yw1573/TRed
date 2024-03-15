# 更新计划

***

| 版本                                                         | 版本说明       |
| ------------------------------------------------------------ | -------------- |
| [v0.0.1](https://github.com/yw1573/TRed/releases/tag/v0.0.1) | 第一个公开版本 |

***

## 功能如下

1. 血糖记录以时间、标签 [空腹，早餐后 2 小时，午餐后 2 小时，晚餐后 2 小时，睡前或 22 点]、血糖值为维度记录血糖【主要功能】
2. 简单折线图，根据标签生成一段时间内的血糖折线图【主要功能】
3. 血糖记录查询，显示所有血糖记录【主要功能】
4. 血糖记录删除，按序号删除血糖记录，可以在记录错误时对数据进行删除，需要在 `3` 内可以查询，并且删除数据后，会整个表重排【主要功能】
5. 血糖数据库 sql 导出导入功能【辅助功能】



## 数据库以其他说明

### 数据库说明

```sql
CREATE TABLE [BloodSugars] ( 							-- 血糖值记录表
    [id]        INTEGER PRIMARY KEY, 					 -- 序号
    [timestamp] INTEGER,								-- 血糖记录时间
    [phase]     TEXT,									-- 标签
    [value]     REAL 									-- 血糖值
);
CREATE TABLE [PhaseString] ( 							 -- 标签展示表
    [id]     INTEGER PRIMARY KEY AUTOINCREMENT,	 		   -- 序号
    [phase]  CHAR    UNIQUE,				   		   	 -- 标签名称
    [desc] CHAR 									    -- 标签描述
);


INSERT INTO PhaseString (phase) VALUES ('空腹');
INSERT INTO PhaseString (phase) VALUES ('早餐1小时后');
INSERT INTO PhaseString (phase) VALUES ('早餐2小时后');
INSERT INTO PhaseString (phase) VALUES ('午餐前');
INSERT INTO PhaseString (phase) VALUES ('午餐1小时后');
INSERT INTO PhaseString (phase) VALUES ('午餐2小时后');
INSERT INTO PhaseString (phase) VALUES ('晚餐前');
INSERT INTO PhaseString (phase) VALUES ('晚餐1小时后');
INSERT INTO PhaseString (phase) VALUES ('晚餐2小时后');
INSERT INTO PhaseString (phase) VALUES ('睡前或22点');
INSERT INTO PhaseString (phase) VALUES ('随机');
```

### 版本说明

版本号将以x.x.x更新方式迭代，其中apk名称中的x.x.x.x版本为小版本，小版本不修改代码中的版本号进行迭代，不定期进行大版本迭代



## 更新计划

* [ ] ### 折线图

    * [x] #### 目前折线图已实现各个阶段切换由按钮更换为下拉框选择
    * [ ] #### X 轴实现日期显示，但是现在有 bug，目前正在解决

* [x] ### 数据库

    * [ ] #### 删除单独的导入导出页面

    * [ ] #### 导入导出到本地 sql 文件

        * [x] ##### 导出 sql 到本地
        * [ ] ##### 从本地导入 sql 到数据库

    * [x] #### 标签从数据库中获取增加 PhaseString 表

* [ ] ### 美化界面

  * [x] #### 更换了下拉框颜色
  
    
    
    

