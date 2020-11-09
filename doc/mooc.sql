DROP DATABASE IF EXISTS `mooc`;

CREATE DATABASE `mooc`;

use mooc;

-- 课程表
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(20) NOT NULL COMMENT '课程名称',
  `teacher_id` INT NOT NULL COMMENT '讲师id',
  `summary` varchar(2000) COMMENT '课程概述',
  `duration` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '时长|单位秒',
  `image` varchar(100) NOT NULL COMMENT '封面图片',
  `learning_num` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '学习人数',
  `comment_num` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '评论数',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态|0草稿/1发布/2禁用/3已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   PRIMARY KEY (`id`),
   UNIQUE KEY `uk_name` (`name`)
)ENGINE = INNODB DEFAULT charset=utf8mb4 COMMENT='课程表';

insert into course (name,teacher_id,summary, duration,image)
values
('测试课程01',1, '这是一门测试课程', 7200,"F:\\image\\default.png"),
('测试课程02',1, '这是一门测试课程', 7200,"F:\\image\\default.png"),
('测试课程03',1, '这是一门测试课程', 7200,"F:\\image\\default.png"),
('测试课程04',1, '这是一门测试课程', 7200,"F:\\image\\default.png");



-- 课程大章表
DROP TABLE IF EXISTS `chapter`;
CREATE TABLE `chapter` (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `course_id` bigint UNSIGNED NOT NULL COMMENT '课程id',
  `name` varchar(30) NOT NULL COMMENT '名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   PRIMARY KEY (`id`)
)ENGINE = INNODB DEFAULT charset=utf8mb4 COMMENT='章节表';

insert into `chapter` (course_id, name) values (1, '测试大章01');
insert into `chapter` (course_id, name) values (1, '测试大章02');
insert into `chapter` (course_id, name) values (1, '测试大章03');
insert into `chapter` (course_id, name) values (1, '测试大章04');
insert into `chapter` (course_id, name) values (1, '测试大章05');
insert into `chapter` (course_id, name) values (1, '测试大章06');
insert into `chapter` (course_id, name) values (1, '测试大章07');
insert into `chapter` (course_id, name) values (1, '测试大章08');
insert into `chapter` (course_id, name) values (1, '测试大章09');
insert into `chapter` (course_id, name) values (1, '测试大章10');
insert into `chapter` (course_id, name) values (1, '测试大章11');
insert into `chapter` (course_id, name) values (1, '测试大章12');
insert into `chapter` (course_id, name) values (1, '测试大章13');
insert into `chapter` (course_id, name) values (1, '测试大章14');
insert into `chapter` (course_id, name) values (1, '测试大章15');
insert into `chapter` (course_id, name) values (1, '测试大章16');
insert into `chapter` (course_id, name) values (1, '测试大章17');

-- 大章小节表
DROP TABLE IF EXISTS `chapter_section`;
create table `chapter_section` (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(50) NOT NULL COMMENT '标题',
  `course_id` bigint UNSIGNED NOT NULL COMMENT '课程id',
  `chapter_id` bigint UNSIGNED NOT NULL COMMENT '章节id',
  `video` varchar(200) NOT NULL COMMENT '视频',
  `duration` INT NOT NULL COMMENT '时长|单位秒',
  `sort` INT NOT NULL COMMENT '顺序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
)ENGINE = INNODB DEFAULT charset=utf8mb4 COMMENT='课程小节表';

insert into `chapter_section` (title, course_id, chapter_id, video, duration,sort)
values
('测试小节01', 1, 1, '', 500,1),
('测试小节02', 1, 1, '', 500,2),
('测试小节02', 1, 1, '', 500,3);


