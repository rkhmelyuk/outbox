
create table TrackingReference (

    TrackingReferenceId char(40) not null,
    CampaignId bigint not null,
    SubscriberId char(40) not null,
    CampaignMessageId char(40) not null,
    ReferenceType smallint not null,
    Reference varchar(4000) not null,

    primary key(TrackingReferenceId),

    constraint FK_TrackingReference_Campaign
        foreign key(CampaignId)
        references Campaign(CampaignId),
    constraint FK_TrackingRefenrece_Subscriber
        foreign key(SubscriberId)
        references Subscriber(SubscriberId),
    constraint FK_TrackingReference_CampaignMessage
        foreign key(CampaignMessageId)
        references CampaignMessage(CampaignMessageId)
);
