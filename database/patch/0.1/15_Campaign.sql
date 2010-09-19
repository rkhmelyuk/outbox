
create table Campaign (
    CampaignId bigserial,
    MemberId bigint not null,
    CampaignState smallint not null,

    Name varchar(500) not null,
    Description varchar(4000) not null,
    
    Subject varchar(1000) not null,
    TemplateId bigint null,

    CreateDate timestamp not null,
    ModifyDate timestamp null,
    StartDate timestamp null,
    EndDate timestamp null,

    primary key(CampaignId),

    constraint FK_Campaign_Member
        foreign key(MemberId)
        references Member(MemberId),
    constraint FK_Campaign_Template
        foreign key(TemplateId)
        references Template(TemplateId)
);

create index IX_Campaign_Member on Campaign(MemberId);
create index IX_Campaign_Template on Campaign(TemplateId);