-- 文件表
DROP TABLE IF EXISTS `mooc_file`;
CREATE TABLE `mooc_file` (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(50) NOT NULL COMMENT '文件名',
  `file_path` varchar(200) NOT NULL COMMENT '视频相对路径', 
  `file_size` INT NOT NULL COMMENT '大小|字节B',
  `file_suffix` varchar(10) NOT NULL COMMENT '文件后缀',
  `file_key` varchar(50) NOT NULL COMMENT '文件唯一标识',
  `shard_index` INT NOT NULL COMMENT '分片下标',
  `shard_count` INT NOT NULL COMMENT '总共分片数',
  `shard_size` INT NOT NULL COMMENT '分片大小',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
)ENGINE = INNODB DEFAULT charset=utf8mb4 COMMENT='文件表';

-- 登录日志表
DROP TABLE IF EXISTS `login_log`;
CREATE TABLE `login_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `logname` varchar(255) NOT NULL COMMENT '日志名称',
  `username` varchar(50) NOT NULL COMMENT '管理员账号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `succeed` varchar(255) DEFAULT NULL COMMENT '是否执行成功',
  `message` text COMMENT '具体消息',
  `ip` varchar(255) DEFAULT NULL COMMENT '登录ip',
  `user_id` varchar(36) DEFAULT NULL,
  `system_type` varchar(255) DEFAULT NULL COMMENT '系统类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8793 DEFAULT CHARSET=utf8 COMMENT='登录日志表';



-- 普通用户表
DROP TABLE IF EXISTS `mooc_user`;
CREATE TABLE `mooc_user`(
`id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
`user_iamge` varchar(50) NOT NULL COMMENT '用户头像',
`name` varchar(20) NOT NULL COMMENT '用户昵称',
`account` varchar(10) NOT NULL COMMENT '登录账号',
`password` varchar(16) NOT NULL COMMENT '登录密码',
`user_type` varchar(4) NOT NULL COMMENT'类型，教师/普通用户',
`status` tinyint NOT NULL DEFAULT 1 COMMENT '用户状态| 1正常，2禁用，3已删除',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
PRIMARY KEY (`id`)
)ENGINE = INNODB DEFAULT charset=utf8 COMMENT='普通用户表';


-- 系统管理员表
DROP TABLE IF EXISTS `mooc_manager`;
CREATE TABLE `mooc_manager`(
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
`name` varchar(20) NOT NULL COMMENT '名字',
`account` varchar(10) NOT NULL COMMENT '登录账号',
`password` varchar(100) NOT NULL COMMENT '密码',
`status` tinyint NOT NULL DEFAULT 1 COMMENT '用户状态| 1正常，2禁用，3已删除',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
PRIMARY KEY (`id`)
)ENGINE = INNODB DEFAULT charset=utf8mb4 COMMENT='系统管理员表';

insert into mooc_manager(name,account,password) values
('张三','zhangsan','$2a$10$0LI/kQxqW8XxO1BVsH2hK.K7AkRdeUMYuhqd/wUOg2RqEe3n3kOY2'),
('李四','lisi','$2a$10$0LI/kQxqW8XxO1BVsH2hK.K7AkRdeUMYuhqd/wUOg2RqEe3n3kOY2'),
('王五','wangwu','$2a$10$0LI/kQxqW8XxO1BVsH2hK.K7AkRdeUMYuhqd/wUOg2RqEe3n3kOY2'),
('xiaomingzhang','xmz','123');



-- 角色表
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`(
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
`name` varchar(20) NOT NULL COMMENT '角色名称',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
PRIMARY KEY (`id`),
UNIQUE KEY `uk_name` (`name`)
)ENGINE = INNODB DEFAULT charset=utf8mb4 COMMENT='系统角色表';

-- 角色表插入数据
insert into role (name)
values
('课程管理'),
('人员管理'),
('一般管理员');

-- 管理员与角色关联表
DROP TABLE IF EXISTS `manager_role_rel`;
CREATE TABLE `manager_role_rel`(
`id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
`manager_id` bigint NOT NULL COMMENT '管理员ID',
`role_id` bigint NOT NULL COMMENT '角色ID',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
PRIMARY KEY (`id`)
)ENGINE = INNODB DEFAULT charset=utf8mb4 COMMENT='管理员与角色关联表';

insert into manager_role_rel(manager_id,role_id)
values
(1,1),
(2,1),
(1,2);

