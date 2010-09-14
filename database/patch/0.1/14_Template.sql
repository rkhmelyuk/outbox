
create table Template (
    TemplateId bigserial,
    MemberId bigint not null,
    Name varchar(200) not null,
    Description varchar(4000) null,
    TemplateBody varchar(10485760) not null,

    CreateDate timestamp not null,
    ModifyDate timestamp not null,

    primary key(TemplateId),

    constraint FK_Template_Member
        foreign key(MemberId)
        references Member(MemberId)
);

create index IX_Template_MemberId on Template(MemberId);
