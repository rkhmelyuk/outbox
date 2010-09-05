
create table Role (
    RoleId serial,
    Authority varchar(50) not null,
    RoleName varchar(50) not null,

    primary key (RoleId)
);

insert into Role (RoleId, Authority, RoleName) values (1, 'ROLE_USER', 'User');
insert into Role (RoleId, Authority, RoleName) values (2, 'ROLE_SYSADMIN', 'System Administrator');
insert into Role (RoleId, Authority, RoleName) values (3, 'ROLE_CLIENT', 'Client');
