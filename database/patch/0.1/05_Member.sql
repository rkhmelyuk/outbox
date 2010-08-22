
create table Member
(
    MemberId bigserial,
    FirstName varchar(100) null,
    LastName varchar(100) null,
    Username varchar(250) not null,
    Password varchar(64) not null,
    Email varchar(512) not null,
    TimeZoneId int null,
    LanguageId int null,

    Enabled boolean not null,
    AccountExpired boolean not null,
    AccountLocked boolean not null,
    PasswordExpired boolean not null,

    primary key(MemberId),

    constraint UQ_Member_Username unique(Username),
    constraint UQ_Member_Email unique(Email),

    constraint FK_Member_2_TimeZone
        foreign key(TimeZoneId)
        references TimeZone(TimeZoneId),

    constraint FK_Member_2_Language
        foreign key(LanguageId)
        references Language(LanguageId)
);


insert into Member(MemberId, FirstName, LastName, Username, Password, Email, TimeZoneId, LanguageId, Enabled, AccountExpired, AccountLocked, PasswordExpired)
values (1, 'System', 'Administrator', 'sysadmin', 'a3bdd2b3f49c01e3a32bf6fbd13475afc545f55ad4d276e11a630d38b80a34f5', 'sysadmin@localhost', null, null, true, false, false, false);

insert into Member(MemberId, FirstName, LastName, Username, Password, Email, TimeZoneId, LanguageId, Enabled, AccountExpired, AccountLocked, PasswordExpired)
values (2, 'John', 'Smith', 'john', 'b4b597c714a8f49103da4dab0266af0ee0ae4f8575250a84855c3d76941cd422', 'jsmith@localhost', null, null, true, false, false, false);
