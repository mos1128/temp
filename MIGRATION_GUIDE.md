# 数据迁移系统使用指南

## 项目概述

本项目是一个品牌数据库合并迁移系统，用于将品牌A的数据迁移到品牌B数据库中。

## 技术架构

- **Spring Boot**: 3.3.11
- **MyBatis-Plus**: 3.5.12
- **Java**: 17
- **数据库**: MySQL (双数据源)

## 项目结构

```
common/src/main/java/com/mos/base/common/
├── config/                          # 配置类
│   ├── SourceDataSourceConfig.java  # 品牌A数据源配置（只读）
│   └── TargetDataSourceConfig.java  # 品牌B数据源配置（读写）
├── entity/                          # 实体类（7个表）
├── mapper/
│   ├── source/                      # 品牌A数据源Mapper（只读）
│   └── target/                      # 品牌B数据源Mapper（读写）
├── service/impl/                    # 迁移服务实现（6个）
├── controller/                      # 迁移控制器
├── dto/                            # 数据传输对象
└── cache/                          # 映射关系缓存
```

## 配置说明

### 1. 修改数据库配置

编辑 `application.yml` 文件，配置两个数据源：

```yaml
spring:
  datasource:
    # 品牌A数据库（源数据库）
    source:
      jdbc-url: jdbc:mysql://localhost:3306/brand_a?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
      username: root
      password: password
      driver-class-name: com.mysql.cj.jdbc.Driver
      hikari:
        read-only: true
        maximum-pool-size: 10
    
    # 品牌B数据库（目标数据库）
    target:
      jdbc-url: jdbc:mysql://localhost:3306/brand_b?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
      username: root
      password: password
      driver-class-name: com.mysql.cj.jdbc.Driver
      hikari:
        maximum-pool-size: 10

# 迁移配置
migration:
  default-batch-size: 1000
```

## 迁移步骤

### 前提条件

1. 在品牌B数据库中提前创建好新的区域（account），与品牌A的区域1:1对应
2. 准备好新旧accountId的映射关系

### 迁移顺序（严格按此顺序执行）

```
1. 初始化Account映射
   ↓
2. 迁移User表
   ↓
3. 迁移UserToAgent表
   ↓
4. 迁移RideCard表
   ↓
5. 迁移RideCardUser表
   ↓
6. 迁移GiftCard表
   ↓
7. 迁移GiftCardUser表
```

## API接口说明

服务启动后，访问地址：`http://localhost:9000/base`

### 1. 初始化Account映射

**接口**: `POST /base/migration/init-account-mapping`

**请求体**:
```json
{
  "accountMapping": {
    "1": 101,
    "2": 102,
    "3": 103
  }
}
```

**说明**: 
- key: 品牌A的旧accountId
- value: 品牌B的新accountId

**响应示例**:
```json
{
  "success": true,
  "message": "Account映射初始化成功",
  "mappingCount": 3,
  "accountMapping": {
    "1": 101,
    "2": 102,
    "3": 103
  }
}
```

### 2. 迁移User表

**接口**: `POST /base/migration/user`

**请求体**:
```json
{
  "batchSize": 1000
}
```

**响应示例**:
```json
{
  "success": true,
  "message": "用户数据迁移成功",
  "totalCount": 10000,
  "migratedCount": 10000,
  "batchCount": 10,
  "mapping": {
    "1": 10001,
    "2": 10002
  }
}
```

### 3. 迁移UserToAgent表

**接口**: `POST /base/migration/usertoagent`

**请求体**:
```json
{
  "batchSize": 1000
}
```

### 4. 迁移RideCard表

**接口**: `POST /base/migration/ridecard`

**请求体**:
```json
{
  "batchSize": 500
}
```

### 5. 迁移RideCardUser表

**接口**: `POST /base/migration/ridecarduser`

**请求体**:
```json
{
  "batchSize": 1000
}
```

### 6. 迁移GiftCard表

**接口**: `POST /base/migration/giftcard`

**请求体**:
```json
{
  "batchSize": 500
}
```

### 7. 迁移GiftCardUser表

**接口**: `POST /base/migration/giftcarduser`

**请求体**:
```json
{
  "batchSize": 1000
}
```

### 8. 查询映射关系

**接口**: `GET /base/migration/mappings/{type}`

**type参数**: `account` | `user` | `ridecard` | `giftcard` | `userphone`

**示例**: `GET /base/migration/mappings/user`

**响应示例**:
```json
{
  "success": true,
  "type": "user",
  "mapping": {
    "1": 10001,
    "2": 10002
  }
}
```

### 9. 校验用户手机号映射

**接口**: `GET /base/migration/validate-user-phone`

**响应示例**:
```json
{
  "success": true,
  "message": "获取用户手机号映射成功",
  "totalCount": 10000,
  "userPhoneMapping": {
    "10001:13800138000": "1:13800138000",
    "10002:13900139000": "2:13900139000"
  }
}
```

**说明**: 
- key格式: `新userId:新phone`
- value格式: `旧userId:旧phone`
- 用于比对迁移数据是否正确

### 10. 清空所有映射关系

**接口**: `POST /base/migration/clear-all-mappings`

**响应示例**:
```json
{
  "success": true,
  "message": "所有映射关系已清空"
}
```

## 核心特性

### 1. 映射管理

系统维护5种映射关系：
- **accountMapping**: 旧accountId → 新accountId
- **userMapping**: 旧userId → 新userId
- **rideCardMapping**: 旧rideCardId → 新rideCardId
- **giftCardMapping**: 旧giftCardId → 新giftCardId
- **userPhoneMapping**: "新userId:新phone" → "旧userId:旧phone"