-- 系统资源表
DROP TABLE IF EXISTS `mooc_resource`;
CREATE TABLE `mooc_resource`(
`id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
`name` varchar(20) NOT NULL COMMENT '资源名称',
`page_router` varchar(100) NOT NULL COMMENT '页面路由',
`url` varchar(200) NOT NULL COMMENT '资源URL',
`parent_id` bigint NOT NULL COMMENT '父资源ID',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
PRIMARY KEY (`id`)
)ENGINE = INNODB DEFAULT charset=utf8mb4 COMMENT='资源表';


-- 角色资源关联表
DROP TABLE IF EXISTS `role_resource_rel`;
CREATE TABLE `role_resource_rel`(
`id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
`role_id` bigint NOT NULL COMMENT '角色ID',
`resource_id` bigint NOT NULL COMMENT '资源ID',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
PRIMARY KEY (`id`)
)ENGINE = INNODB DEFAULT charset=utf8mb4 COMMENT='角色资源关联表';

-- 菜单结构表
DROP TABLE IF EXISTS `menu_tree`;
CREATE TABLE `menu_tree`(
`id` bigint NOT NULL COMMENT 'ID',
`label` varchar(20) NOT NULL COMMENT '菜单标签|名字',
`parent_id` bigint NOT NULL COMMENT '菜单父ID|0表示根节点',
`permission` varchar(255) NOT NULL COMMENT '权限',
`router` varchar(255) DEFAULT NULL COMMENT '页面路由',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
PRIMARY KEY (`id`)
)ENGINE = INNODB DEFAULT charset=utf8mb4 COMMENT='菜单结构表';

-- 初始化菜单
insert into menu_tree(id,label,parent_id,permission)
values
(1,'课程管理',0,'course','/course'),
  (11,'查询',1,'course:select'),
  (12,'新增',1,'course:instert'),
  (13,'修改',1,'course:update'),
  (14,'删除',1,'course:delete'),
  (15,'导出',1,'course:export'),
(2,'用户管理',0,'user','/user'),
  (21,'查询',2,'user:select'),
  (22,'新增',2,'user:instert'),
  (23,'修改',2,'user:update'),
  (24,'删除',2,'user:delete'),
  (25,'导出',2,'user:export'),
(3,'报表统计',0,'report','/report'),
  (31,'查询',3,'report:select'),
  (32,'导出',3,'report:export'),
(4,'系统管理',0,'sys','/system'),
  (41,'管理员管理',4,'sys:manager','/system/manager'),
    (411,'xxx管理',41,'user:select'),
  (42,'角色管理',4,'sys:role','/system/role'),
    (421,'查询',42,'sys:role:select'),
    (422,'新增',42,'sys:role:instert'),
    (423,'修改',42,'sys:role:update'),
	(424,'删除',42,'sys:role:delete'),
	(425,'导出',42,'sys:role:export'),
  (43,'系统日志',4,'sys:log','/system/log'),
  (44,'系统设置',4,'sys:setting','/system/setting');


-- 管理员与角色关联表
DROP TABLE IF EXISTS `role_menu_rel`;
CREATE TABLE `role_menu_rel`(
`id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
`manager_id` bigint NOT NULL COMMENT '管理员ID',
`menu_id` bigint NOT NULL COMMENT '菜单权限ID',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
PRIMARY KEY (`id`)
)ENGINE = INNODB DEFAULT charset=utf8mb4 COMMENT='管理员与菜单关联表';

-- 1为课程管理员，2为人员管理员，3一般管理员
insert into role_menu_rel(manager_id,menu_id)
values
-- 1有课程权限，没有删除课程的权限
(1,1),(1,11),(1,12),(1,13),

-- 2 有用户权限，没有新增人员的权限
(2,2),(2,21),(2,23),(2,24),

-- 3 有用户、课程、报表权限
(3,1),(3,11),(3,12),(3,13),(3,14),(3,15),
(3,2),(3,21),(3,22),(3,23),(3,24),(3,25),
(3,3),(3,31);
