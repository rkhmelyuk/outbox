
create table NamePrefix
(
    NamePrefixId smallint not null,
    NamePrefixName varchar(10) not null,

    primary key(NamePrefixId)
);

insert into NamePrefix(NamePrefixId, NamePrefixName) values (1, 'Mr.');
insert into NamePrefix(NamePrefixId, NamePrefixName) values (2, 'Ms.');
insert into NamePrefix(NamePrefixId, NamePrefixName) values (3, 'Mrs.');
insert into NamePrefix(NamePrefixId, NamePrefixName) values (4, 'Miss.');
insert into NamePrefix(NamePrefixId, NamePrefixName) values (5, 'Dr.');
insert into NamePrefix(NamePrefixId, NamePrefixName) values (6, 'Fr.');
insert into NamePrefix(NamePrefixId, NamePrefixName) values (7, 'Sir');
insert into NamePrefix(NamePrefixId, NamePrefixName) values (8, 'Prof.');