### 2. 分批次迁移

- 默认批次大小: 1000条
- 可根据需要调整批次大小
- 避免大数据量导致内存溢出

### 3. 事务管理

- 事务只包裹写操作和验证
- 流程: **读数据（无事务）→ 处理数据（无事务）→ @Transactional(写入 + 验证)**
- 每个批次独立事务
- 验证失败自动回滚当前批次

### 4. 数据验证

- 每批次插入后验证数量
- 数量不匹配立即抛出异常并回滚
- 只有验证成功才更新映射缓存

### 5. 字段名处理

- 使用 `@TableField` 明确指定数据库列名
- 避免驼峰转换导致的字段名问题
- 支持 `userId` / `userid` 等大小写不一致的情况

## 使用示例

### 完整迁移流程

```bash
# 1. 启动服务
cd /home/user/webapp
./mvnw spring-boot:run

# 2. 初始化Account映射（假设品牌A的accountId 1,2,3 对应品牌B的 101,102,103）
curl -X POST http://localhost:9000/base/migration/init-account-mapping \
  -H "Content-Type: application/json" \
  -d '{
    "accountMapping": {
      "1": 101,
      "2": 102,
      "3": 103
    }
  }'

# 3. 迁移User表
curl -X POST http://localhost:9000/base/migration/user \
  -H "Content-Type: application/json" \
  -d '{"batchSize": 1000}'

# 4. 迁移UserToAgent表
curl -X POST http://localhost:9000/base/migration/usertoagent \
  -H "Content-Type: application/json" \
  -d '{"batchSize": 1000}'

# 5. 迁移RideCard表
curl -X POST http://localhost:9000/base/migration/ridecard \
  -H "Content-Type: application/json" \
  -d '{"batchSize": 500}'

# 6. 迁移RideCardUser表
curl -X POST http://localhost:9000/base/migration/ridecarduser \
  -H "Content-Type: application/json" \
  -d '{"batchSize": 1000}'

# 7. 迁移GiftCard表
curl -X POST http://localhost:9000/base/migration/giftcard \
  -H "Content-Type: application/json" \
  -d '{"batchSize": 500}'

# 8. 迁移GiftCardUser表
curl -X POST http://localhost:9000/base/migration/giftcarduser \
  -H "Content-Type: application/json" \
  -d '{"batchSize": 1000}'

# 9. 获取用户手机号映射进行校验
curl http://localhost:9000/base/migration/validate-user-phone
```

## 注意事项

### 1. 迁移前准备

- ✅ 备份品牌B数据库
- ✅ 确保品牌B中已创建对应的account
- ✅ 准备好新旧accountId的映射关系
- ✅ 测试数据库连接

### 2. 迁移过程

- ✅ 严格按顺序迁移
- ✅ 每个表迁移完成后检查响应
- ✅ 出现错误立即停止
- ✅ 使用 `validate-user-phone` 接口校验数据

### 3. 迁移后验证

- ✅ 对比源数据库和目标数据库的数据量
- ✅ 抽查部分数据验证正确性
- ✅ 使用userPhoneMapping对比用户数据

### 4. 异常处理

- **找不到映射关系**: 检查是否已初始化accountMapping或前置表已迁移
- **插入数量不匹配**: 事务会自动回滚，检查数据库约束或唯一键冲突
- **数据库连接失败**: 检查application.yml配置

## 常见问题

### Q1: 迁移中断怎么办？

**A**: 由于使用批次事务，中断的批次会自动回滚。可以：
1. 查询映射关系确认已迁移的数据
2. 清空映射缓存重新开始
3. 或手动调整批次大小继续迁移

### Q2: 如何重新迁移某个表？

**A**: 
1. 手动清空目标数据库中该表的数据
2. 如果是User/RideCard/GiftCard表，需要清除对应的映射缓存
3. 重新调用迁移接口

### Q3: 批次大小如何选择？

**A**: 
- 数据量小（<1万）: 可用默认1000
- 数据量中（1-10万）: 建议500-1000
- 数据量大（>10万）: 建议200-500
- 根据内存和性能调整

### Q4: userPhoneMapping有什么用？

**A**: 用于数据校验，格式为：
- Key: `新userId:新phone` (迁移后的数据)
- Value: `旧userId:旧phone` (迁移前的数据)
- 可以通过比对确认数据是否正确迁移

## 技术细节

### 双数据源配置

- **源数据源**: 只读，连接品牌A数据库
- **目标数据源**: 读写，连接品牌B数据库，支持事务
- 使用不同的SqlSessionFactory和TransactionManager

### 事务策略

```java
// 无事务：读取和处理数据
List<User> sourceUsers = sourceUserMapper.selectByPage(offset, batchSize);
List<User> targetUsers = processData(sourceUsers);

// 有事务：写入 + 验证 + 更新缓存
@Transactional
public int insertAndValidate(List<User> targetUsers) {
    int inserted = targetUserMapper.batchInsert(targetUsers);
    if (inserted != targetUsers.size()) {
        throw new RuntimeException("回滚！");
    }
    updateMappingCache(); // 验证成功才更新
    return inserted;
}
```

### MyBatis批量插入自动填充ID

```java
@Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "userId")
int batchInsert(List<User> users);
```

插入后，MyBatis会自动将生成的ID填充到users列表中的每个对象。

## 联系方式

如有问题，请联系开发人员。
