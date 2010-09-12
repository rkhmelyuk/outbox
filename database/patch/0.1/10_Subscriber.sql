
create table Subscriber
(
    SubscriberId char(40) not null,

    NamePrefixId smallint null,
    FirstName varchar(100) null,
    LastName varchar(100) null,
    Email varchar(512) not null,
    Enabled boolean not null,

    MemberId bigint not null,
    SubscriberTypeId bigint null,
    GenderId smallint null,
    LanguageId smallint null,
    TimezoneId smallint null,

    CreateDate timestamp not null,

    primary key(SubscriberId),

    constraint FK_Subscriber_Member
        foreign key(MemberId)
        references Member(MemberId),
    constraint FK_Subscriber_SubscriberType
        foreign key(SubscriberTypeId)
        references SubscriberType(SubscriberTypeId),
    constraint FK_Subscriber_Gender
        foreign key(GenderId)
        references Gender(GenderId),
    constraint FK_Subscriber_Language
        foreign key(LanguageId)
        references Language(LanguageId),
    constraint FK_Subscriber_Timezone
        foreign key(TimezoneId)
        references Timezone(TimezoneId),
    constraint FK_Subscriber_NamePrefix
        foreign key(NamePrefixId)
        references NamePrefix(NamePrefixId)
);

create index IX_Subscriber_MemberId_Email on Subscriber(MemberId, Email);
create index IX_Subscriber_MemberId_SubscriberTypeId on Subscriber(MemberId, SubscriberTypeId);
