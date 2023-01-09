DROP TABLE IF EXISTS user;

CREATE TABLE user
(
    id INT AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
    age INT(11) NULL DEFAULT NULL COMMENT '年龄',
    email VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
    PRIMARY KEY (id)
);

CREATE TABLE student (
  id INT PRIMARY KEY AUTO_INCREMENT COMMENT '学号',
  name VARCHAR(200) COMMENT '姓名',
  age    int COMMENT '年龄'
) COMMENT='学生信息';

CREATE TABLE product(
  id INT PRIMARY KEY AUTO_INCREMENT COMMENT '商品ID',
  cid int default 1 COMMENT '商品分类',
  title VARCHAR(200) COMMENT '商品名称',
  prod_no VARCHAR(50) COMMENT '商品编码',
  prod_desc VARCHAR(1000) COMMENT '商品描述',
  img_url varchar(255) COMMENT '商品图片链接',
  price decimal(10,2) COMMENT '商品价格'
) COMMENT='商品表';


