DROP DATABASE IF EXISTS `mooc`;
-- 创建数据库
CREATE DATABASE `mooc`;
-- 进入数据库
use mooc;

-- 创建课程表
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`
(
    `id`           bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `name`         varchar(20)     NOT NULL COMMENT '课程名称',
    `teacher_id`   INT             NOT NULL COMMENT '讲师id',
    `summary`      varchar(2000) COMMENT '课程概述',
    `duration`     INT UNSIGNED    NOT NULL DEFAULT 0 COMMENT '时长|单位秒',
    `image`        varchar(100)    NOT NULL COMMENT '封面图片',
    `learning_num` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '学习人数（观看视频人数）',
    `collection_num` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '收藏人数',
    `comment_num`  bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '评论数',
    `question_num`  bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '课程问答数',
    `status`       tinyint         NOT NULL DEFAULT 1 COMMENT '状态|0草稿/1发布/2禁用/3已删除',
    `create_time`  datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE = INNODB
  DEFAULT charset = utf8mb4 COMMENT ='课程表';

insert into course (id,name, teacher_id, summary, duration, image,create_time)
values (1,'spring cloud入门实践',7, '这是一门很好的课程，spring cloud入门实践，学习微服务架构', 9230, "/file/default.png",'2021-03-25'),
       (2,'spring boot快速开始', 7, '这是一门学习spring boot的入门课程，牛牛', 8260, "/file/default.png",'2021-03-24'),
       (3,'初探spring', 7, 'spring框架是当前比较最流行的java框架，普遍应用于企业web开发', 6201, "/file/default.png",'2021-03-24'),
       (4,'java是初恋', 7, '第一眼看到java就知道自己离不开它，它是世界上最好的语言', 17242, "/file/default.png",'2021-03-26'),

       (5,'spring cloud入门实践1', 8, '这是一门很好的课程，spring cloud入门实践，学习微服务架构', 9230, "/file/default.png",'2021-03-21'),
       (6,'spring boot快速开始1', 8, '这是一门学习spring boot的入门课程，牛牛', 8260, "/file/default.png",'2020-11-03'),
       (7,'初探spring1', 8, 'spring框架是当前比较最流行的java框架，普遍应用于企业web开发', 6201, "/file/default.png",'2021-03-26'),
       (8,'java是初恋1', 7, '第一眼看到java就知道自己离不开它，它是世界上最好的语言', 17242, "/file/default.png",'2020-03-26'),

       (9,'spring cloud入门实践2', 7, '这是一门很好的课程，spring cloud入门实践，学习微服务架构', 9230, "/file/default.png",'2020-03-03'),
       (10,'spring boot快速开始2', 8, '这是一门学习spring boot的入门课程，牛牛', 8260, "/file/default.png",'2020-03-26'),
       (11,'初探spring2', 8, 'spring框架是当前比较最流行的java框架，普遍应用于企业web开发', 6201, "/file/default.png",'2021-01-01'),
       (12,'java是初恋2', 7, '第一眼看到java就知道自己离不开它，它是世界上最好的语言', 17242, "/file/default.png",'2020-03-12'),

       (13,'spring cloud入门实践3', 7, '这是一门很好的课程，spring cloud入门实践，学习微服务架构', 9230, "/file/default.png",'2020-03-03'),
       (14,'spring boot快速开始3', 7, '这是一门学习spring boot的入门课程，牛牛', 8260, "/file/default.png",'2020-03-03'),
       (15,'初探spring3', 7, 'spring框架是当前比较最流行的java框架，普遍应用于企业web开发', 6201, "/file/default.png",'2021-03-01'),
       (16,'java是初恋3', 7, '第一眼看到java就知道自己离不开它，它是世界上最好的语言', 17242, "/file/default.png",'2020-03-12');


DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`
(
    `id`          int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`        varchar(30)  NOT NULL COMMENT '分类名称',
    `description` varchar(200)          DEFAULT NULL COMMENT '分类描述',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE = INNODB
  DEFAULT charset = utf8mb4 COMMENT ='课程分类表';
insert into `category` (name, description)
values ('其他', '其他课程'),
       ('后台开发', '后台开发相关课程'),
       ('前端开发', '前端开发相关课程'),
       ('测试工程师', 'description'),
       ('文学创作', '文学创作'),
       ('数据库', '我热爱学习数据库');

DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`
(
    `id`          int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    category_id   int UNSIGNED NOT NULL COMMENT '分类id',
    `name`        varchar(30)  NOT NULL COMMENT '标签名',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE = INNODB
  DEFAULT charset = utf8mb4 COMMENT ='标签表';
insert into `tag` (category_id, name)
values (2, 'java'),
       (2, 'python'),
       (2, 'golang'),
       (2, 'spring'),
       (3, 'vue'),
       (3, 'js'),
       (3, 'html');

-- 课程标签关联表
DROP TABLE IF EXISTS `course_tag_rel`;
CREATE TABLE `course_tag_rel`
(
    `id`          int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `course_id`   int UNSIGNED NOT NULL COMMENT '课程id',
    `tag_id`      int UNSIGNED NOT NULL COMMENT '标签id',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  DEFAULT charset = utf8mb4 COMMENT ='课程标签关联表';

insert into course_tag_rel(course_id,tag_id)
values (1,1),(2,2),(3,1),(4,2),(5,1),(6,2),(7,1),(8,2),(9,1),(10,2),(11,1),(12,2),(13,1),(14,2),(15,1),(16,2);

-- 课程大章表
DROP TABLE IF EXISTS `chapter`;
CREATE TABLE `chapter`
(
    `id`          bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `course_id`   bigint UNSIGNED NOT NULL COMMENT '课程id',
    `name`        varchar(30)     NOT NULL COMMENT '名称',
    `duration`    INT                      DEFAULT 0 COMMENT '时长|单位秒',
    `sort`        int                      DEFAULT 999 COMMENT '顺序',
    `create_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  DEFAULT charset = utf8mb4 COMMENT ='章节表';
insert into `chapter` (course_id, name, sort)
values (1, '测试大章01', 1);
insert into `chapter` (course_id, name, sort)
values (1, '测试大章02', 2);
insert into `chapter` (course_id, name, sort)
values (1, '测试大章03', 3);
insert into `chapter` (course_id, name, sort)
values (1, '测试大章04', 4);
insert into `chapter` (course_id, name, sort)
values (1, '测试大章05', 5);
insert into `chapter` (course_id, name, sort)
values (1, '测试大章06', 6);
insert into `chapter` (course_id, name, sort)
values (1, '测试大章07', 7);
insert into `chapter` (course_id, name, sort)
values (1, '测试大章08', 8);
insert into `chapter` (course_id, name, sort)
values (1, '测试大章09', 9);
insert into `chapter` (course_id, name, sort)
values (1, '测试大章10', 10);
insert into `chapter` (course_id, name, sort)
values (1, '测试大章11', 11);
insert into `chapter` (course_id, name, sort)
values (1, '测试大章12', 12);
insert into `chapter` (course_id, name, sort)
values (1, '测试大章13', 13);
insert into `chapter` (course_id, name, sort)
values (1, '测试大章14', 14);
insert into `chapter` (course_id, name, sort)
values (1, '测试大章15', 15);
insert into `chapter` (course_id, name, sort)
values (1, '测试大章16', 16);
insert into `chapter` (course_id, name, sort)
values (1, '测试大章17', 17);

-- 大章小节表
DROP TABLE IF EXISTS `chapter_section`;
create table `chapter_section`
(
    `id`          bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `title`       varchar(50)     NOT NULL COMMENT '标题',
    `course_id`   bigint UNSIGNED COMMENT '课程id',
    `chapter_id`  bigint UNSIGNED NOT NULL COMMENT '章节id',
    `video`       varchar(200) COMMENT '视频',
    `file_id`     int COMMENT '文件表Id',
    `duration`    INT                      DEFAULT 0 COMMENT '时长|单位秒',
    `sort`        INT                      DEFAULT 999 COMMENT '顺序',
    `create_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  DEFAULT charset = utf8mb4 COMMENT ='课程小节表';

insert into `chapter_section` (title, course_id, chapter_id, video, duration, sort)
values ('测试小节01', 1, 1, '/file/39dee899-b1f0-427e-9c6c-61d80afbbf17.mp4', 500, 1),
       ('测试小节02', 1, 1, '/file/39dee899-b1f0-427e-9c6c-61d80afbbf17.mp4', 500, 2),
       ('测试小节03', 1, 1, '', 500, 3),
       ('测试小节01', 1, 2, '', 500, 1),
       ('测试小节02', 1, 2, '', 500, 2),
       ('测试小节02', 1, 2, '', 500, 3),
       ('测试小节01', 1, 2, '', 500, 1),
       ('测试小节02', 1, 2, '', 500, 2),
       ('测试小节02', 1, 2, '', 500, 3),
       ('测试小节01', 1, 3, '', 500, 1),
       ('测试小节02', 1, 3, '', 500, 2),
       ('测试小节02', 1, 4, '', 500, 3),
       ('测试小节01', 1, 4, '', 500, 1),
       ('测试小节02', 1, 5, '', 500, 2),
       ('测试小节02', 1, 5, '', 500, 3),
       ('测试小节01', 1, 6, '', 500, 1),
       ('测试小节02', 1, 6, '', 500, 2),
       ('测试小节02', 1, 6, '', 500, 3),
       ('测试小节01', 1, 7, '', 500, 1),
       ('测试小节02', 1, 8, '', 500, 2);


-- 文件表
DROP TABLE IF EXISTS `mooc_file`;
CREATE TABLE `mooc_file`
(
    `id`          bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`        varchar(200)    NOT NULL COMMENT '文件名',
    `file_path`   varchar(400)    NOT NULL COMMENT '视频相对路径',
    `file_size`   int             NOT NULL COMMENT '大小|字节Byte',
    `file_suffix` varchar(10)     NOT NULL COMMENT '文件后缀',
    `file_key`    varchar(100)    NOT NULL COMMENT '文件唯一标识',
    `file_type`   int             NOT NULL COMMENT '文件类型|1视频、2图片、3未知类型、4txt、5markdowm 5、ppt 6 word 7、excel 8、pdf',
    `shard_index` int             NOT NULL COMMENT '分片下标',
    `shard_count` int             NOT NULL COMMENT '总共分片数',
    `shard_size`  int             NOT NULL COMMENT '分片大小',
    `user_id`     bigint          NOT NULL COMMENT '所属用户ID',
    `course_id`   bigint COMMENT '所属课程ID',
    `status`      tinyint                  DEFAULT 1 COMMENT '文件状态| 1正常，2已删除',
    `create_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  DEFAULT charset = utf8mb4 COMMENT ='文件表';

-- 登录日志表
DROP TABLE IF EXISTS `login_log`;
CREATE TABLE `login_log`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `log_name`    varchar(255) NOT NULL COMMENT '日志名称',
    `account`     varchar(50)  NOT NULL COMMENT '管理员账号',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `succeed`     varchar(255)          DEFAULT NULL COMMENT '是否执行成功',
    `message`     text COMMENT '具体消息',
    `ip`          varchar(255)          DEFAULT NULL COMMENT '登录ip',
    `system_type` varchar(255)          DEFAULT NULL COMMENT '系统类型',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 8793
  DEFAULT CHARSET = utf8 COMMENT ='登录日志表';

-- 课程监控记录表
DROP TABLE IF EXISTS `course_monitor_record`;
CREATE TABLE `course_monitor_record`
(
    `id`          bigint          NOT NULL AUTO_INCREMENT COMMENT '主键',
    `teacher_id`  bigint UNSIGNED NOT NULL COMMENT '教师id',
    `course_id`   bigint UNSIGNED NOT NULL COMMENT '课程id',
    `message`     varchar(255) COMMENT '具体消息',
    `record_type` varchar(6)      NOT NULL COMMENT '类型|新增课程、上传视频、删除课程',
    `ip`          varchar(255)             DEFAULT NULL COMMENT '登录ip',
    `create_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 8793
  DEFAULT CHARSET = utf8 COMMENT ='课程监控记录表';
insert into course_monitor_record(teacher_id, course_id, message, record_type, ip)
values (7, 1, '新增了课程 《我是大傻逼》', '新增课程', '127.0.0.1'),
       (7, 1, '新增了课程 《我是大傻逼》', '新增课程', '127.0.0.1'),
       (7, 1, '删除了课程 《我是大傻逼》', '删除课程', '127.0.0.1'),
       (7, 1, '上传了视频', '上传视频', '127.0.0.1'),
       (7, 1, '收藏了课程 《我是大傻逼》', '课程收藏', '127.0.0.1'),
       (7, 1, '收藏了课程 《我是大傻逼》', '课程收藏', '127.0.0.1'),
       (7, 1, '上传了视频', '上传视频', '127.0.0.1');

-- 普通用户表（教师/用户）
DROP TABLE IF EXISTS `mooc_user`;
CREATE TABLE `mooc_user`
(
    `id`          bigint        NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_image`  varchar(50)   NOT NULL COMMENT '用户头像',
    `name`        varchar(40)   NOT NULL COMMENT '用户昵称',
    `account`     varchar(20)   NOT NULL COMMENT '登录账号',
    `password`    varchar(1048) NOT NULL COMMENT '登录密码',
    `user_type`   varchar(4)    NOT NULL COMMENT '类型，教师/普通用户',
    `motto` varchar(64) default NULL COMMENT '用户格言/座右铭',
    `status`      tinyint       NOT NULL DEFAULT 1 COMMENT '用户状态| 1正常，2禁用，3已删除',
    `login_time`  datetime               DEFAULT NULL COMMENT '最近登录时间',
    `create_time` datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_account` (`account`)
) ENGINE = INNODB
  DEFAULT charset = utf8 COMMENT ='普通用户表';
-- 插入用户表，密码为root
insert into `mooc_user`(id,user_image, name, account, password, user_type)
values (1,'/file/default.png', '张三丰', 'zhangsanfeng', '$2a$10$0LI/kQxqW8XxO1BVsH2hK.K7AkRdeUMYuhqd/wUOg2RqEe3n3kOY2',
        '普通用户'),
       (2,'/file/default.png', '张四丰', 'zhangsifeng', '$2a$10$0LI/kQxqW8XxO1BVsH2hK.K7AkRdeUMYuhqd/wUOg2RqEe3n3kOY2',
        '普通用户'),
       (3,'/file/default.png', '张五丰', 'zhangwufeng', '$2a$10$0LI/kQxqW8XxO1BVsH2hK.K7AkRdeUMYuhqd/wUOg2RqEe3n3kOY2',
        '普通用户'),
       (4,'/file/default.png', '张一丰', 'zhangyifeng', '$2a$10$0LI/kQxqW8XxO1BVsH2hK.K7AkRdeUMYuhqd/wUOg2RqEe3n3kOY2',
        '普通用户'),
       (5,'/file/default.png', '张六丰', 'zhangliufeng', '$2a$10$0LI/kQxqW8XxO1BVsH2hK.K7AkRdeUMYuhqd/wUOg2RqEe3n3kOY2',
        '普通用户'),
       (6,'/file/default.png', '张二丰', 'zhangerfeng', '$2a$10$0LI/kQxqW8XxO1BVsH2hK.K7AkRdeUMYuhqd/wUOg2RqEe3n3kOY2',
        '普通用户'),
       (7,'/file/default.png', 'go老师', 'gotodo', '$2a$10$0LI/kQxqW8XxO1BVsH2hK.K7AkRdeUMYuhqd/wUOg2RqEe3n3kOY2', '教师'),
       (8,'/file/default.png', '以父之名', 'yifuzhiming', '$2a$10$0LI/kQxqW8XxO1BVsH2hK.K7AkRdeUMYuhqd/wUOg2RqEe3n3kOY2',
        '教师');

-- 系统管理员表
DROP TABLE IF EXISTS `mooc_manager`;
CREATE TABLE `mooc_manager`
(
    `id`          bigint        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`        varchar(40)   NOT NULL COMMENT '名字',
    `account`     varchar(20)   NOT NULL COMMENT '登录账号',
    `password`    varchar(1048) NOT NULL COMMENT '密码',
    `status`      tinyint       NOT NULL DEFAULT 1 COMMENT '用户状态| 1正常，2禁用，3已删除',
    `create_time` datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_account` (`account`)
) ENGINE = INNODB
  DEFAULT charset = utf8mb4 COMMENT ='系统管理员表';

-- 插入管理员，密码是root
insert into mooc_manager(name, account, password)
values ('张三', 'zhangsan', '$2a$10$0LI/kQxqW8XxO1BVsH2hK.K7AkRdeUMYuhqd/wUOg2RqEe3n3kOY2'),
       ('李四', 'lisi', '$2a$10$0LI/kQxqW8XxO1BVsH2hK.K7AkRdeUMYuhqd/wUOg2RqEe3n3kOY2'),
       ('王五', 'wangwu', '$2a$10$0LI/kQxqW8XxO1BVsH2hK.K7AkRdeUMYuhqd/wUOg2RqEe3n3kOY2'),
       ('xiaomingzhang', 'xmz', '$2a$10$0LI/kQxqW8XxO1BVsH2hK.K7AkRdeUMYuhqd/wUOg2RqEe3n3kOY2');


-- 角色表
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`
(
    `id`          bigint      NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `name`        varchar(20) NOT NULL COMMENT '角色名称',
    `create_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE = INNODB
  DEFAULT charset = utf8mb4 COMMENT ='系统角色表';

-- 角色表插入数据,初始化一个教师角色id为1
insert into role (id,name)
values (1,'教师角色'),
       (2,'课程管理'),
       (3,'人员管理'),
       (4,'一般管理员');

-- 管理员与角色关联表
DROP TABLE IF EXISTS `manager_role_rel`;
CREATE TABLE `manager_role_rel`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `manager_id`  bigint   NOT NULL COMMENT '管理员ID',
    `role_id`     bigint   NOT NULL COMMENT '角色ID',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  DEFAULT charset = utf8mb4 COMMENT ='管理员与角色关联表';

insert into manager_role_rel(manager_id, role_id)
values (1, 1),
       (2, 1),
       (1, 2);

-- 系统资源表
DROP TABLE IF EXISTS `mooc_resource`;
CREATE TABLE `mooc_resource`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`        varchar(20)  NOT NULL COMMENT '资源名称',
    `page_router` varchar(100) NOT NULL COMMENT '页面路由',
    `url`         varchar(200) NOT NULL COMMENT '资源URL',
    `parent_id`   bigint       NOT NULL COMMENT '父资源ID',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  DEFAULT charset = utf8mb4 COMMENT ='资源表';


-- 角色资源关联表
-- 暂时不用
DROP TABLE IF EXISTS `role_resource_rel`;
CREATE TABLE `role_resource_rel`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `role_id`     bigint   NOT NULL COMMENT '角色ID',
    `resource_id` bigint   NOT NULL COMMENT '资源ID',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  DEFAULT charset = utf8mb4 COMMENT ='角色资源关联表';

-- 菜单结构表
DROP TABLE IF EXISTS `menu_tree`;
CREATE TABLE `menu_tree`
(
    `id`          bigint       NOT NULL COMMENT 'ID',
    `label`       varchar(20)  NOT NULL COMMENT '菜单标签|名字',
    `menu_key`    varchar(48) COMMENT '菜单唯一标识',
    `icon`        varchar(48) COMMENT '菜单图标',
    `parent_id`   bigint       NOT NULL COMMENT '菜单父ID|0表示根节点',
    `permission`  varchar(255) NOT NULL COMMENT '权限',
    `router`      varchar(255)          DEFAULT NULL COMMENT '页面路由',
    `leaf`        tinyint      NOT NULL COMMENT '是否叶子节点|0不是，1是',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  DEFAULT charset = utf8mb4 COMMENT ='菜单结构表';
-- 初始化菜单
insert into menu_tree(id, label, menu_key, icon, parent_id, permission, router, leaf)
values (1, '监控中心', 'monitor', 'el-icon-view', 0, 'monitor:select', '/monitor', 0),
       (2, '课程管理', 'course', 'el-icon-video-camera', 0, 'course:select', '/course', 0),
       (21, '课程信息管理', 'courseInfo', null, 2, 'courseInfo:select', '/courseInfo', 0),
       (211, '查询', null, null, 21, 'courseInfo:select', null, 1),
       (212, '新增', null, null, 21, 'courseInfo:insert', null, 1),
       (213, '修改', null, null, 21, 'courseInfo:update', null, 1),
       (214, '删除', null, null, 21, 'courseInfo:delete', null, 1),
       (215, '导出', null, null, 21, 'courseInfo:export', null, 1),
       (22, '章节管理', 'chapter', null, 2, 'chapter:select', '/chapter', 0),
       (3, '人员管理', 'person', 'el-icon-s-custom', 0, 'user', '/person', 0),
       (311, '教师管理', 'teacher', null, 3, 'teacher:select', '/teacher', 0),
       (312, '普通用户', 'user', null, 3, 'user:select', '/user', 0),
       (31, '查询', null, null, 3, 'user:select', null, 1),
       (32, '新增', null, null, 3, 'user:insert', null, 1),
       (33, '修改', null, null, 3, 'user:update', null, 1),
       (34, '删除', null, null, 3, 'user:delete', null, 1),
       (35, '导出', null, null, 3, 'user:export', null, 1),

       (4, '分类管理', 'category', 'el-icon-notebook-2', 0, 'category:select', '/category', 0),
       (5, '文件管理', 'file', 'el-icon-folder', 0, 'file:select', '/file', 0),
       (6, '报表统计', 'charts', 'el-icon-s-data', 0, 'report:select', '/report', 0),
       (61, '查询', null, null, 6, 'report:select', null, 1),
       (62, '导出', null, null, 6, 'report:export', null, 1),

       (8, '消息通知管理', 'notice', 'el-icon-bell', 0, 'notice:select', '/notice', 0),

       (9, '系统管理', 'sys', 'el-icon-s-tools', 0, 'sys', '/system', 0),
       (91, '管理员管理', 'manager', null, 9, 'manager:select', '/system/manager', 0),
       (92, '角色管理', 'role', null, 9, 'role:select', '/system/role', 0),
       (921, '查询', null, null, 92, 'role:select', null, 1),
       (922, '新增', null, null, 92, 'role:insert', null, 1),
       (923, '修改', null, null, 92, 'role:update', null, 1),
       (925, '删除', null, null, 92, 'role:delete', null, 1),
       (929, '导出', null, null, 92, 'role:export', null, 1),
       (93, '系统日志', 'log', null, 9, 'log:select', '/system/log', 0),
       (931, '查询', null, null, 93, 'role:select', null, 1),
       (932, '导出', null, null, 93, 'role:export', null, 1),
       (95, '在线人员管理', 'online', null, 9, 'online:select', '/online/user', 0),
       (96, 'IP黑名单管理', 'ipBlacklist', null, 9, 'ipBlacklist:select', '/ipBlacklist', 0),
       (99, '系统图标设置', 'personalizedSet', null, 9, 'personalizedSet', '/personalizedSet', 0);




-- 管理员与角色关联表
DROP TABLE IF EXISTS `role_menu_rel`;
CREATE TABLE `role_menu_rel`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `role_id`     bigint   NOT NULL COMMENT '管理员ID',
    `menu_id`     bigint   NOT NULL COMMENT '菜单权限ID',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  DEFAULT charset = utf8mb4 COMMENT ='管理员与菜单关联表';

-- 1为教师角色， 2为课程管理员，3为人员管理员，4一般管理员
insert into role_menu_rel(role_id, menu_id)
values
-- 1有课程管理，章节管理，报表统计
(1, 1),
(1, 2),
(1, 21),
(1, 22),
(1, 6),
-- 1有课程权限，没有删除课程的权限
(2, 1),
(2, 11),
(2, 12),
(2, 13),

-- 2 有用户权限，没有新增人员的权限
(3, 2),
(3, 21),
(3, 23),
(3, 24),
(3, 211),
(3, 212),

-- 3 有用户、课程、报表权限
(4, 1),
(4, 11),
(4, 12),
(4, 13),
(4, 14),
(4, 15),
(4, 2),
(4, 21),
(4, 22),
(4, 23),
(4, 24),
(4, 25),
(4, 3),
(4, 31);
   
DROP TABLE IF EXISTS `login_amount_count`;
CREATE TABLE `login_amount_count`
(
    `id`   int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `amount` int NOT NULL COMMENT '登录人数|统计用户/教师',
    `count_time` varchar(64)     NOT NULL COMMENT '统计时间',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
     PRIMARY KEY (`id`)
) ENGINE = INNODB
  DEFAULT charset = utf8mb4 COMMENT ='登录数量统计表';
insert into login_amount_count(amount,count_time)
values
(18,'2021-01-17'),
(10,'2021-01-16'),
(6,'2021-01-15'),
(5,'2021-01-14'),
(4,'2021-01-13'),
(7,'2021-01-12');
  

-- 系统logo表
DROP TABLE IF EXISTS `mooc_logo`;
CREATE TABLE `mooc_logo` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `system_name` varchar(50) COMMENT '系统名',
  `system_logo_path` varchar(200)  COMMENT '系统主页logo图片地址',
  `login_logo_path` varchar(200)  COMMENT '登录页logo图片地址',
  `favicon_path` varchar(200) COMMENT '网站图标地址',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
)ENGINE = INNODB DEFAULT charset=utf8mb4 COMMENT='系统logo表';


-- 用户收藏表
DROP TABLE IF EXISTS `collection`;
CREATE TABLE `collection` (
  `id`  bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `course_id`  bigint UNSIGNED NOT NULL  COMMENT '课程Id',
  `user_id`  bigint UNSIGNED NOT NULL  COMMENT '用户Id|教师/普通用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   PRIMARY KEY (`id`),
   UNIQUE INDEX `uk_course_user_id` (`course_id`, `user_id`)
)ENGINE = INNODB DEFAULT charset=utf8mb4 COMMENT='用户收藏表';
insert into collection(course_id, user_id)
values (1,1),(2,1),(3,1),(4,1),(5,1),(6,1),(7,1),
(1,2),(2,2),(3,2),(4,2),(5,2),(6,2),(7,2);

-- IP黑名单表，一定程度主动防护DDos攻击
DROP TABLE IF EXISTS `mooc_ip_blacklist`;
CREATE TABLE `mooc_ip_blacklist` (
                              `id`  int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
                              `name` varchar(50) COMMENT '名称',
                              `ip`   varchar(255) NOT NULL COMMENT '禁用ip',
                              `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                              PRIMARY KEY (`id`),
                              UNIQUE INDEX `uk_ip` (`ip`)
)ENGINE = INNODB DEFAULT charset=utf8mb4 COMMENT='用户收藏表';
insert into mooc_ip_blacklist(name, ip)
values ('DDoS攻击者2','127.0.0.2'),('DDoS攻击者3','127.0.0.3'),('DDoS攻击者4','127.0.0.4');



DROP TABLE IF EXISTS `course_comment`;
-- 创建评论表
CREATE TABLE `course_comment`(
`id` int NOT NULL AUTO_INCREMENT COMMENT '评论id',
`user_id` int NOT NULL COMMENT '评论者id',
`course_id` int NOT NULL COMMENT '对应课程id',
`comment_content` varchar(512) NOT NULL COMMENT '评论内容',
`reply_num` int DEFAULT 0 COMMENT '回复数',
`comment_star` int DEFAULT 0 COMMENT '点赞数',
`status` int DEFAULT 0 COMMENT '状态是否已读,0未读,1已读,2已回复',
`type` int DEFAULT 0 COMMENT '0课程评论,1课程问答',
`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
PRIMARY KEY(`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='评论表';

-- article_comment表插入数据
insert into course_comment(user_id,course_id,comment_content,create_time)
values
(3,1,"不错,写的什么玩意,成功浪费我几分钟",'2021-03-18 06:17'),
(2,1,"很好啊",'2021-03-18 06:18'),
(1,1,"不错,写的什么玩意,成功浪费我几分钟",'2021-03-18 08:17'),
(2,2,"很好啊",'2021-03-19 06:17'),
(1,2,"不错,写的什么玩意,成功浪费我几分钟",'2021-03-18 15:17'),
(3,1,"不错,写的什么玩意,成功浪费我几分钟",'2021-03-18 06:17'),
(2,1,"很好啊",'2021-03-18 06:18'),
(1,1,"不错,写的什么玩意,成功浪费我几分钟",'2021-03-18 08:17'),
(2,2,"很好啊",'2021-03-19 06:17'),
(1,2,"不错,写的什么玩意,成功浪费我几分钟",'2021-03-18 15:17'),
(3,1,"不错,写的什么玩意,成功浪费我几分钟",'2021-03-18 06:17'),
(2,1,"很好啊",'2021-03-18 06:18'),
(1,1,"不错,写的什么玩意,成功浪费我几分钟",'2021-03-18 08:17'),
(2,2,"很好啊",'2021-03-19 06:17'),
(1,2,"不错,写的什么玩意,成功浪费我几分钟",'2021-03-18 15:17'),
(2,3,"很好啊",'2021-03-10 06:17');

insert into course_comment(user_id,course_id,comment_content,create_time,type)
values
(3,1,"老师您好，请问什么是SpringCloud",'2021-03-18 06:17',1),
(2,1,"老师您好,想问一下您在吗？",'2021-03-18 06:18',1);


DROP TABLE IF EXISTS `comment_reply`;
-- 创建评论回复表
CREATE TABLE `comment_reply`(
`id` int NOT NULL AUTO_INCREMENT COMMENT '回复表主键',
`comment_id` int NOT NULL COMMENT '对应的评论id',
`parent_id` int default 0 COMMENT '对应父回复id | 0表示回复对象是comment表下的回复',
`user_id` int NOT NULL COMMENT '回复者id',
`to_user_id` int NOT NULL COMMENT '回复对象id',
`reply_content` varchar(512) NOT NULL COMMENT '评论内容',
`reply_star` int DEFAULT 0 COMMENT '回复点赞数',
`status` int DEFAULT 0 COMMENT '状态是否已读,0未读,1已读,2已回复',
`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
PRIMARY KEY(`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='评论回复表';

-- comment_reply表插入数据
insert into comment_reply(id,comment_id,to_user_id,user_id,reply_content,create_time,parent_id)
values
(1,1,3,1,"哈哈",'2021-03-10 06:28',0),
(2,1,1,3,"呵呵",'2021-03-10 07:28',1),
(3,1,3,1,"傻子",'2021-04-10 07:28',2),
(4,1,1,3,"你才是傻子",'2021-04-10 13:28',3),
(5,1,3,1,"牛逼，咋不上天",'2021-04-10 13:29',4),
(6,1,3,1,"哈哈",'2021-04-10 14:29',2);


-- 创建通知表
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`(
`id` int NOT NULL AUTO_INCREMENT COMMENT '通知id',
`send_id` int default NULL  COMMENT '发送方的用户Id',
`accept_id` int default NULL COMMENT '接收方的用户id',
`course_id` int default NULL COMMENT '课程id，如果是创建课程',
`reply_id` int default NULL  COMMENT '回复id，如果是创建课程',
`content` varchar(512) not null COMMENT '消息内容',
`type` int NOT NULL DEFAULT 1 COMMENT '消息所属用户类型,1管理员，2教师',
`status` int NOT NULL DEFAULT 0 COMMENT '0未读，1已读，2已删除',
`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送通知的时间',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='通知表';

insert into notice(send_id,accept_id,course_id,content)
values
(null,null,1,'新增了课程《Spring入门实践》'),
(null,null,2,'新增了课程《Spring入门实践》'),
(null,null,3,'新增了课程《Spring入门实践》'),
(null,null,4,'新增了课程《Spring入门实践》'),
(null,null,5,'新增了课程《Spring入门实践》'),
(null,null,6,'新增了课程《Spring入门实践》'),
(null,null,7,'新增了课程《Spring入门实践》'),
(null,1,null,'新增了课程《Spring入门实践》'),
(null,2,null,'新增了课程《Spring入门实践》'),
(null,3,null,'新增了课程《Spring入门实践》'),
(null,4,null,'新增了课程《Spring入门实践》'),
(null,5,null,'新增了课程《Spring入门实践》');
