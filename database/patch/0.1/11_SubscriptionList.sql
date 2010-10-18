
create table SubscriptionList (

    SubscriptionListId bigserial,
    MemberId bigint not null,

    Name varchar(200) not null,
    Description varchar(1000) null,
    Archived boolean not null,
    SubscribersNumber int null,
    
    CreateDate timestamp not null,

    primary key(SubscriptionListId)
);
