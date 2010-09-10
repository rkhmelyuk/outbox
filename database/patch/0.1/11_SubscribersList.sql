
create table SubscribersList (

    SubscribersListId bigserial,
    Name varchar(200) not null,
    Description varchar(1000) null,
    SubscribersNumber int not null,

    MemberId bigint not null,
    
    CreateDate timestamp not null,

    primary key(SubscribersListId)
);
