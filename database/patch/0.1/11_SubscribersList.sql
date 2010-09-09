
create table SubscribersList (

    SubscribersListId bigserial,
    MemberId bigint not null,
    Name varchar(200) not null,
    Description varchar(1000) null,
    SubscribersNumber bigint not null,

    primary key(SubscribersListId)
);
