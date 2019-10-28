## 数据库字典

[TOC]

### access 权限配置
------------
|参数|类型|注释|允空|键|备注|
|:-------|:-------|:-------|:-------|:-------|:-------|
|id|bigint(15)|无|否|主键|自增;|
|debug|tinyint(2)|是否为调试表，只允许在开发环境使用，测试和线上环境禁用|否|||
|name|varchar(100)|实际表名，例如 apijson_user|否|||
|alias|varchar(100)|外部调用的表别名，例如 User|是|||
|get|varchar(100)|允许 get 的角色列表，例如 ["LOGIN", "CONTACT", "CIRCLE", "OWNER"]用 JSON 类型不能设置默认值，反正权限对应的需求是明确的，也不需要自动转 JSONArray。TODO: 直接 LOGIN,CONTACT,CIRCLE,OWNER 更简单，反正是开发内部用，不需要复杂查询。|否|||
|head|varchar(100)|允许 head 的角色列表，例如 ["LOGIN", "CONTACT", "CIRCLE", "OWNER"]|否|||
|gets|varchar(100)|允许 gets 的角色列表，例如 ["LOGIN", "CONTACT", "CIRCLE", "OWNER"]|否|||
|heads|varchar(100)|允许 heads 的角色列表，例如 ["LOGIN", "CONTACT", "CIRCLE", "OWNER"]|否|||
|post|varchar(100)|允许 post 的角色列表，例如 ["LOGIN", "CONTACT", "CIRCLE", "OWNER"]|否|||
|put|varchar(100)|允许 put 的角色列表，例如 ["LOGIN", "CONTACT", "CIRCLE", "OWNER"]|否|||
|delete|varchar(100)|允许 delete 的角色列表，例如 ["LOGIN", "CONTACT", "CIRCLE", "OWNER"]|否|||
|date|timestamp|创建时间|否|||
|databaseId|int(11)|数据库id与database表关联|否|||
|mark|tinyint(1)|生成接口文档标记|否|||



### apijson_privacy 用户隐私信息表。\n对安全要求高，不想泄漏真实名称。对外名称为 Privacy
------------
|参数|类型|注释|允空|键|备注|
|:-------|:-------|:-------|:-------|:-------|:-------|
|id|bigint(15)|唯一标识|否|主键||
|certified|tinyint(2)|已认证|否|||
|phone|bigint(11)|手机号，仅支持 11 位数的。不支持 +86 这种国家地区开头的。如果要支持就改为 VARCHAR(14)|否|||
|balance|decimal(10,2)|余额|否|||
|Password|varchar(20)|登录密码|否|||
|PayPassword|int(6)|支付密码|否|||



### apijson_user 用户公开信息表。\n对安全要求高，不想泄漏真实名称。对外名称为 User
------------
|参数|类型|注释|允空|键|备注|
|:-------|:-------|:-------|:-------|:-------|:-------|
|id|bigint(15)|唯一标识|否|主键|自增;|
|sex|tinyint(2)|性别：0-男1-女|否|||
|name|varchar(20)|名称|是|||
|tag|varchar(45)|标签|是|||
|head|varchar(300)|头像url|是|||
|contactIdList|text|联系人id列表|是|||
|pictureList|text|照片列表|是|||
|date|timestamp|创建日期|是|||



### comment 评论
------------
|参数|类型|注释|允空|键|备注|
|:-------|:-------|:-------|:-------|:-------|:-------|
|id|bigint(15)|唯一标识|否|主键||
|toId|bigint(15)|被回复的id|否|||
|userId|bigint(15)|评论人 User 的 id|否|||
|momentId|bigint(15)|动态id|否|||
|date|timestamp|创建日期|是|||
|content|varchar(1000)|内容|否|||



### database_info 数据库链接信息
------------
|参数|类型|注释|允空|键|备注|
|:-------|:-------|:-------|:-------|:-------|:-------|
|id|int(11) unsigned|无|否|主键|自增;|
|name|varchar(50)|名称（每个库的特定名称，用作查询时区别库的标识）|否|||
|url|varchar(255)|路径|否|||
|user|varchar(50)|用户名|否|||
|password|varchar(50)|密码|否|||
|schema|varchar(50)|数据库|否|||



