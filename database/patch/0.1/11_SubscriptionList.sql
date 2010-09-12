
create table SubscriptionList (

    SubscriptionListId bigserial,
    Name varchar(200) not null,
    Description varchar(1000) null,
    SubscribersNumber int null,

    MemberId bigint not null,
    
    CreateDate timestamp not null,

    primary key(SubscriptionListId)
);
