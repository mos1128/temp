CREATE TABLE `account` (
  `accountId` int(11) NOT NULL AUTO_INCREMENT COMMENT '账户id',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '用户类型',
  `parentId` int(11) DEFAULT '-1' COMMENT '父id',
  `name` varchar(100) DEFAULT '' COMMENT '用户名',
  `password` varchar(100) DEFAULT '' COMMENT '密码',
  `phone` varchar(50) NOT NULL COMMENT '手机号码',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `joinTime` datetime NOT NULL COMMENT '添加时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  `country` int(11) DEFAULT '86' COMMENT '国家代码',
  `delFlag` int(11) DEFAULT '0' COMMENT '0=未删除 1=已删除',
  PRIMARY KEY (`accountId`) USING BTREE,
  KEY `type` (`type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11084 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='管理账户';