### document 测试用例文档\n后端开发者在测试好后，把选好的测试用例上传，这样就能共享给前端/客户端开发者
------------
|参数|类型|注释|允空|键|备注|
|:-------|:-------|:-------|:-------|:-------|:-------|
|id|bigint(15)|唯一标识|否|主键||
|tableName|varchar(255)|表名|否|||
|type|int(4)|接口类型（1-总数接口，2-查询接口）|否|||
|describe|varchar(100)|接口描述|否|||
|url|varchar(255)|请求地址|否|||
|requestJson|text|请求\n用json格式会导致强制排序，而请求中引用赋值只能引用上面的字段，必须有序。|是|||
|responseJson|text|标准返回结果JSON\n用json格式会导致强制排序，而请求中引用赋值只能引用上面的字段，必须有序。|是|||
|header|text|请求头 Request Header：key: value  //注释|是|||
|date|timestamp|创建日期|是|||
|requestFormat|varchar(50)|请求格式|否|||
|requestMode|varchar(50)|请求方式|否|||
|mark|tinyint(1)|已发送给体育局标记（1-已发送）|否|||



### function 远程函数。强制在启动时校验所有demo是否能正常运行通过
------------
|参数|类型|注释|允空|键|备注|
|:-------|:-------|:-------|:-------|:-------|:-------|
|id|bigint(15)|无|否|主键|自增;|
|userId|bigint(15)|管理员用户Id|否|||
|name|varchar(50)|方法名|否|||
|arguments|varchar(100)|参数列表，每个参数的类型都是 String。用 , 分割的字符串 比 [JSONArray] 更好，例如 array,item ，更直观，还方便拼接函数。|是|||
|demo|text|可用的示例。TODO 改成 call，和返回值示例 back 对应。|否|||
|detail|varchar(1000)|详细描述|否|||
|type|varchar(50)|返回值类型。TODO RemoteFunction 校验 type 和 back|否|||
|version|tinyint(4)|允许的最低版本号，只限于GET,HEAD外的操作方法。TODO 使用 requestIdList 替代 version,tag,methods|否|||
|tag|varchar(20)|允许的标签.null - 允许全部TODO 使用 requestIdList 替代 version,tag,methods|是|||
|methods|varchar(50)|允许的操作方法。null - 允许全部TODO 使用 requestIdList 替代 version,tag,methods|是|||
|date|timestamp|创建时间|否|||
|back|varchar(45)|返回值示例|是|||



### history 
------------
|参数|类型|注释|允空|键|备注|
|:-------|:-------|:-------|:-------|:-------|:-------|
|id|bigint(11) unsigned|无|否|主键|自增;|
|appId|bigint(20)|应用id|否|||
|appName|varchar(50)|应用名称|是|||
|userId|bigint(20)|用户Id|否|||
|userName|varchar(50)|用户名称|是|||
|tableId|varchar(50)|表id|否|||
|tableName|varchar(255)|表名|否|||
|deptId|varchar(50)|部门|否|||
|deptName|varchar(50)|部门名|是|||
|requestParam|text|请求参数|否|||
|responseParam|text|返回参数|否|||
|time|timestamp|调用时间|否|||
|dataSize|bigint(20)|数据量|是|||



### login @deprecated，登录信息存session
------------
|参数|类型|注释|允空|键|备注|
|:-------|:-------|:-------|:-------|:-------|:-------|
|id|bigint(15)|唯一标识|否|主键||
|userId|bigint(15)|用户id|否|||
|type|tinyint(2)|类型0-密码登录1-验证码登录|否|||
|date|timestamp|创建日期|否|||



### mytest 
------------
|参数|类型|注释|允空|键|备注|
|:-------|:-------|:-------|:-------|:-------|:-------|
|id|int(11) unsigned|无|否|主键|自增;|
|content|varchar(50)|无|是|||
|phone|varchar(50)|无|是|||



