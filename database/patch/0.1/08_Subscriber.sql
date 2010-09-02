
create table Subscriber
(
    SubscriberId char(20) not null,

    Name varchar(512) null,
    Email varchar(512) not null,

    GenderId smallint null,
    Language smallint null,
    Timezone smallint null,

    MemberId integer not null,

    primary key(SubscriberId)
);
