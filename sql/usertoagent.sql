CREATE TABLE `usertoagent` (
  `recordId` int(11) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `userId` int(11) DEFAULT NULL COMMENT '用户ID',
  `accountId` int(11) DEFAULT NULL COMMENT '运营区域ID',
  `updateDt` datetime DEFAULT NULL COMMENT '更新时间',
  `firstId` int(11) DEFAULT NULL COMMENT '第一次区域id',
  PRIMARY KEY (`recordId`) USING BTREE,
  UNIQUE KEY `idx_usertoagent_userId_accountId` (`userId`,`accountId`) USING BTREE,
  KEY `idx_usertoagent_accountId` (`accountId`) USING BTREE,
  KEY `idx_usertoagent_userId` (`userId`) USING BTREE,
  KEY `first` (`firstId`) USING BTREE,
  CONSTRAINT `usertoagent_ibfk_1` FOREIGN KEY (`accountId`) REFERENCES `account` (`accountId`) ON DELETE CASCADE,
  CONSTRAINT `usertoagent_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=149074236 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户和运营区域关联表';