### praise 如果对Moment写安全要求高，可以将Moment内praiserUserIdList分离到Praise表中，作为userIdList。\n权限注解也改下：\n@MethodAccess(\n		PUT = {OWNER, ADMIN}\n		)\nclass Moment {\n       …\n}\n\n@MethodAccess(\n		PUT = {LOGIN, CONTACT, CIRCLE, OWNER, ADMIN}\n		)\n class Praise {\n       …\n }\n
------------
|参数|类型|注释|允空|键|备注|
|:-------|:-------|:-------|:-------|:-------|:-------|
|id|bigint(15)|动态id|否|主键|自增;|
|momentId|bigint(15)|唯一标识|否|||
|userId|bigint(15)|用户id|否|||
|date|timestamp|点赞时间|是|||



### request 最好编辑完后删除主键，这样就是只读状态，不能随意更改。需要更改就重新加上主键。\n\n每次启动服务器时加载整个表到内存。\n这个表不可省略，model内注解的权限只是客户端能用的，其它可以保证即便服务端代码错误时也不会误删数据。
------------
|参数|类型|注释|允空|键|备注|
|:-------|:-------|:-------|:-------|:-------|:-------|
|id|bigint(15)|唯一标识|否|主键||
|version|tinyint(4)|GET,HEAD可用任意结构访问任意开放内容，不需要这个字段。其它的操作因为写入了结构和内容，所以都需要，按照不同的version选择对应的structure。自动化版本管理：Request JSON最外层可以传  “version”:Integer 。1.未传或 <= 0，用最新版。 “@order”:”version-“2.已传且 > 0，用version以上的可用版本的最低版本。 “@order”:”version+”, “version{}”:”>={version}”|否|||
|method|varchar(10)|只限于GET,HEAD外的操作方法。|是|||
|tag|varchar(20)|标签|否|||
|structure|text|结构。TODO 里面的 PUT 改为 UPDATE，避免和请求 PUT 搞混。|否|||
|detail|varchar(10000)|详细说明|是|||
|date|timestamp|创建日期|是|||



### response 每次启动服务器时加载整个表到内存。
------------
|参数|类型|注释|允空|键|备注|
|:-------|:-------|:-------|:-------|:-------|:-------|
|id|bigint(15)|唯一标识|否|主键||
|method|varchar(10)|方法|是|||
|model|varchar(20)|表名，table是SQL关键词不能用|否|||
|structure|text|结构|否|||
|detail|varchar(10000)|详细说明|是|||
|date|timestamp|创建日期|是|||



### test 测试及验证用的表，可以用 SELECT condition替代 SELECT * FROM Test WHERE condition，这样就不需要这张表了
------------
|参数|类型|注释|允空|键|备注|
|:-------|:-------|:-------|:-------|:-------|:-------|
|id|tinyint(2)|无|否|主键||



### testrecord 测试记录\n主要用于保存自动化接口回归测试
------------
|参数|类型|注释|允空|键|备注|
|:-------|:-------|:-------|:-------|:-------|:-------|
|id|bigint(15)|唯一标识|否|主键||
|userId|bigint(15)|用户id|否|||
|documentId|bigint(15)|测试用例文档id|否|||
|response|text|接口返回结果JSON|否|||
|date|timestamp|创建日期|否|||
|compare|text|对比结果|是|||
|standard|text|response 的校验标准，是一个 JSON 格式的 AST ，描述了正确 Response 的结构、里面的字段名称、类型、长度、取值范围 等属性。|是|||



### verify 
------------
|参数|类型|注释|允空|键|备注|
|:-------|:-------|:-------|:-------|:-------|:-------|
|id|bigint(15)|唯一标识|否|主键|自增;|
|type|int(2)|类型：0-登录1-注册2-修改登录密码3-修改支付密码|否|||
|phone|bigint(11)|手机号|否|||
|verify|int(6)|验证码|否|||
|date|timestamp|创建时间|否|||



