
create table CampaignMessage (

    CampaignMessageId char(40) not null,
    CampaignId bigint not null,
    SubscriberId char(40) not null,
    SubscriberEmail varchar(512) not null,
    SendDate timestamp not null,

    primary key (CampaignMessageId),

    constraint FK_CampaignMessage_Campaign
        foreign key(CampaignId)
        references Campaign(CampaignId),
    constraint FK_CampaignMessage_Subscriber
        foreign key(SubscriberId)
        references Subscriber(SubscriberId)
);
