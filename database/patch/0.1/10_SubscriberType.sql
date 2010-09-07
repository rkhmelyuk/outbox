
create table SubscriberType (
    SubscriberTypeId bigserial,
    MemberId bigint not null,
    Name varchar(200) not null,

    primary key(SubscriberTypeId),

    constraint FK_SubscriberType_Member
        foreign key(MemberId)
        references Member(MemberId)
);

create index IX_SubscriberType_MemberId on SubscriberType(MemberId);
