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


DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`(
                           `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
                           `name` varchar(30) NOT NULL COMMENT '分类名称',
                           `description` varchar(200) DEFAULT NULL COMMENT '分类描述',
                           `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `uk_name` (`name`)
)ENGINE = INNODB DEFAULT charset=utf8mb4 COMMENT='课程分类表';
insert into `category` (name,description)
values
('其他','其他课程'),
('后台开发','后台开发相关课程'),
('前端开发','前端开发相关课程'),
('测试工程师','description'),
('文学创作','文学创作'),
('数据库','我热爱学习数据库');

DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`(
                      `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
                      category_id int UNSIGNED NOT NULL COMMENT '分类id',
                      `name` varchar(30) NOT NULL COMMENT '标签名',
                      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                      `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                      PRIMARY KEY (`id`),
                      UNIQUE KEY `uk_name` (`name`)
)ENGINE = INNODB DEFAULT charset=utf8mb4 COMMENT='标签表';
insert into `tag` (category_id,name)
values
(2,'java'),
(2,'python'),
(2,'golang'),
(2,'spring'),
(3,'vue'),
(3,'js'),
(3,'html');

-- 课程标签关联表
DROP TABLE IF EXISTS `course_tag_rel`;
CREATE TABLE `course_tag_rel`(
                                 `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
                                 `course_id` int UNSIGNED NOT NULL COMMENT '课程id',
                                 `tag_id` int UNSIGNED NOT NULL COMMENT '标签id',
                                 `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 PRIMARY KEY (`id`)
)ENGINE = INNODB DEFAULT charset=utf8mb4 COMMENT='课程标签关联表';



