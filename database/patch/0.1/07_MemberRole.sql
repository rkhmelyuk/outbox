

create table MemberRole (

    MemberId int not null,
    RoleId int not null,
    
    primary key(MemberId, RoleId)
);

insert into MemberRole (MemberId, RoleId) values (1, 1);
insert into MemberRole (MemberId, RoleId) values (2, 2);
