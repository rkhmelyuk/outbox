
create table TimeZone
(
    TimeZoneId serial,
    Name varchar(400) not null,
    TimeOffset int not null,

    primary key(TimeZoneId)
);
