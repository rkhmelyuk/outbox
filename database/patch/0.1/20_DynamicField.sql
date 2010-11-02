
create table DynamicField (

    DynamicFieldId bigserial,
    MemberId bigint not null,

    Name varchar(200) not null,
    Label varchar(200) not null,
    Type smallint not null,
    Status smallint not null,
    Mandatory boolean not null,

    MinValue int null,
    MaxValue int null,
    MaxLength int null,

    Sequence int not null,

    primary key(DynamicFieldId),

    constraint FK_DynamicField_Member
        foreign key(MemberId)
        references Member(MemberId)
);

create index IX_DynamicField_MemberId_Status on DynamicField(MemberId, Status);