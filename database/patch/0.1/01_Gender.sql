
create table Gender
(
    GenderId smallint not null,
    GenderName varchar(10) not null,

    primary key(GenderId)
);


insert into Gender (GenderId, GenderName) values (1, 'Female');
insert into Gender (GenderId, GenderName) values (2, 'Male');
