
create table Subscription (
    SubscriptionId bigserial,
    SubscriberId char(40) not null,
    SubscriptionListId bigint not null,
    SubscriptionStatusId smallint not null,
    CreateDate timestamp not null,

    primary key(SubscriptionId),

    constraint FK_Subscription_Subscriber
        foreign key(SubscriberId)
        references Subscriber(SubscriberId),
    constraint FK_Subscription_SubscriptionList
        foreign key(SubscriptionListId)
        references SubscriptionList(SubscriptionListId),
    constraint FK_Subscription_SubscriptionStatus
        foreign key(SubscriptionStatusId)
        references SubscriptionStatus(SubscriptionStatusId)
);

create index IX_Subscription_SubscriberId on Subscription(SubscriberId);
create index IX_Subscription_SubscriptionListId_SubscriberId
    on Subscription(SubscriptionListId, SubscriberId);
create index IX_Subscription_SubscriptionListId_SubscriptionStatusId
    on Subscription(SubscriptionListId, SubscriptionStatusId);

