-- mysql
create table if not exists proxy_info
(
    bh         varchar(300)  not null
        primary key,
    ip         varchar(300)  null comment 'ip',
    port       varchar(30)   null comment '端口',
    valid      int default 1 null comment '是否有效，1有效，0失效',
    createtime datetime      null comment '创建时间',
    updatetime datetime      null comment '修改时间'
)
    comment '代理IP表';


