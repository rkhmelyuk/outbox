
create table SubscriptionStatus (
    SubscriptionStatusId smallint not null,
    Name varchar(50) not null,

    primary key(SubscriptionStatusId)
);

insert into SubscriptionStatus(SubscriptionStatusId, Name) values (1, 'Subscribed');
insert into SubscriptionStatus(SubscriptionStatusId, Name) values (2, 'Unsubscribed');
insert into SubscriptionStatus(SubscriptionStatusId, Name) values (3, 'Recipient Unsubscribed');
