
create table DynamicFieldValue (

    DynamicFieldValueId bigserial,
    DynamicFieldId bigint not null,
    SubscriberId char(40) not null,

    StringValue varchar(9999) null,
    NumberValue decimal(8, 4) null,
    BooleanValue boolean null,
    DynamicFieldItemId bigint null,

    primary key(DynamicFieldValueId),

    constraint FK_DynamicFieldValue_DynamicField
        foreign key(DynamicFieldId)
        references DynamicField(DynamicFieldId)
);

create index IX_DynamicFieldValue_SubscriberId on DynamicFieldValue(SubscriberId);