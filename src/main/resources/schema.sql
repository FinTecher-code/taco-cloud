-- 创建数据库
CREATE DATABASE IF NOT EXISTS blog DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE blog;

-- 用户表
CREATE TABLE IF NOT EXISTS user (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE COMMENT '用户名',
    password    VARCHAR(100) NOT NULL COMMENT '密码（明文，后续可升级为加密）',
    nickname    VARCHAR(50)  DEFAULT NULL COMMENT '昵称',
    avatar      VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除 0-正常 1-删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 分类表
CREATE TABLE IF NOT EXISTS category (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50)  NOT NULL UNIQUE COMMENT '分类名称',
    description VARCHAR(255) DEFAULT NULL COMMENT '分类描述',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章分类表';

-- 文章表
CREATE TABLE IF NOT EXISTS article (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(200) NOT NULL COMMENT '文章标题',
    content     LONGTEXT     NOT NULL COMMENT '文章内容（Markdown）',
    summary     VARCHAR(500) DEFAULT NULL COMMENT '文章摘要',
    user_id     BIGINT       NOT NULL COMMENT '作者ID',
    category_id BIGINT       DEFAULT NULL COMMENT '分类ID',
    status      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态 0-草稿 1-已发布',
    view_count  INT          NOT NULL DEFAULT 0 COMMENT '浏览量',
    like_count  INT          NOT NULL DEFAULT 0 COMMENT '点赞量',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_user (user_id),
    INDEX idx_category (category_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章表';

-- 远程推送数据记录表
CREATE TABLE IF NOT EXISTS remote_data_record (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    source      VARCHAR(100) NOT NULL COMMENT '数据来源',
    data_type   VARCHAR(50)  NOT NULL COMMENT '数据类型',
    data_content TEXT        NOT NULL COMMENT '数据内容(JSON)',
    status      VARCHAR(20)  NOT NULL DEFAULT 'RECEIVED' COMMENT '状态',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_source (source),
    INDEX idx_data_type (data_type),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='远程推送数据记录表';
