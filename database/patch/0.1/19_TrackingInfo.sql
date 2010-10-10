
create table TrackingInfo (

    TrackingInfoId char(40) not null,
    CampaignId bigint not null,
    SubscriberId char(40) not null,
    TrackingReferenceId char(40) not null,
    Click boolean not null,
    Open boolean not null,

    Date timestamp not null,
    BrowserName varchar(100) null,
    BrowserVersion varchar(100) null,
    OperatingSystem varchar(100) null,
    IPAddress varchar(40) null,
    Locale varchar(50) null,
    
    LocationCountryCode char(2) null,
    LocationCountryName varchar(100) null,
    LocationCity varchar(500) null,
    LocationRegion varchar(500) null,
    LocationPostCode varchar(20) null,
    LocationLatitude numeric null,
    LocationLongitude numeric null,

    primary key(TrackingInfoId)
);