-- 课程大章表
DROP TABLE IF EXISTS `chapter`;
CREATE TABLE `chapter` (
                           `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
                           `course_id` bigint UNSIGNED NOT NULL COMMENT '课程id',
                           `name` varchar(30) NOT NULL COMMENT '名称',
                           `sort` int NOT NULL COMMENT '顺序',
                           `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                           PRIMARY KEY (`id`)
)ENGINE = INNODB DEFAULT charset=utf8mb4 COMMENT='章节表';
insert into `chapter` (course_id, name,sort) values (1, '测试大章01',1);
insert into `chapter` (course_id, name,sort) values (1, '测试大章02',2);
insert into `chapter` (course_id, name,sort) values (1, '测试大章03',3);
insert into `chapter` (course_id, name,sort) values (1, '测试大章04',4);
insert into `chapter` (course_id, name,sort) values (1, '测试大章05',5);
insert into `chapter` (course_id, name,sort) values (1, '测试大章06',6);
insert into `chapter` (course_id, name,sort) values (1, '测试大章07',7);
insert into `chapter` (course_id, name,sort) values (1, '测试大章08',8);
insert into `chapter` (course_id, name,sort) values (1, '测试大章09',9);
insert into `chapter` (course_id, name,sort) values (1, '测试大章10',10);
insert into `chapter` (course_id, name,sort) values (1, '测试大章11',11);
insert into `chapter` (course_id, name,sort) values (1, '测试大章12',12);
insert into `chapter` (course_id, name,sort) values (1, '测试大章13',13);
insert into `chapter` (course_id, name,sort) values (1, '测试大章14',14);
insert into `chapter` (course_id, name,sort) values (1, '测试大章15',15);
insert into `chapter` (course_id, name,sort) values (1, '测试大章16',16);
insert into `chapter` (course_id, name,sort) values (1, '测试大章17',17);

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
('测试小节03', 1, 1, '', 500,3),
('测试小节01', 1, 2, '', 500,1),
('测试小节02', 1, 2, '', 500,2),
('测试小节02', 1, 2, '', 500,3),
('测试小节01', 1, 2, '', 500,1),
('测试小节02', 1, 2, '', 500,2),
('测试小节02', 1, 2, '', 500,3),
('测试小节01', 1, 3, '', 500,1),
('测试小节02', 1, 3, '', 500,2),
('测试小节02', 1, 4, '', 500,3),
('测试小节01', 1, 4, '', 500,1),
('测试小节02', 1, 5, '', 500,2),
('测试小节02', 1, 5, '', 500,3),
('测试小节01', 1, 6, '', 500,1),
('测试小节02', 1, 6, '', 500,2),
('测试小节02', 1, 6, '', 500,3),
('测试小节01', 1, 7, '', 500,1),
('测试小节02', 1, 8, '', 500,2);




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
                             `log_name` varchar(255) NOT NULL COMMENT '日志名称',
                             `account` varchar(50) NOT NULL COMMENT '管理员账号',
                             `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `succeed` varchar(255) DEFAULT NULL COMMENT '是否执行成功',
                             `message` text COMMENT '具体消息',
                             `ip` varchar(255) DEFAULT NULL COMMENT '登录ip',
                             `system_type` varchar(255) DEFAULT NULL COMMENT '系统类型',
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8793 DEFAULT CHARSET=utf8 COMMENT='登录日志表';



-- 普通用户表（教师/用户）
DROP TABLE IF EXISTS `mooc_user`;
CREATE TABLE `mooc_user`(
                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                            `user_iamge` varchar(50) NOT NULL COMMENT '用户头像',
                            `name` varchar(40) NOT NULL COMMENT '用户昵称',
                            `account` varchar(20) NOT NULL COMMENT '登录账号',
                            `password` varchar(1048) NOT NULL COMMENT '登录密码',
                            `user_type` varchar(4) NOT NULL COMMENT'类型，教师/普通用户',
                            `status` tinyint NOT NULL DEFAULT 1 COMMENT '用户状态| 1正常，2禁用，3已删除',
                            `login_time` datetime DEFAULT NULL COMMENT '最近登录时间',
                            `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `uk_account` (`account`)
)ENGINE = INNODB DEFAULT charset=utf8 COMMENT='普通用户表';
insert into `mooc_user`(user_iamge,name,account,password,user_type)
values
('/image/default.png','张三丰','zhangsanfeng','$2a$10$0LI/kQxqW8XxO1BVsH2hK.K7AkRdeUMYuhqd/wUOg2RqEe3n3kOY2','普通用户'),
('/image/default.png','张四丰','zhangsifeng','$2a$10$0LI/kQxqW8XxO1BVsH2hK.K7AkRdeUMYuhqd/wUOg2RqEe3n3kOY2','普通用户'),
('/image/default.png','张五丰','zhangwufeng','$2a$10$0LI/kQxqW8XxO1BVsH2hK.K7AkRdeUMYuhqd/wUOg2RqEe3n3kOY2','普通用户'),
('/image/default.png','张一丰','zhangyifeng','$2a$10$0LI/kQxqW8XxO1BVsH2hK.K7AkRdeUMYuhqd/wUOg2RqEe3n3kOY2','普通用户'),
('/image/default.png','张六丰','zhangliufeng','$2a$10$0LI/kQxqW8XxO1BVsH2hK.K7AkRdeUMYuhqd/wUOg2RqEe3n3kOY2','普通用户'),
('/image/default.png','张二丰','zhangerfeng','$2a$10$0LI/kQxqW8XxO1BVsH2hK.K7AkRdeUMYuhqd/wUOg2RqEe3n3kOY2','普通用户'),
('/image/default.png','go老师','gotodo','$2a$10$0LI/kQxqW8XxO1BVsH2hK.K7AkRdeUMYuhqd/wUOg2RqEe3n3kOY2','教师'),
('/image/default.png','以父之名','yifuzhiming','$2a$10$0LI/kQxqW8XxO1BVsH2hK.K7AkRdeUMYuhqd/wUOg2RqEe3n3kOY2','教师');

-- 系统管理员表
DROP TABLE IF EXISTS `mooc_manager`;
CREATE TABLE `mooc_manager`(
                               `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                               `name` varchar(40) NOT NULL COMMENT '名字',
                               `account` varchar(20) NOT NULL COMMENT '登录账号',
                               `password` varchar(1048) NOT NULL COMMENT '密码',
                               `status` tinyint NOT NULL DEFAULT 1 COMMENT '用户状态| 1正常，2禁用，3已删除',
                               `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `uk_account` (`account`)
)ENGINE = INNODB DEFAULT charset=utf8mb4 COMMENT='系统管理员表';

