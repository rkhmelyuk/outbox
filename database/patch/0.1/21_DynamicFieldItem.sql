
create table DynamicFieldItem (

    DynamicFieldItemId bigserial,
    DynamicFieldId bigint not null,

    Name varchar(500) not null,
    Sequence int not null,

    primary key(DynamicFieldItemId),

    constraint FK_DynamicFieldItem_DynamicField
        foreign key(DynamicFieldId)
        references DynamicField(DynamicFieldId)
);

create index IX_DynamicFieldItem_DynamicFieldId on DynamicField(DynamicFieldId);