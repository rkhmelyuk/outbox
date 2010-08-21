
drop table if exists Member;

create table Member
(
    MemberId int not null,
    FirstName varchar(100) null,
    LastName varchar(100) null,
    Username varchar(250) not null,
    Password varchar(250) not null,
    Email varchar(512) not null,

    primary key(MemberId)
);