-- 密码是root
insert into mooc_manager(name,account,password) values
('张三','zhangsan','$2a$10$0LI/kQxqW8XxO1BVsH2hK.K7AkRdeUMYuhqd/wUOg2RqEe3n3kOY2'),
('李四','lisi','$2a$10$0LI/kQxqW8XxO1BVsH2hK.K7AkRdeUMYuhqd/wUOg2RqEe3n3kOY2'),
('王五','wangwu','$2a$10$0LI/kQxqW8XxO1BVsH2hK.K7AkRdeUMYuhqd/wUOg2RqEe3n3kOY2'),
('xiaomingzhang','xmz','$2a$10$0LI/kQxqW8XxO1BVsH2hK.K7AkRdeUMYuhqd/wUOg2RqEe3n3kOY2');



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
                            `menu_key` varchar(48) COMMENT '菜单唯一标识',
                            `icon` varchar(48) COMMENT '菜单图标',
                            `parent_id` bigint NOT NULL COMMENT '菜单父ID|0表示根节点',
                            `permission` varchar(255) NOT NULL COMMENT '权限',
                            `router` varchar(255) DEFAULT NULL COMMENT '页面路由',
                            `leaf` tinyint NOT NULL COMMENT '是否叶子节点|0不是，1是',
                            `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                            PRIMARY KEY (`id`)
)ENGINE = INNODB DEFAULT charset=utf8mb4 COMMENT='菜单结构表';
-- 初始化菜单
insert into menu_tree(id,label,menu_key,icon,parent_id,permission,router,leaf)
values
(1,'课程管理','course','el-icon-video-camera',0,'course:select','/course',0),
(11,'课程信息管理','courseInfo',null,1,'courseInfo:select','/courseInfo',0),
(111,'查询',null,null,11,'courseInfo:select',null,1),
(112,'新增',null,null,11,'courseInfo:instert',null,1),
(113,'修改',null,null,11,'courseInfo:update',null,1),
(114,'删除',null,null,11,'courseInfo:delete',null,1),
(115,'导出',null,null,11,'courseInfo:export',null,1),
(12,'章节管理','chapter',null,1,'chapter:select','/chapter',0),
(2,'人员管理','person','el-icon-s-custom',0,'user','/person',0),
(211,'教师管理','teacher',null,2,'teacher:select','/teacher',0),
(212,'普通用户','user',null,2,'user:select','/user',0),
(21,'查询',null,null,2,'user:select',null,1),
(22,'新增',null,null,2,'user:instert',null,1),
(23,'修改',null,null,2,'user:update',null,1),
(24,'删除',null,null,2,'user:delete',null,1),
(25,'导出',null,null,2,'user:export',null,1),

(3,'分类管理','category','el-icon-notebook-2',0,'category:select','/category',0),
(4,'文件管理','file','el-icon-folder',0,'file:select','/file',0),

(5,'报表统计','charts','el-icon-s-data',0,'report:select','/report',0),
(51,'查询',null,null,5,'report:select',null,1),
(52,'导出',null,null,5,'report:export',null,1),
(6,'系统管理','sys','el-icon-s-tools',0,'sys','/system',0),
(61,'管理员管理','manager',null,6,'manager:select','/system/manager',0),
(62,'角色管理','role',null,6,'role:select','/system/role',0),
(621,'查询',null,null,62,'role:select',null,1),
(622,'新增',null,null,62,'role:instert',null,1),
(623,'修改',null,null,62,'role:update',null,1),
(625,'删除',null,null,62,'role:delete',null,1),
(626,'导出',null,null,62,'role:export',null,1),
(63,'系统日志','log',null,6,'log:select','/system/log',0),
(631,'查询',null,null,63,'role:select',null,1),
(632,'导出',null,null,63,'role:export',null,1),
(65,'在线人员管理','online',null,6,'online:select','/online/user',0);


-- 管理员与角色关联表
DROP TABLE IF EXISTS `role_menu_rel`;
CREATE TABLE `role_menu_rel`(
                                `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                `role_id` bigint NOT NULL COMMENT '管理员ID',
                                `menu_id` bigint NOT NULL COMMENT '菜单权限ID',
                                `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                PRIMARY KEY (`id`)
)ENGINE = INNODB DEFAULT charset=utf8mb4 COMMENT='管理员与菜单关联表';

-- 1为课程管理员，2为人员管理员，3一般管理员
insert into role_menu_rel(role_id,menu_id)
values
-- 1有课程权限，没有删除课程的权限
(1,1),(1,11),(1,12),(1,13),

-- 2 有用户权限，没有新增人员的权限
(2,2),(2,21),(2,23),(2,24),(2,211),(2,212),

-- 3 有用户、课程、报表权限
(3,1),(3,11),(3,12),(3,13),(3,14),(3,15),
(3,2),(3,21),(3,22),(3,23),(3,24),(3,25),
(3,3),(3,31);
