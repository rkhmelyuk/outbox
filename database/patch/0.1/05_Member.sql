
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
values (1, 'System', 'Administrator', 'sysadmin', 'o73Ss/ScAeOjK/b70TR1r8VF9VrU0nbhGmMNOLgKNPU=', 'sysadmin@localhost', 15, 25, true, false, false, false);

insert into Member(MemberId, FirstName, LastName, Username, Password, Email, TimeZoneId, LanguageId, Enabled, AccountExpired, AccountLocked, PasswordExpired)
values (2, 'John', 'Smith', 'john', 'a2+HOqaR4USoUn0g41+yxd4OphGDjaI+MygoCvNa8C4=', 'jsmith@localhost', 17, 127, true, false, false, false);
