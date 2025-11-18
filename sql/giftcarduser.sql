CREATE TABLE `giftcarduser` (
  `accountId` int(11) NOT NULL DEFAULT '0' COMMENT '运营区域id',
  `userId` int(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `money` int(11) NOT NULL DEFAULT '0' COMMENT '金额',
  `updateTime` datetime NOT NULL COMMENT '更新时间',
  UNIQUE KEY `userId_index_accountId` (`userId`,`accountId`) USING BTREE,
  KEY `accountId` (`accountId`) USING BTREE,
  CONSTRAINT `giftcarduser_ibfk_1` FOREIGN KEY (`accountId`) REFERENCES `account` (`accountId`) ON DELETE CASCADE,
  CONSTRAINT `giftcarduser_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='礼品卡用户余额';