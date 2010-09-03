
create table Subscriber
(
    SubscriberId char(20) not null,

    FirstName varchar(100) null,
    LastName varchar(100) null,
    Email varchar(512) not null,

    GenderId smallint null,
    LanguageId smallint null,
    TimezoneId smallint null,
    MemberId integer not null,

    CreateDate timestamp not null,

    primary key(SubscriberId),

    constraint FK_Subscriber_2_Gender
        foreign key(GenderId)
        references Gender(GenderId),
    constraint FK_Subscriber_2_Language
        foreign key(LanguageId)
        references Language(LanguageId),
    constraint FK_Subscriber_2_Timezone
        foreign key(TimezoneId)
        references Timezone(TimezoneId)
);

create index IX_Subscriber_Email on Subscriber(Email);
create index IX_Subscriber_MemberId on Subscriber(MemberId);
