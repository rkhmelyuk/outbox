
create table CampaignSubscription (

    CampaignSubscriptionId bigserial,
    CampaignId bigint not null,
    SubscriptionListId bigint not null,

    primary key(CampaignSubscriptionId),

    constraint FK_CampaignSubscription_Campaign
        foreign key(CampaignId)
        references Campaign(CampaignId),
    constraint FK_CampaignSubscription_Subscriptionlist
        foreign key(SubscriptionListId)
        references SubscriptionList(SubscriptionListId)
);
