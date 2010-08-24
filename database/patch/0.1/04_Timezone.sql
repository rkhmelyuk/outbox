
create table TimeZone
(
    TimeZoneId int not null,
    Name varchar(400) not null,
    TimeOffset float not null,

    primary key(TimeZoneId)
);

insert into TimeZone (TimeZoneId, Name, TimeOffset)
values
(1, '(-12:00) Enitwetok, Kwajalien', -12),
(2, '(-11:00) Midway Island, Samoa', -11),
(3, '(-10:00) Hawaii, Papeete', -10),
(4, '(-09:30) French Polynesia, Marquesas Islands', -9.5),
(5, '(-09:00) Alaska', -9),
(6, '(-08:00) Pacific Time (US & Canada)', -8),
(7, '(-07:00) Mountain Time (US & Canada)', -7),
(8, '(-06:00) Central Time (US & Canada), Mexico City', -6),
(9, '(-05:00) Eastern Time (US & Canada), Bogota', -5),
(10, '(-04:00) Atlantic Time (Canada), Caracas, La Paz', -4),
(11, '(-03:30) Newfoundland', -3.5),
(12, '(-03:00) Brazil, Buenos Aires, Falkland Is.', -3),
(13, '(-02:00) Mid-Atlantic, Ascention Is., St Helena', -2),
(14, '(-01:00) Azores, Cape Verde Islands', -1),
(15, '(GMT) Casablanca, Dublin, London, Lisbon', 0),
(16, '(+01:00) Albania, Slovenia, Macedonia, Norway', 1),
(17, '(+02:00) Finland, Lithuania, Belarus, Ukraine', 2),
(18, '(+03:00) Baghdad, Riyadh, Moscow, Nairobi', 3),
(19, '(+03:30) Tehran', 3.5),
(20, '(+04:00) Abu Dhabi, Baku, Muscat, Tbilisi', 4),
(21, '(+04:30) Kabul', 4.5),
(22, '(+05:00) Ekaterinburg, Karachi, Tashkent', 5),
(23, '(+05:30) Bombay, Calcutta, New Delhi', 5.5),
(24, '(+05:45) Kathmandu', 5.75),
(25, '(+06:00) Almaty, Colombo, Dhaka', 6),
(26, '(+06:30) Yangon, Naypyidaw, Bantam', 6.5),
(27, '(+07:00) Bangkok, Hanoi, Jakarta', 7),
(28, '(+08:00) Hong Kong, Singapore, Taipei', 8),
(29, '(+08:45) Caiguna, Eucla', 8.75),
(30, '(+09:00) Osaka, Seoul, Tokyo, Yakutsk', 9),
(31, '(+09:30) Adelaide, Darwin', 9.5),
(32, '(+10:00) Melbourne, Papua New Guinea, Sydney', 10),
(33, '(+10:30) Lord Howe Island', 10.5),
(34, '(+11:00) Magadan, New Caledonia, Solomon Is.', 11),
(35, '(+11:30) Burnt Pine, Kingston', 11.5),
(36, '(+12:00) Auckland, Fiji, Marshall Island', 12),
(37, '(+12:45) Chatham Islands', 12.75),
(38, '(+13:00) Kamchatka, Anadyr', 13),
(39, '(+14:00) Kiritimati', 14);
